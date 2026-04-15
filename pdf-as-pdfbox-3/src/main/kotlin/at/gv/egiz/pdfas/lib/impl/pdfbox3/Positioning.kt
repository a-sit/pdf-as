package at.gv.egiz.pdfas.lib.impl.pdfbox3

import at.gv.egiz.pdfas.common.exceptions.PdfAsException
import at.gv.egiz.pdfas.common.settings.IProfileConstants
import at.gv.egiz.pdfas.common.settings.ISettings
import at.gv.egiz.pdfas.common.settings.SignatureProfileSettings
import at.gv.egiz.pdfas.lib.api.IConfigurationConstants
import at.gv.egiz.pdfas.lib.impl.signing.pdfbox3.PDFBOXSigner.checkPDFPermissions
import at.gv.egiz.pdfas.lib.impl.stamping.pdfbox3.PDFBOXStamper
import at.knowcenter.wag.egov.egiz.pdf.PositioningInstruction
import at.knowcenter.wag.egov.egiz.pdf.TablePos
import at.knowcenter.wag.egov.egiz.pdf.TablePos.PAGE_MODE
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.graphics.color.PDColor
import org.apache.pdfbox.rendering.PDFRenderer
import org.apache.pdfbox.rendering.PageDrawer
import org.apache.pdfbox.rendering.PageDrawerParameters
import org.apache.pdfbox.tools.imageio.ImageIOUtil
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.image.BufferedImage

object Positioning {
    private val logger = LoggerFactory.getLogger(Positioning::class.java)
    /** The left/right margin */
    const val SIGNATURE_MARGIN_HORIZONTAL = 50f
    /** The top/bottom margin */
    const val SIGNATURE_MARGIN_VERTICAL = 20f
    fun determineTablePositioning(
        pos: TablePos, pdfDataSource: PDDocument,
        pdfTable: PDFBOXStamper.VisualObject, globalSettings: ISettings,
        signatureProfileSettings: SignatureProfileSettings
    ): PositioningInstruction {

        pdfDataSource.checkPDFPermissions()

        val hasExistingSignatures = runCatching {
            pdfDataSource.signatureFields.any { it.signature != null }
        }.getOrElse { e ->
            logger.warn("Failed to extract existing signatures from PDF.", e)
            false
        }

        var (pageNum, makeNewPage) = when (pos.pageMode) {
            PAGE_MODE.EXACT ->
                pos.page.takeIf { it <= pdfDataSource.numberOfPages }
                    ?.let { Pair(it, false) }
                    ?:
                        Pair(pdfDataSource.numberOfPages, true)
                            .also { logger.info("Document is shorter than requested page for signature block. Adding new page...") }
            PAGE_MODE.NEW -> Pair(pdfDataSource.numberOfPages, true)
            PAGE_MODE.LAST, PAGE_MODE.AUTO -> Pair(pdfDataSource.numberOfPages, false)
        }

        if (makeNewPage && hasExistingSignatures) {
            makeNewPage = getNewPageFallback(signatureProfileSettings, globalSettings)
        }

        val (pageWidth, pageHeight) = pdfDataSource.getPage(pageNum-1).let { pdPage ->
            val cropBox = pdPage.cropBox
            when (val rotation = pdPage.rotation % 360) {
                 0, 180 -> Pair(cropBox.width, cropBox.height)
                90, 270 -> Pair(cropBox.height, cropBox.width)
                else -> throw IllegalStateException("Invalid page rotation $rotation")
            }
        }

        val hasExplicitPosX = !pos.isXauto
        val hasExplicitWidth = !pos.isWauto
        val (posX, width) = when {
             hasExplicitPosX &&  hasExplicitWidth -> Pair(pos.posX, pos.width)
            !hasExplicitPosX &&  hasExplicitWidth -> Pair((pageWidth - pos.width)/2, pos.width)
             hasExplicitPosX && !hasExplicitWidth -> Pair(pos.posX, pageWidth - 2*pos.posX)
            /*!hasExplicitPosX && !hasExplicitWidth*/ else ->
                Pair(SIGNATURE_MARGIN_HORIZONTAL, pageWidth - 2*SIGNATURE_MARGIN_HORIZONTAL)
        }
        pdfTable.width = width
        pdfTable.fixWidth()

        if (pos.pageMode == PAGE_MODE.LAST) {
            return PositioningInstruction(
                false, pageNum,
                posX, if (pos.isYauto) pageHeight - SIGNATURE_MARGIN_VERTICAL else pos.posY,
                pos.rotation)
        }

        // explicit y or invisible signature
        if (!pos.isYauto || pdfTable.height == 0.0f) {
            if (makeNewPage) pageNum = pdfDataSource.numberOfPages + 1
            return PositioningInstruction(makeNewPage, pageNum, posX, pos.posY, pos.rotation)
        }

        if (makeNewPage) {
            return PositioningInstruction(
                true, pdfDataSource.numberOfPages + 1,
                posX, pageHeight - SIGNATURE_MARGIN_VERTICAL, pos.rotation)
        }

        // ok, y-position is automatic, and we are not making a new page
        // therefore, we need to go looking for the end of the page content
        val pageContentHeight = calculatePageLength(pdfDataSource, pageNum-1, pos.footerLine.toInt(), globalSettings)
        val candidateY = pageHeight - pageContentHeight - SIGNATURE_MARGIN_VERTICAL

        if (candidateY - pos.footerLine <= pdfTable.height) {
            if (pos.pageMode == PAGE_MODE.AUTO)
                makeNewPage = true
            else
                pageNum = pdfDataSource.numberOfPages

            if (makeNewPage && hasExistingSignatures) {
                makeNewPage = getNewPageFallback(signatureProfileSettings, globalSettings)
            }
            if (makeNewPage) {
                pageNum = pdfDataSource.numberOfPages + 1
            }
            return PositioningInstruction(
                makeNewPage, pageNum,
                posX, pageHeight - SIGNATURE_MARGIN_VERTICAL, pos.rotation
            )
        }
        return PositioningInstruction(
            false, pageNum,
            posX, candidateY, pos.rotation
        )
    }

