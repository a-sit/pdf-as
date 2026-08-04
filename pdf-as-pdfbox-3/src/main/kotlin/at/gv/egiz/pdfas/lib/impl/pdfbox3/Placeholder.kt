package at.gv.egiz.pdfas.lib.impl.pdfbox3

import at.gv.egiz.pdfas.common.exceptions.PDFIOException
import at.gv.egiz.pdfas.common.exceptions.PdfAsException
import at.gv.egiz.pdfas.common.exceptions.PlaceholderExtractionException
import at.gv.egiz.pdfas.lib.impl.placeholder.PlaceholderExtractor
import at.gv.egiz.pdfas.lib.impl.placeholder.PlaceholderExtractorConstants
import at.gv.egiz.pdfas.lib.impl.placeholder.SignaturePlaceholderData
import at.gv.egiz.pdfas.lib.impl.status.PDFObject
import at.knowcenter.wag.egov.egiz.pdf.TablePos
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.ReaderException
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import org.apache.pdfbox.contentstream.PDFStreamEngine
import org.apache.pdfbox.contentstream.operator.Operator
import org.apache.pdfbox.contentstream.operator.OperatorProcessor
import org.apache.pdfbox.cos.COSBase
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDPropBuild
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDPropBuildDataDict
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature
import org.apache.pdfbox.util.Matrix
import org.slf4j.LoggerFactory
import java.awt.geom.AffineTransform
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.util.Hashtable
import java.util.Properties
import java.util.Vector
import kotlin.math.ceil
import kotlin.math.floor


object PDFBoxPlaceholderExtractor : PlaceholderExtractor {
    override fun extract(
        pdfObject: PDFObject,
        placeholderID: String?,
        matchMode: Int
    ): SignaturePlaceholderData? {
        if (pdfObject !is PDFBOXObject) throw PdfAsException("Invalid state")
        try {
            return Extractor().extract(pdfObject.document!!, placeholderID, matchMode)
        } catch (e: Throwable) {
            when (e) {
                is IOException, is ClassNotFoundException, is InstantiationException,
                is IllegalAccessException, is NoSuchMethodException, is InvocationTargetException
                    -> throw PDFIOException("error.pdf.io.04", e)
                else
                    -> throw e
            }
        }
    }

    @JvmStatic
    fun findEmptySignatureFields(doc: PDDocument) =
        doc.signatureFields.asSequence().filter { it.signature == null }.mapNotNull { it.partialName }.toList()

    /**
     * Returns the next unused signature placeholder
     *
     * @param doc The document to be searched for signature placeholders
     * @return The next unused signature placeholder, or `null`
     */
    // needed for PDF-Over
    @JvmStatic
    fun getNextUnusedSignaturePlaceholder(doc: PDDocument) =
        Extractor().extract(
            doc, "1",
            PlaceholderExtractorConstants.PLACEHOLDER_MATCH_MODE_SORTED)

