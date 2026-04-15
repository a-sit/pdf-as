package at.gv.egiz.pdfas.lib.impl.stamping.pdfbox3

import at.gv.egiz.pdfas.common.exceptions.PdfAsException
import at.gv.egiz.pdfas.common.settings.ISettings
import at.gv.egiz.pdfas.common.settings.SignatureProfileSettings
import at.gv.egiz.pdfas.common.utils.ImageUtils
import at.gv.egiz.pdfas.lib.impl.pdfbox3.PDFBOXObject
import at.knowcenter.wag.egov.egiz.pdf.PositioningInstruction
import at.knowcenter.wag.egov.egiz.table.Entry
import at.knowcenter.wag.egov.egiz.table.Style
import org.apache.pdfbox.cos.COSArray
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.PDResources
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.common.PDStream
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.visible.PDFTemplateStructure
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.geom.AffineTransform
import java.awt.geom.Point2D
import java.io.ByteArrayOutputStream
import kotlin.properties.Delegates

// This unifies the old PDFAsVisualSignatureProperties, PDFAsVisualSignatureDesigner, PDFAsVisualSignatureBuilder, and PDFAsTemplateCreator
object PDFAsVisualSignature {
    val logger = LoggerFactory.getLogger(PDFAsVisualSignature::class.java)
    fun build(
        pdfObject: PDFBOXObject, visualObject: PDFBOXStamper.VisualObject,
        pos: PositioningInstruction, signatureProfileSettings: SignatureProfileSettings,
        buildPreviewOnly: Boolean
    ): Pair<ByteArray, String> {
        try {
            val main = visualObject.table
            val rotationAngle = pos.rotation
            val origDoc = pdfObject.document

            if (pos.page < 1) {
                throw IllegalArgumentException("PDF pages start at 1, expected page number >= 1, got ${pos.page}")
            }

            val (pageWidth, pageHeight, pageRotation) =
                pdfObject.document!!.documentCatalog.pages.let { pages ->
                    when (pos.isMakeNewPage) {
                        true -> pages[pages.count-1]
                        false -> pages[pos.page-1]
                    }
                }.let { page ->
                    val rotation = page.rotation % 360
                    val mediaBox = page.mediaBox
                    when (rotation % 180) {
                         0 -> Triple(mediaBox.width, mediaBox.height, rotation)
                        90 -> Triple(mediaBox.height, mediaBox.width, rotation)
                        else -> throw IllegalStateException("Invalid page rotation $rotation")
                    }
                }

            val page = when (pos.isMakeNewPage) {
                true -> pdfObject.document!!.documentCatalog.pages.let { it[it.count-1] }
                false -> pdfObject.document!!.documentCatalog.pages[pos.page-1]
            }
            val posx = pos.x
            val posy = pageHeight - pos.y

            // designer formater rectangle params: 0, 0, main.width + 1, main.height + 1
            val pdfStructure = PDFTemplateStructure()
            pdfStructure.procSet = COSArray().apply {
                sequenceOf("PDF","Text","ImageC","ImageB","ImageI")
                    .map(COSName::getPDFName).forEach(this::add)
            }
            pdfStructure.page = PDPage().apply {
                mediaBox = PDRectangle(pageWidth, pageHeight)
                rotation = pageRotation
            }
            pdfStructure.template = PDDocument().apply {
                addPage(pdfStructure.page)
            }
            pdfStructure.acroForm = PDAcroForm(pdfStructure.template).also {
                pdfStructure.template.documentCatalog.acroForm = it
            }
            pdfStructure.signatureField = PDSignatureField(pdfStructure.acroForm)
            pdfStructure.pdSignature = PDSignature().apply {
                pdfStructure.signatureField.value = this
                pdfStructure.signatureField.widgets[0].page = pdfStructure.page
                pdfStructure.page.annotations.add(pdfStructure.signatureField.widgets[0])
                name = "sig"
                byteRange = intArrayOf(0,0,0,0)
                contents = ByteArray(4096)
            }
            pdfStructure.acroFormFields = pdfStructure.acroForm.fields.apply {
                add(pdfStructure.signatureField)
            }
            pdfStructure.acroFormDictionary = pdfStructure.acroForm.cosObject.apply {
                isDirect = true
                setInt(COSName.SIG_FLAGS, 3)
                setString(COSName.DA, "/sylfaen 0 Tf 0 g")
            }
            pdfStructure.affineTransform = AffineTransform(floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f))
            pdfStructure.signatureRectangle = createSignatureRectangle(
                pageWidth = pageWidth, pageHeight = pageHeight,
                posx, posy,
                main.width, main.height,
                rotationAngle, pageRotation,
            ).also {
                pdfStructure.signatureField.widgets[0].rectangle = it
            }
            pdfStructure.formatterRectangle = FloatArray(4).also {
                pdfStructure.affineTransform.transform(
                    floatArrayOf(0.0f, 0.0f, main.width + 1, main.height + 1), 0,
                    it, 0,
                    2)
            }.let {
                PDRectangle().apply {
                    upperRightX = it[0]
                    upperRightY = it[1]
                    lowerLeftX = it[2]
                    lowerLeftY = it[3]
                }
            }

            pdfStructure.holderFormStream = PDStream(pdfStructure.template)
            pdfStructure.holderFormResources = PDResources()
            pdfStructure.holderForm = PDFormXObject(pdfStructure.holderFormStream).apply {
                resources = pdfStructure.holderFormResources
                bBox = pdfStructure.formatterRectangle
                formType = 1
            }

            pdfStructure.appearanceDictionary = PDAppearanceDictionary().apply {
                cosObject.isDirect = true
                setNormalAppearance(PDAppearanceStream(pdfStructure.holderForm.cosObject).apply {
                    setMatrix(AffineTransform().apply {
                        setToIdentity()
                        rotate(Math.toRadians(rotationAngle.toDouble() + pageRotation))
                    })
                })
            }.also { pdfStructure.signatureField.widgets[0].appearance = it }

            val (alternativeTableCaption, innerFormResources) =
                Table.draw(main,
                    pdfStructure.template, pdfStructure.page,
                    main.width, main.height,
                    signatureProfileSettings, pdfObject.settings)

            pdfStructure.innerFormResources = innerFormResources

            pdfStructure.setInnterFormStream(PDStream(pdfStructure.template, pdfStructure.page.contents))

            pdfStructure.innerForm = PDFormXObject(pdfStructure.innerFormStream).apply {
                resources = pdfStructure.innerFormResources
                bBox = pdfStructure.formatterRectangle
                formType = 1
            }.also {
                pdfStructure.innerFormName = pdfStructure.holderFormResources.add(it, "FRM")
            }

            pdfStructure.innerForm.resources.cosObject.setItem(COSName.PROC_SET, pdfStructure.procSet)
            pdfStructure.page.cosObject.setItem(COSName.PROC_SET, pdfStructure.procSet)
            pdfStructure.innerFormResources.cosObject.setItem(COSName.PROC_SET, pdfStructure.procSet)
            pdfStructure.holderFormResources.cosObject.setItem(COSName.PROC_SET, pdfStructure.procSet)

            if (!buildPreviewOnly) {
                val holderFormComment = pdfStructure.affineTransform.run {
                    "q $scaleX $shearY $shearX $scaleY $translateX $translateY cm /${pdfStructure.innerFormName.name} Do Q"
                }
                logger.debug("Holder form stream: {}", holderFormComment)

                val innerFormComment = pdfStructure.innerFormStream.toByteArray()

                pdfStructure.holderFormStream.createOutputStream().use {
                    it.write(holderFormComment.trim().filterNot {c -> c == '\r' || c == '\n' }.toByteArray())
                }
                pdfStructure.innerFormStream.createOutputStream().use {
                    it.write(innerFormComment)
                }
                logger.debug("Injected appearance stream to PDF")
            }

            pdfStructure.visualSignature = pdfStructure.template.document

            pdfStructure.widgetDictionary = pdfStructure.signatureField.widgets[0].cosObject.apply {
                isNeedToBeUpdated = true
                setItem(COSName.DR, pdfStructure.holderFormResources.cosObject)
            }

            if (signatureProfileSettings.isPDFA3) {
                pdfStructure.template.pages.forEach { page ->
                    page.resources.fontNames.forEach { fontName ->
                        val pdFont = page.resources.getFont(fontName)
                        if (pdFont is PDType0Font) {
                            pdFont.descendantFont?.fontDescriptor?.cosObject?.removeItem(COSName.CID_SET)
                        }
                    }
                }
            }

            return Pair(
                ByteArrayOutputStream().also {
                    pdfStructure.template.save(it)
                    pdfStructure.template.close()
                }.toByteArray(),
                alternativeTableCaption)
        } catch (e: Throwable) {
            logger.warn("Failed to create visual signature block", e)
            throw PdfAsException("Failed to create visual signature block", e)
        }
    }

    fun createSignatureRectangle(
        pageWidth: Float, pageHeight: Float,
        /** from left edge of page */
        signaturePosX: Float,
        /** from top edge of page */
        signaturePosY: Float,
        signatureWidth: Float, signatureHeight: Float, rotationDegrees: Float, pageRotationDegrees: Int
    ): PDRectangle {
        val leftX = signaturePosX
        /** from bottom edge of page */
        val topY = pageHeight - signaturePosY
        logger.debug("POS: ({}, {})", leftX, topY)
        logger.debug("SIZE: {} by {}", signatureWidth, signatureHeight)
        val upperRight = Point2D.Float(leftX + signatureWidth, topY)
        val lowerLeft = Point2D.Float(leftX, topY - signatureHeight)
        logger.debug("Corners: upper right {}, lower left {}", upperRight, lowerLeft)

        ((rotationDegrees + pageRotationDegrees) % 360).takeIf { it != 0.0f }
            ?.let { deg ->
                AffineTransform().apply {
                    setToRotation(
                        Math.toRadians(deg.toDouble()),
                        lowerLeft.x.toDouble(),
                        lowerLeft.y.toDouble())
                }.let {
                    it.transform(upperRight, upperRight)
                    it.transform(lowerLeft, lowerLeft)
                }
        }
        logger.debug("Rotated corners: upper right {}, lower left {}", upperRight, lowerLeft)

        when (pageRotationDegrees) {
            90 -> AffineTransform().apply {
                setToTranslation(
                    (pageHeight - topY - leftX + signatureHeight).toDouble(),
                    (leftX + signatureHeight - topY).toDouble()
                )
            }
            180 -> AffineTransform().apply {
                setToTranslation(
                    (pageWidth - 2 * leftX).toDouble(),
                    (pageHeight - 2 * (topY - signatureHeight)).toDouble()
                )
            }
            270 -> AffineTransform().apply {
                setToTranslation(
                    (-signatureHeight + topY - leftX).toDouble(),
                    (pageWidth - (topY - signatureHeight) - leftX).toDouble()
                )
            }
            else -> null
        }?.let {
            it.transform(upperRight, upperRight)
            it.transform(lowerLeft, lowerLeft)
        }
        logger.debug("Adjusted for page rotation: upper right {}, lower left {}", upperRight, lowerLeft)

        return PDRectangle().apply {
            upperRightX = upperRight.x
            upperRightY = upperRight.y
            lowerLeftX = lowerLeft.x
            lowerLeftY = lowerLeft.y
        }.also { logger.debug("Signature rectangle: {}", it) }
    }

    data class ImageObject(val image: PDImageXObject, /** width/height */ val aspectRatio: Float)

    object Table {
        fun draw(
            mainTable: PDFBOXStamper.Table,
            document: PDDocument, page: PDPage,
            width: Float, height: Float,
            signatureProfileSettings: SignatureProfileSettings, globalSettings: ISettings
        ): Pair<String, PDResources> {
            val innerFormResources = PDResources()
            page.resources = innerFormResources
            PDPageContentStream(document, page).use { stream ->
                val imageCache = mutableMapOf<String, ImageObject>()
                val alternativeTableCaption = stream.drawTable(
                    mainTable, isSubtable = false,
                    0.0f, 1.0f, width, height,
                    document, innerFormResources, imageCache, signatureProfileSettings, globalSettings
                )
                return Pair(alternativeTableCaption, innerFormResources)
            }
        }

        private fun PDPageContentStream.drawTable(
            table: PDFBOXStamper.Table, isSubtable: Boolean,
            tableLeftX: Float, tableBottomY: Float, width: Float, height: Float,
            document: PDDocument, innerFormResources: PDResources, imageCache: MutableMap<String, ImageObject>,
            signatureProfileSettings: SignatureProfileSettings, globalSettings: ISettings
        ): String {

            logger.debug("Drawing table at ({},{}), size {} by {}\n{}", tableLeftX, tableBottomY, width, height, table.abstractTable)
            table.abstractTable.width = width

            // draw background
            table.bgColor?.let { drawRect(tableLeftX, tableBottomY, table.width, table.height, it) }

            val colSizes = (table.colRelativeWidths ?: FloatArray(table.colCount) { 1.0f }).let { sizes ->
                val factor = width / sizes.sum()
                FloatArray(sizes.size) { sizes[it]*factor }
            }

            val alternateTableCaption = StringBuilder()

            (0..<table.rowCount).asSequence()
                .map { it to table.getRow(it) }
                .fold(tableBottomY + height)
                { rowTopY, (rowIdx, row) ->
                    (rowTopY - table.rowHeights[rowIdx]).also { rowBottomY ->
                        // draw top line of row
                        drawLine(tableLeftX, rowTopY, tableLeftX + width, rowTopY, table.style.border, Color.BLACK)

                        var cellLeftX = tableLeftX
                        var colIdx = 0
                        while (true) {
                            // left border
                            drawLine(cellLeftX, rowTopY, cellLeftX, rowBottomY, table.style.border, Color.BLACK)

                            if (colIdx >= row.size) break

                            val cell = row[colIdx]
                            val nextColIdx = colIdx + cell.colSpan
                            val cellRightX = cellLeftX + (colIdx..<nextColIdx).asSequence().map(colSizes::get).sum()

                            // taken from pdfbox 2 code:
                            // "cell only contains default values so table style is the primary style"
                            cell.style = Style.doInherit(table.style, cell.style)

                            when (cell.type) {
                                Entry.TYPE_CAPTION -> {
                                    val captionText = cell.value as String
                                    alternateTableCaption.append(captionText).append(":\n")
                                    drawString(
                                        content = captionText,
                                        leftX = cellLeftX, topY = rowTopY,
                                        width = cellRightX - cellLeftX, height = rowTopY - rowBottomY,
                                        hAlign = cell.style.hAlign, vAlign = cell.style.vAlign,
                                        padding = table.padding, font = table.font, color = Color.BLACK,
                                        settings = globalSettings
                                    )
                                }
                                Entry.TYPE_VALUE -> {
                                    val cellText = cell.value as String
                                    alternateTableCaption.append(cellText).append('\n')
                                    drawString(
                                        content = cellText,
                                        leftX = cellLeftX, topY = rowTopY,
                                        width = cellRightX - cellLeftX, height = rowTopY - rowBottomY,
                                        hAlign = cell.style.valueHAlign, vAlign = cell.style.valueVAlign,
                                        padding = table.padding, font = table.valueFont, color = Color.BLACK,
                                        settings = globalSettings
                                    )
                                }
                                Entry.TYPE_IMAGE ->
                                    drawImage(
                                        imageIdentifier = cell.value as String,
                                        leftX = cellLeftX, topY = rowTopY,
                                        width = cellRightX - cellLeftX, height = rowTopY - rowBottomY,
                                        padding = table.padding,
                                        scaleToFit = table.style.imageScaleToFit,
                                        hAlign = cell.style.imageHAlign, vAlign = cell.style.imageVAlign,
                                        document = document, innerFormResources = innerFormResources,
                                        imageCache = imageCache, globalSettings = globalSettings,
                                        signatureProfileSettings = signatureProfileSettings
                                    )
                                Entry.TYPE_TABLE -> {
                                    val subTable = cell.value as PDFBOXStamper.Table
                                    subTable.abstractTable.style = Style.doInherit(table.style, cell.style)
                                    drawTable(
                                        subTable, isSubtable = true,
                                        tableLeftX = cellLeftX, tableBottomY = rowBottomY,
                                        width = cellRightX - cellLeftX, height = rowTopY - rowBottomY,
                                        document = document, innerFormResources = innerFormResources,
                                        signatureProfileSettings = signatureProfileSettings,
                                        imageCache = imageCache, globalSettings = globalSettings
                                    ).also { alternateTableCaption.append(it) }
                                }
                            }

                            // setup next iteration
                            cellLeftX = cellRightX
                            colIdx = nextColIdx
                        }
                    }
                }.also {
                    // draw bottom table border
                    drawLine(tableLeftX, it, tableLeftX + width, it, table.style.border, Color.BLACK)
                }

            return alternateTableCaption.toString()
        }

        private fun PDPageContentStream.drawRect(
            leftX: Float, bottomY: Float, width: Float, height: Float,
            color: Color
        ) {
            setNonStrokingColor(color)
            addRect(leftX, bottomY, width, height)
            fill()
        }

        private fun PDPageContentStream.drawLine(
            startX: Float, startY: Float,
            endX: Float, endY: Float,
            width: Float, color: Color
        ) {
            if (width <= 0) return
            setStrokingColor(color)
            setLineWidth(width)
            moveTo(startX, startY)
            lineTo(endX, endY)
            stroke()
        }

        private fun PDDocument.loadImage(
            identifier: String,
            signatureProfileSettings: SignatureProfileSettings, globalSettings: ISettings
        ) : ImageObject {
            var image = ImageUtils.getImage(identifier, globalSettings)
            if (
                signatureProfileSettings.isPDFA ||
                /* not sure what this does -- copied from pdfbox 2 code */
                ((image.alphaRaster == null) && image.colorModel.hasAlpha())
            ) {
                image = ImageUtils.removeAlphaChannel(image)
            }
            return ImageObject(
                image = LosslessFactory.createFromImage(this@loadImage, image),
                aspectRatio = image.width.toFloat() / image.height.toFloat()
            )
        }

        private fun PDPageContentStream.drawImage(
            imageIdentifier: String,
            leftX: Float, topY: Float, width: Float, height: Float, padding: Float,
            scaleToFit: Style.ImageScaleToFit?, hAlign: String?, vAlign: String?,
            document: PDDocument, innerFormResources: PDResources, imageCache: MutableMap<String, ImageObject>,
            signatureProfileSettings: SignatureProfileSettings, globalSettings: ISettings
        ) {
            val image = imageCache.computeIfAbsent(imageIdentifier) { identifier ->
                document.loadImage(identifier, signatureProfileSettings, globalSettings).also {
                    innerFormResources.add(it.image, "Im")
                }
            }

            val cellWidth = (width - 2*padding)
            val cellHeight = (height - 2*padding)

            val renderBoxWidth = scaleToFit?.width ?: cellWidth
            val renderBoxHeight = scaleToFit?.height ?: cellHeight
            val boxAspectRatio = renderBoxWidth/renderBoxHeight
            val (imageWidth, imageHeight) = when {
                boxAspectRatio > image.aspectRatio ->
                    /** box width is too wide, height is limiting factor */
                    Pair(renderBoxHeight * image.aspectRatio, renderBoxHeight)
                boxAspectRatio < image.aspectRatio ->
                    /** box width is too low, width is limiting factor */
                    Pair(renderBoxWidth, renderBoxWidth / image.aspectRatio)
                else ->
                    Pair(renderBoxWidth, renderBoxHeight)
            }
            assert(imageWidth <= renderBoxWidth)
            assert(imageHeight <= renderBoxHeight)

            val imageLeftX = leftX + padding + when (hAlign) {
                Style.RIGHT -> (cellWidth - imageWidth)
                Style.LEFT -> 0.0f
                else -> (cellWidth - imageWidth)/2
            }
            val imageTopY = topY - padding - when (vAlign) {
                Style.BOTTOM -> (cellHeight - imageHeight)
                Style.MIDDLE -> (cellHeight - imageHeight)/2
                else -> 0.0f
            }
            drawImage(image.image, imageLeftX, imageTopY - imageHeight, imageWidth, imageHeight)
        }

        private fun PDPageContentStream.drawString(
            content: String,
            leftX: Float, topY: Float, width: Float, height: Float, padding: Float, font: PDFBOXStamper.Table.Font,
            hAlign: String?, vAlign: String?, color: Color, settings: ISettings
        ) {

            val lines = content.split('\n')
            val actualHeight = height - 2*padding
            val actualWidth = width - 2*padding

            val textHeight = font.fontSize*lines.size
            val actualY = (topY - padding) - when (vAlign) {
                Style.BOTTOM -> (actualHeight - textHeight)
                Style.MIDDLE -> (actualHeight - textHeight)/2
                else -> 0.0f
            }

            setNonStrokingColor(color)
            beginText()
            if ((hAlign == Style.LINECENTER) && (lines.size > 1)) {
                setFont(font.font, font.fontSize)
                var previousBonusPadding by Delegates.notNull<Float>()
                lines.forEachIndexed { i, line ->
                    val width = font.font.getStringWidth(line) / 1000.0f * font.fontSize
                    val bonusPadding = (actualWidth - width) / 2.0f
                    if (i == 0) {
                        newLineAtOffset(
                            leftX + padding + bonusPadding,
                            (actualY - (1 + font.font.fontDescriptor.descent / 1000.0f) * font.fontSize)
                        )
                    } else {
                        newLineAtOffset(bonusPadding - previousBonusPadding, -font.fontSize)
                    }
                    showText(line)
                    previousBonusPadding = bonusPadding
                }
            } else {
                val textWidth by lazy { lines.maxOf { font.font.getStringWidth(it) } / 1000.0f * font.fontSize }
                val actualX = (leftX + padding) + when (hAlign) {
                    Style.CENTER, Style.LINECENTER -> (actualWidth - textWidth)/2
                    Style.RIGHT -> (actualWidth - textWidth)
                    else -> 0.0f
                }
                setFont(font.font, font.fontSize)
                lines.forEachIndexed { i, line ->
                    if (i == 0) {
                        newLineAtOffset(
                            actualX,
                            (actualY - (1 + font.font.fontDescriptor.descent / 1000.0f) * font.fontSize)
                        )
                    } else {
                        newLineAtOffset(0.0f, -font.fontSize)
                    }

                    showText(line)
                }
            }
            endText()
        }
    }
}