    private fun getNewPageFallback(signatureProfileSettings: SignatureProfileSettings, globalSettings: ISettings): Boolean {
        logger.debug("Signature block would need to be on a new page, but you cannot add a new page to a signed document")
        if (signatureProfileSettings.getValue(IProfileConstants.SIG_NEWPAGE_FORCE).toBoolean()) {
            logger.info("New pages are not allowed on signed document, but the profile configuration overrides this")
            return true
        } else if (globalSettings.getValue(IConfigurationConstants.SIG_BLOCK_LESS_SPACE_STOPPING_WITH_ERROR).toBoolean()) {
            throw PdfAsException("error.pdf.stamp.12")
        } else {
            logger.info("Placing signature block on last page without free space checks")
            return false
        }
    }

    private val RENDERER_BACKGROUND_COLOR = Color(152, 254, 52)
    private val RENDERER_FOREGROUND_COLOR = Color(234, 14, 184, 211)
    private fun calculatePageLength(
        pdfDataSource: PDDocument, pageNum: Int,
        footerSize: Int, globalSettings: ISettings
    ): Int {
        try {
            val (cropBox, rotation) = pdfDataSource.getPage(pageNum).let {
                Pair(it.cropBox, it.rotation)
            }
            val isRotated = (rotation % 180 != 0)
            val (imageWidth, imageHeight) = when (isRotated) {
                true -> Pair(cropBox.height, cropBox.width)
                false -> Pair(cropBox.width, cropBox.height)
            }
            val image = BufferedImage(
                imageWidth.toInt(), imageHeight.toInt(),
                BufferedImage.TYPE_INT_ARGB)
            Renderer(pdfDataSource).renderPageToGraphics(
                pageNum,
                image.createGraphics().apply { background = RENDERER_BACKGROUND_COLOR})
            globalSettings.getValue(IConfigurationConstants.SIG_PLACEMENT_DEBUG_OUTPUT)?.let {
                ImageIOUtil.writeImage(image, it, 72)
            }

            val bgColor = when (globalSettings.getValue(IConfigurationConstants.BG_COLOR_DETECTION).toBoolean()) {
                true -> {
                    /*
                     * Only used if background color should be determined automatically.
                     * That can be necessary of PDF contains page-size images.
                     */
                    val topLeft = image.getRGB(5,5)
                    val topRight = image.getRGB(image.width-5, 5)
                    val bottomLeft = image.getRGB(5, image.height-5)
                    val bottomRight = image.getRGB(image.width-5, image.height-5)
                    // pick most common color
                    sequenceOf(topLeft, topRight, bottomLeft, bottomRight)
                        .groupingBy { it }
                        .eachCount()
                        .maxBy { it.value }.key
                }
                false -> RENDERER_BACKGROUND_COLOR.rgb
            }
            return ((image.height - 1 - footerSize).downTo(1).firstOrNull { row ->
                (0..<image.width).any { col -> (image.getRGB(col, row) != bgColor) }
            } ?: 0)

        } catch (e: Throwable) {
            logger.warn("Could not determine page length, ignoring page content", e)
            return 0
        }
    }

    private class Renderer(pdf: PDDocument): PDFRenderer(pdf) {
        class Drawer(parameters: PageDrawerParameters) : PageDrawer(parameters) {
            override fun getPaint(color: PDColor) = RENDERER_FOREGROUND_COLOR
        }
        override fun createPageDrawer(parameters: PageDrawerParameters): PageDrawer {
            return Drawer(parameters)
        }
    }
}