    class Extractor : PDFStreamEngine() {
        companion object {
            private val logger = LoggerFactory.getLogger(Extractor::class.java)
        }
        init {
            Properties().apply {
                load(PDFBoxPlaceholderExtractor::class.java.classLoader
                    .getResourceAsStream("placeholder/pdfbox-reader-3.properties"))
            }.values.forEach {
              val klass = Class.forName(it as String)

              val processor = klass
                  .getDeclaredConstructor(PDFStreamEngine::class.java)
                  .newInstance(this) as OperatorProcessor

              addOperator(processor)
            }
        }
        var currentPageNumber: Int = 0
        val placeholders = mutableListOf<SignaturePlaceholderData>()
        val seenPlaceholderNames = mutableSetOf<String>()
        lateinit var placeholderNamesOfExistingSignatures: Set<String>
        fun extract(document: PDDocument, targetPlaceholderID: String?, matchMode: Int): SignaturePlaceholderData? {
            placeholderNamesOfExistingSignatures =
                document.signatureDictionaries.asSequence()
                    .mapNotNull { it.signaturePlaceholderId }
                    .toSet()

            document.pages.forEachIndexed { i, page ->
                try {
                    currentPageNumber = i+1
                    val placeholdersBeforePage = placeholders.size
                    if ((page.contents != null) && (page.resources != null) && (page.contentStreams != null)) {
                        // this causes page processing into processOperator
                        processPage(page)
                    }

                    logger.debug("Searching for requested placeholder {} (match mode {}) in page {} only...",
                        targetPlaceholderID, matchMode, currentPageNumber)

                    // process only placeholders that were found in the current page to see if we find an exact match
                    (placeholdersBeforePage..<placeholders.size).forEach { i ->
                        val placeholder = placeholders[i]
                        if (targetPlaceholderID != null) {
                            if (placeholder.id != null && matchPlaceholderId(targetPlaceholderID, placeholder.id)) {
                                return placeholder
                            }
                        } else {
                            if (matchMode != PlaceholderExtractorConstants.PLACEHOLDER_MATCH_MODE_SORTED && placeholder.id == null) {
                                return placeholder
                            }
                        }
                    }
                } catch (e: Throwable) {
                    throw PDFIOException("error.pdf.io.04", e)
                }
            }

            if (matchMode == PlaceholderExtractorConstants.PLACEHOLDER_MATCH_MODE_STRICT) {
                throw PlaceholderExtractionException("error.pdf.stamp.09")
            }

            if (placeholders.isEmpty()) return null

            logger.debug("Searching for requested placeholder {} (match mode {}) in entire document...",
                targetPlaceholderID, matchMode)

            if (matchMode == PlaceholderExtractorConstants.PLACEHOLDER_MATCH_MODE_SORTED) {
                // get the placeholder with the lowest id
                var currentPlaceholder: SignaturePlaceholderData? = null
                placeholders.forEach { placeholder ->
                    if (placeholder.id == null) return@forEach
                    if (currentPlaceholder == null || placeholderIdLessThan(placeholder.id!!, currentPlaceholder.id!!)) {
                        currentPlaceholder = placeholder
                    }
                }
                if (currentPlaceholder != null) return currentPlaceholder
            }

            // get any placeholder with id null
            placeholders.firstOrNull { it.id == null }?.let { return it }

            // lenient mode: get any placeholder even if it has an id
            if (matchMode == PlaceholderExtractorConstants.PLACEHOLDER_MATCH_MODE_LENIENT) return placeholders.first()

            // give up
            return null
        }

        private fun matchPlaceholderId(targetId: String, actualId: String): Boolean {
            try {
                val targetInt = Integer.parseInt(targetId)
                val actualInt = Integer.parseInt(actualId)
                return (targetInt == actualInt)
            } catch (_: NumberFormatException) {
                logger.trace("Cannot parse identifiers ({},{}) as numbers, comparing as strings", targetId, actualId)
                return targetId.equals(actualId, ignoreCase = true)
            }
        }

        private fun placeholderIdLessThan(left: String, right: String): Boolean {
            try {
                val leftInt = Integer.parseInt(left)
                val rightInt = Integer.parseInt(right)
                return (leftInt < rightInt)
            } catch (_: NumberFormatException) {
                logger.trace("placeholderIdLessThan: falling back to String compare")
                return left.compareTo(right, true) < 0
            }
        }

        private fun detectQRCodeFromImage(imageObj: PDImageXObject): SignaturePlaceholderData? {
            val image = imageObj.image ?: run {
                logger.info("Unable to extract image for QR code analysis. {} not supported. Add additional JAI Image filters to your classpath. Refer to https://jai.dev.java.net. Skipping image.",
                    imageObj.suffix?.let { "${it.uppercase()} images"} ?: "Image type")
                return null
            }
            if (image.height < 10 || image.width < 10) {
                logger.debug("Image too small for QR code. Skipping.")
                return null
            }

            val bitmap = BinaryBitmap(HybridBinarizer(BufferedImageLuminanceSource(image)))
            val result = try {
                MultiFormatReader().decode(bitmap,
                    Hashtable<DecodeHintType, Any>().apply {
                        put(DecodeHintType.POSSIBLE_FORMATS, Vector<BarcodeFormat>().apply {
                            add(BarcodeFormat.QR_CODE)
                        })
                    }
                ).text ?: return null
            } catch (e: ReaderException) {
                if (e !is NotFoundException) {
                    logger.info("Failed to decode image", e)
                }
                return null
            } catch (e: ArrayIndexOutOfBoundsException) {
                logger.info("Failed to decode image. Probably a zxing bug.", e)
                return null
            }

            if (!result.startsWith(PlaceholderExtractorConstants.QR_PLACEHOLDER_IDENTIFIER)) {
                logger.warn("QR-Code found but does not start with \"${PlaceholderExtractorConstants.QR_PLACEHOLDER_IDENTIFIER}\". Ignoring.")
                return null
            }

            var profile: String? = null
            var type: String? = null
            var sigKey: String? = null
            var id: String? = null
            result.splitToSequence(';').drop(1).forEach {
                val parts = it.split('=')
                if (parts.size != 2) {
                    logger.debug("Invalid parameter in placeholder data: $it")
                    return@forEach
                }
                when (parts[0].lowercase()) {
                    SignaturePlaceholderData.ID_KEY      -> id = parts[1]
                    SignaturePlaceholderData.PROFILE_KEY -> profile = parts[1]
                    SignaturePlaceholderData.SIG_KEY_KEY -> sigKey = parts[1]
                    SignaturePlaceholderData.TYPE_KEY    -> type = parts[1]
                }
            }
            return SignaturePlaceholderData(profile, type, sigKey, id)
        }

        private fun buildUniqueObjectName(objectName: COSName) =
            sequence {
                val baseName = objectName.name
                yield(baseName)
                yieldAll((1..Int.MAX_VALUE).asSequence().map { i -> "${baseName}_${i}"})
            }.first { !seenPlaceholderNames.contains(it) }

        override fun processOperator(operator: Operator, arguments: List<COSBase>) {
            run {
                if (operator.name != "Do") return@run
                val objectName = arguments[0] as COSName
                val xObject = resources.getXObject(objectName)
                if (xObject !is PDImageXObject) return@run

                val signaturePlaceholderData = detectQRCodeFromImage(xObject) ?: return@run

                val placeholderName = buildUniqueObjectName(objectName)
                seenPlaceholderNames.add(placeholderName)
                if (placeholderNamesOfExistingSignatures.contains(placeholderName)) {
                    logger.debug("Not processing placeholder {}, there is already a corresponding signature", placeholderName)
                    return@run
                }

                val page = this.currentPage
                val pageRotation = page.rotation % 360
                val rotationInverseMatrix = Matrix(
                    AffineTransform().apply {
                        setToRotation(Math.toRadians(pageRotation.toDouble()))
                        invert()
                    }
                )
                val unrotatedTransformMatrix = this.graphicsState.currentTransformationMatrix.multiply(rotationInverseMatrix)

                logger.debug("Page height: {}", page.cropBox.height)
                logger.debug("Page width: {}", page.cropBox.width)

                // TODO i've taken this from SignaturePlaceholderExtractor in the pdfbox 2 module, but this feels suspect
                var x = unrotatedTransformMatrix.translateX
                var y = unrotatedTransformMatrix.translateY + unrotatedTransformMatrix.scaleY

                when (pageRotation) {
                    90 -> {
                        y += page.cropBox.width
                    }
                    180 -> {
                        x += page.cropBox.width
                        y += page.cropBox.height
                    }
                    270 -> {
                        x += page.cropBox.height
                    }
                }

                val w = unrotatedTransformMatrix.scaleX

                signaturePlaceholderData.tablePos =
                    TablePos("p:$currentPageNumber;x:${floor(x)};y:${ceil(y)};w:${ceil(w)}")
                signaturePlaceholderData.placeholderName = placeholderName
                logger.debug("Found placeholder: {}", signaturePlaceholderData)
                placeholders.add(signaturePlaceholderData)
            }
            super.processOperator(operator, arguments)
        }
    }

    const val SIGNATURE_PLACEHOLDER_PREFIX = "PDF-AS_"
    /** The placeholderId stored in the PDSignature's signature dictionary (in the propBuild.app.name key) */
    var PDSignature.signaturePlaceholderId: String?
        get() = this.propBuild?.app?.name?.removePrefix(SIGNATURE_PLACEHOLDER_PREFIX) ?: this.location
        set(value) {
            val props = this.propBuild ?: PDPropBuild()
            val appProps = props.app ?: PDPropBuildDataDict()
            appProps.name = value?.let { SIGNATURE_PLACEHOLDER_PREFIX + it }
            props.setPDPropBuildApp(appProps)
            this.propBuild = props
        }
}
