package at.gv.egiz.pdfas.lib.impl.signing.pdfbox3

import at.gv.egiz.pdfas.common.exceptions.PDFASError
import at.gv.egiz.pdfas.common.exceptions.PdfAsException
import at.gv.egiz.pdfas.common.exceptions.PdfAsValidationException
import at.gv.egiz.pdfas.common.exceptions.PdfAsWrappedIOException
import at.gv.egiz.pdfas.common.exceptions.SLPdfAsException
import at.gv.egiz.pdfas.common.messages.MessageResolver
import at.gv.egiz.pdfas.common.settings.SignatureProfileSettings
import at.gv.egiz.pdfas.lib.api.ByteArrayDataSource
import at.gv.egiz.pdfas.lib.api.IConfigurationConstants
import at.gv.egiz.pdfas.lib.api.sign.SignParameter
import at.gv.egiz.pdfas.lib.impl.ErrorExtractor
import at.gv.egiz.pdfas.lib.impl.SignaturePositionImpl
import at.gv.egiz.pdfas.lib.impl.configuration.SignatureProfileConfiguration
import at.gv.egiz.pdfas.lib.impl.pdfbox3.PDFBOXObject
import at.gv.egiz.pdfas.lib.impl.pdfbox3.PDFBoxPlaceholderExtractor.signaturePlaceholderId
import at.gv.egiz.pdfas.lib.impl.pdfbox3.Positioning
import at.gv.egiz.pdfas.lib.impl.pdfbox3.asDereferencedSequence
import at.gv.egiz.pdfas.lib.impl.placeholder.PlaceholderFilter
import at.gv.egiz.pdfas.lib.impl.placeholder.SignaturePlaceholderData
import at.gv.egiz.pdfas.lib.impl.signing.IPdfSigner
import at.gv.egiz.pdfas.lib.impl.signing.PDFASSignatureExtractor
import at.gv.egiz.pdfas.lib.impl.signing.PDFASSignatureInterface
import at.gv.egiz.pdfas.lib.impl.stamping.IPDFVisualObject
import at.gv.egiz.pdfas.lib.impl.stamping.TableFactory
import at.gv.egiz.pdfas.lib.impl.stamping.ValueResolver
import at.gv.egiz.pdfas.lib.impl.stamping.pdfbox3.PDFAsVisualSignature
import at.gv.egiz.pdfas.lib.impl.stamping.pdfbox3.PDFBOXStamper
import at.gv.egiz.pdfas.lib.impl.status.OperationStatus
import at.gv.egiz.pdfas.lib.impl.status.RequestedSignature
import at.knowcenter.wag.egov.egiz.pdf.PositioningInstruction
import at.knowcenter.wag.egov.egiz.pdf.TablePos
import iaik.x509.X509Certificate
import org.apache.pdfbox.Loader
import org.apache.pdfbox.cos.COSArray
import org.apache.pdfbox.cos.COSDictionary
import org.apache.pdfbox.cos.COSInteger
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.cos.COSObject
import org.apache.pdfbox.cos.COSString
import org.apache.pdfbox.io.RandomAccessReadBuffer
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDResources
import org.apache.pdfbox.pdmodel.common.PDNumberTreeNode
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureElement
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField
import org.apache.pdfbox.preflight.PreflightDocument
import org.apache.pdfbox.preflight.exception.SyntaxValidationException
import org.apache.pdfbox.preflight.exception.ValidationException
import org.apache.pdfbox.preflight.parser.PreflightParser
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import org.apache.xmpbox.XMPMetadata
import org.apache.xmpbox.xml.DomXmpParser
import org.slf4j.LoggerFactory
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

private fun makeSignaturePositionImpl(i: PositioningInstruction, o: IPDFVisualObject) =
    SignaturePositionImpl().also { position ->
        position.x = i.x
        position.y = i.y
        position.page = i.page
        position.height = o.height
        position.width = o.width
    }

object PDFBOXSigner : IPdfSigner<PDFBOXObject, PDFBOXSigner.SignatureDataExtractor> {
    private val logger = LoggerFactory.getLogger(PDFBOXSigner::class.java)

    override fun rewritePlainSignature(plainSignature: ByteArray) =
        COSString(plainSignature).toHexString().toByteArray()

    override fun buildPDFObject(operationStatus: OperationStatus) =
        PDFBOXObject(operationStatus)

    @Throws(PdfAsValidationException::class)
    override fun checkPDFPermissions(pdfObject: PDFBOXObject) {

        pdfObject.document!!.checkPDFPermissions()
    }

    fun PDDocument.checkPDFPermissions() {
        if (isEncrypted || !currentAccessPermission.isOwnerPermission) {
            if (
                (currentAccessPermission.canModify() && currentAccessPermission.canModifyAnnotations()) ||
                currentAccessPermission.canFillInForm()) {

                logger.debug("Document is protected, but signing is still allowed")
            } else {
                throw PdfAsValidationException("error.pdf.sig.12", null)
            }
        }
    }

    class SignatureDataExtractor(
        val certificate: X509Certificate,
        private val pdfFilter: String,
        private val pdfSubFilter: String,
        private val signingDate: Calendar)
        : PDFASSignatureExtractor, PDFASSignatureInterface, SignatureInterface {

        override fun getPDFFilter() = pdfFilter
        override fun getPDFSubFilter() = pdfSubFilter
        override fun getSigningDate() = signingDate

        private lateinit var signatureData: ByteArray
        override fun getSignatureData() = signatureData
        lateinit var byteRange: IntArray private set

        /** Called by PDFBox.
         * We save the data to be signed and return an all-zeros signature (padded by pdfbox).
         * We splice the actual signature in at a later point.
         */
        override fun sign(content: InputStream): ByteArray {
            signatureData = content.readAllBytes()
            return byteArrayOf(0)
        }

        fun setPDSignature(signature: PDSignature) {
            byteRange = signature.byteRange
        }


    }
    override fun buildBlindSignaturInterface(
        certificate: X509Certificate,
        filter: String,
        subfilter: String,
        date: Calendar
    ) = SignatureDataExtractor(certificate, filter, subfilter, date)

    private fun findExistingSignature(doc: PDDocument, sigFieldName: String?): PDSignature? =
        doc.documentCatalog.acroForm
            ?.let { it.getField(sigFieldName) as? PDSignatureField }
            ?.let { field ->
                check (field.signature == null) { "The signature field $sigFieldName is already signed." }
                PDSignature().also { field.cosObject.setItem(COSName.V, it) }
            }

    private fun getSignatureFieldNameConfig(pdfObject: PDFBOXObject): String? =
        pdfObject.status.settings.getValue(IConfigurationConstants.SIGNATURE_FIELD_NAME)

    private fun getPDFAVersion(doc: PDDocument): String? = try {
        doc.documentCatalog.metadata
            ?.let { DomXmpParser().parse(it.exportXMPMetadata()) }
            ?.let(XMPMetadata::getPDFAIdentificationSchema)
            ?.let {
                val pdfaVersion = it.part
                val conformance = it.conformance
                logger.info("Detected PDF/A version: {} - {}", pdfaVersion, conformance)
                pdfaVersion.toString()
            }
    } catch (e: Throwable) {
        logger.warn("Failed to determine PDF/A version", e)
        null
    }

    private fun logPdfUpdateError(e: Throwable) {
        if (e is SLPdfAsException && !e.isCriticalError)
            logger.info("Could not save incremental update", e)
        else
            logger.error("Could not save incremental update", e)
    }

    private fun SignatureProfileSettings.calculateBlankAreaForSignature() : Int =
        runCatching {
            this.getValue(IConfigurationConstants.SIG_RESERVED_SIZE)?.toInt()
        }.getOrElse {
            logger.warn("Invalid configuration value for ${IConfigurationConstants.SIG_RESERVED_SIZE} (should be a number), using default")
            null
        } ?: 0x1000

    private fun prepareTablePosition(nextPlaceholderData: SignaturePlaceholderData?, signatureProfileConfiguration: SignatureProfileConfiguration, signParameterPosParam: String?): TablePos {
        nextPlaceholderData?.tablePos?.let { placeholderTablePos ->
            val minWidth = signatureProfileConfiguration.minWidth
            if ((minWidth > 0) && (placeholderTablePos.width < minWidth)) {
                placeholderTablePos.width = minWidth
                logger.debug("Correcting placeholder to minimum required width ({})", minWidth)
            }
            logger.debug("Placeholder position set to: {}", placeholderTablePos)
            return placeholderTablePos
        }

        val defaultProfilePos = signatureProfileConfiguration.defaultPositioning?.let {
            logger.debug("Using Signature positioning from profile: {}", it)
            TablePos(it)
        }

        logger.debug("Signature positioning from sign parameter: {}", signParameterPosParam)
        return when (signParameterPosParam) {
            null -> defaultProfilePos ?: TablePos()
            else -> TablePos(signParameterPosParam, defaultProfilePos)
        }
    }

    private fun buildNextSignatureFieldName(doc: PDDocument, pdfObject: PDFBOXObject): String {
        val baseName = getSignatureFieldNameConfig(pdfObject) ?: "PDF-AS Signatur"

        val existingSignatureNames =
            doc.document.trailer
                .getCOSDictionary(COSName.ROOT)
                .getCOSDictionary(COSName.ACRO_FORM)
                .getCOSArray(COSName.FIELDS)
                .asDereferencedSequence()
                .mapNotNull {
                    if (it !is COSDictionary) return@mapNotNull null
                    if (it.getNameAsString(COSName.FT) != "Sig") return@mapNotNull null
                    it.getString(COSName.T)?.takeIf { n -> n.startsWith(baseName) }
                }
                .toSet()

        var i = 1
        return generateSequence { "$baseName ${i++}" }
            .first { !existingSignatureNames.contains(it) }
    }

    private fun injectPdfUaContent(doc: PDDocument, signatureField: PDSignatureField,
                                   sigFieldName: String, signatureProfileSettings: SignatureProfileSettings) {
        try {
            logger.info("Adding PDF/UA content...")
            val structureTreeRoot = doc.documentCatalog.structureTreeRoot
            val docElement = (structureTreeRoot?.kids ?: run {
                logger.info("No kid elements in structure tree root, maybe not PDF/UA document. Skipping PDF/UA injection...")
                return@injectPdfUaContent
            }).firstNotNullOf { it as? PDStructureElement }

            val annotationObj = signatureField.widgets[0]
            val annotationPage = annotationObj.page
            val objectDic = COSDictionary().apply {
                setName(COSName.TYPE, "OBJR")
                setItem(COSName.PG, annotationPage)
                setItem(COSName.OBJ, annotationObj)
            }

            val sigBlock = PDStructureElement("Form", docElement).apply {
                kids = listOf(objectDic)
                page = annotationPage

                cosObject.apply {
                    setItem(COSName.A, COSDictionary().apply {
                        setName(COSName.O, "Layout")
                        setName("Placement", "Block")
                    })
                    isNeedToBeUpdated = true
                }
            }.also(docElement::appendKid)

            val ntn = structureTreeRoot.parentTree ?: run {
                logger.info("No number-tree-node found!")
                PDNumberTreeNode(objectDic, null)
            }

            val (ntnKids, ntnNumbers) = ntn.cosObject.run {
                Pair(getCOSArray(COSName.KIDS), getCOSArray(COSName.NUMS))
            }

            val parentTreeNextKey = structureTreeRoot.parentTreeNextKey.takeIf { it >= 0 } ?: run {
                structureTreeRoot.parentTree.upperLimit?.plus(1) ?: 0
            }
            val parentTreeNextKeyCOS = COSInteger.get(parentTreeNextKey.toLong())

            if ((ntnKids != null) && (ntnNumbers == null)) {
                PDNumberTreeNode(COSDictionary().apply {
                    setItem(COSName.NUMS, COSArray().apply {
                        add(parentTreeNextKeyCOS)
                        add(sigBlock)
                    })
                    setItem(COSName.LIMITS, COSArray().apply {
                        add(parentTreeNextKeyCOS)
                        add(parentTreeNextKeyCOS)
                    })
                }, PDNumberTreeNode::class.java).let {
                    ntnKids.add(it)
                    ntnKids.isNeedToBeUpdated = true
                }
            } else if ((ntnNumbers != null) && (ntnKids == null)) {
                ntnNumbers.add(parentTreeNextKeyCOS)
                ntnNumbers.add(sigBlock.cosObject)
                ntnNumbers.isNeedToBeUpdated = true
                structureTreeRoot.parentTree = ntn
            } else {
                logger.error(
                    "Document is not PDF/UA conformant before signature creation (ntnKids = {}, ntnNumbers = {})",
                    ntnKids, ntnNumbers)
                throw PdfAsException("error.pdf.sig.pdfua.1")
            }

            annotationObj.structParent = parentTreeNextKey
            structureTreeRoot.parentTreeNextKey = parentTreeNextKey + 1
            annotationPage.cosObject.let {
                it.setName("Tabs", "S")
                it.isNeedToBeUpdated = true
            }

            if (signatureField.alternateFieldName.isEmpty())
                signatureField.alternateFieldName = sigFieldName

            ntn.cosObject.isNeedToBeUpdated = true
            sigBlock.cosObject.isNeedToBeUpdated = true
            structureTreeRoot.cosObject.isNeedToBeUpdated = true
            objectDic.isNeedToBeUpdated = true
            docElement.cosObject.isNeedToBeUpdated = true

        } catch (e: Throwable) {
            if (signatureProfileSettings.isPDFUA) {
                logger.error("Could not create PDF/UA conformant document!", e)
                throw PdfAsException("error.pdf.sig.pdfua.1", e)
            } else {
                if (logger.isDebugEnabled) {
                    logger.debug("Could not create PDF/UA conformant signature. Reason: {}", e.message, e)
                } else {
                    logger.info("Could not create PDF/UA conformant signature. Reason: {}", e.message)
                }
            }
        }
    }

    private fun validatePDFAPreflight(signedDocument: ByteArray) {
        RandomAccessReadBuffer(signedDocument).use { buf ->
            try {
                val result =
                    (PreflightParser(buf).parse() as PreflightDocument).use(PreflightDocument::validate)

                logger.info("PDF-A Validation Result: {}", result.isValid)
                result.errorsList.takeIf { it.isNotEmpty() }?.let { errors ->
                    logger.error("The following validation errors occurred for PDF-A validation:")
                    errors.forEach {
                        logger.error("\t{}: {}", it.errorCode, it.details)
                    }
                }
                if (!result.isValid) {
                    logger.info("The file is not a valid PDF-A document")
                }
            } catch (e: SyntaxValidationException) {
                logger.error("The file is syntactically invalid", e)
                throw PdfAsException("Resulting PDF document is syntactically invalid.")
            } catch (e: ValidationException) {
                logger.error("The file is not a valid PDF-A document.", e)
            } catch (e: IOException) {
                logger.error("IOException (${e.message}) occurred while validating PDF-A conformance", e)
                throw PdfAsException("Failed validating PDF Document.", e)
            } catch (e: RuntimeException) {
                logger.error("RuntimeException occurred while validating PDF-A conformance", e)
                throw PdfAsException("Failed validating PDF Document.", e)
            }
        }
    }

    override fun signPDF(
        pdfObject: PDFBOXObject,
        requestedSignature: RequestedSignature,
        signer: SignatureDataExtractor
    ) {

        val isAdobeSignatureForm: Boolean
        try { SignatureOptions().use { options -> pdfObject.document!!.use { doc ->
            val signature = findExistingSignature(doc, getSignatureFieldNameConfig(pdfObject))
                .also { isAdobeSignatureForm = (it != null) }
                ?: PDSignature()
            signature.setFilter(COSName.getPDFName(signer.pdfFilter))
            signature.setSubFilter(COSName.getPDFName(signer.pdfSubFilter))
            signature.signDate = Calendar.getInstance()
            logger.debug("Signing at {}", signature.signDate.time)

            val nextPlaceholderData: SignaturePlaceholderData? = PlaceholderFilter.checkPlaceholderSignatureLocation(
                pdfObject.status, pdfObject.status.settings,
                pdfObject.status.signParameter.placeHolderId)
            if (nextPlaceholderData != null) {
                logger.info("Placeholder data found.")
                signature.signaturePlaceholderId = nextPlaceholderData.placeholderName
                nextPlaceholderData.profile?.let { profile ->
                    if (pdfObject.status.settings.isValue(IConfigurationConstants.PLACEHOLDER_PROFILE_OVERWRITE, true)) {
                        logger.debug("Placeholder profile override applied. Using profile {}...", profile)
                        requestedSignature.signatureProfileID = profile
                    } else {
                        logger.debug("Placeholder profile override is disabled. Using profile from request...")
                    }
                }
            }

            val signatureProfileSettings =
                TableFactory.createProfile(requestedSignature.signatureProfileID, pdfObject.status.settings)
            val resolver = ValueResolver(requestedSignature, pdfObject.status)

            signature.name = resolver.resolve("SIG_SUBJECT",
                signatureProfileSettings.getValue("SIG_SUBJECT"), signatureProfileSettings)
            signature.reason =
                (signatureProfileSettings.signingReason ?: "PAdES Signature").also { logger.debug("Signing reason: $it") }

            signer.setPDSignature(signature)

            if (signatureProfileSettings.isPDFA() || signatureProfileSettings.isPDFA3) {
                signatureProfileSettings.setPDFAVersion(getPDFAVersion(doc))
            }

            options.preferredSignatureSize = signatureProfileSettings.calculateBlankAreaForSignature()
                .also { logger.debug("Reserving {} bytes for signature", it) }

            var alternateCaption: String? = null
            if (requestedSignature.isVisual) {
                logger.info("Creating visual signature block")

                val signatureProfileConfiguration =
                    pdfObject.status.getSignatureProfileConfiguration(requestedSignature.signatureProfileID)
                val tablePos =
                    prepareTablePosition(nextPlaceholderData, signatureProfileConfiguration,
                        pdfObject.status.signParameter.signaturePosition)
                val main = TableFactory.createSigTable(
                    signatureProfileSettings, IConfigurationConstants.MAIN,
                    pdfObject.status, requestedSignature)

                val visualObject = PDFBOXStamper.createVisualPDFObject(pdfObject, main)

                val positioningInstruction: PositioningInstruction = Positioning.determineTablePositioning(
                    tablePos, doc, visualObject, pdfObject.status.settings, signatureProfileSettings
                )
                logger.debug("Positioning: {}", positioningInstruction)

                if (!isAdobeSignatureForm) {
                    if (positioningInstruction.isMakeNewPage) {
                        val last = doc.numberOfPages - 1
                        val root = doc.documentCatalog
                        val lastPage = root.pages[last]
                        root.pages.cosObject.isNeedToBeUpdated = true

                        doc.addPage(
                            PDPage(lastPage.mediaBox).apply {
                                setResources(PDResources())
                                rotation = lastPage.rotation
                            })
                    }

                    // TODO: this is a no-op, right?
                    val targetPage = doc.pages.get(positioningInstruction.page - 1)
                    val rot = targetPage.rotation
                    logger.debug("Page rotation: $rot")
                    logger.debug("Resulting sign rotation: ${positioningInstruction.rotation}")

                    requestedSignature.signaturePosition =
                        makeSignaturePositionImpl(positioningInstruction, visualObject)
                }

                if (signatureProfileSettings.isPDFA() || signatureProfileSettings.isPDFA3) {
                    val root = doc.documentCatalog

                    PDFBOXSigner.javaClass.getResourceAsStream("/icm/sRGB Color Space Profile.icm").use { colorProfile ->
                        root.outputIntents = listOf(PDOutputIntent(doc, colorProfile).apply {
                            info = "sRGB IEC61966-2.1"
                            outputCondition = "sRGB IEC61966-2.1"
                            outputConditionIdentifier = "sRGB IEC61966-2.1"
                            registryName = "http://www.color.org"
                        })
                        root.cosObject.isNeedToBeUpdated = true
                    }

                }

                options.page = positioningInstruction.page - 1

                PDFAsVisualSignature.build(
                    pdfObject, visualObject, positioningInstruction, signatureProfileSettings, false
                ).let { (signatureBlock, caption) ->
                    options.setVisualSignature(ByteArrayInputStream(signatureBlock))
                    alternateCaption = caption
                }
            }

            doc.addSignature(signature, signer, options)

            val sigFieldName = buildNextSignatureFieldName(doc, pdfObject)

            if (!isAdobeSignatureForm) {
                val signatureField =
                    doc.documentCatalog.acroForm?.fields?.asSequence()
                        ?.filterIsInstance<PDSignatureField>()
                        ?.firstOrNull { it.signature?.cosObject == signature.cosObject }
                if (signatureField != null) {
                    signatureField.partialName = sigFieldName
                    signatureField.alternateFieldName = alternateCaption ?: sigFieldName
                } else {
                    logger.warn("Failed to name Signature Field! [Cannot find AcroForm field list]")
                }
            }

            val signatureField = doc.documentCatalog.acroForm?.getField(sigFieldName) as PDSignatureField
            injectPdfUaContent(doc, signatureField, sigFieldName, signatureProfileSettings)
            try {
                synchronized(doc) {
                    pdfObject.signedDocument = ByteArrayOutputStream().also(doc::saveIncremental).toByteArray()
                    if (signatureProfileSettings.isPDFA) {
                        validatePDFAPreflight(pdfObject.signedDocument)
                    }
                }
            } catch (e: PdfAsWrappedIOException) {
                throw e.decoratedException.also(::logPdfUpdateError)
            } catch (e: Throwable) {
                throw PdfAsException("error.pdf.sig.06", e.also(::logPdfUpdateError))
            }
            logger.debug("Signature done!")
        }}} catch (e: IOException) {
            logger.warn(MessageResolver.resolveMessage("error.pdf.sig.01"), e)
            throw PdfAsException("error.pdf.sig.01", e)
        } catch (e: PDFASError) {
            logger.warn(e.info)
            throw PdfAsException("error.pdf.sig.01", e)
        } finally {
            System.gc()
        }
    }

    override fun generateVisibleSignaturePreview(
        parameter: SignParameter,
        cert: java.security.cert.X509Certificate,
        resolution: Int,
        status: OperationStatus,
        requestedSignature: RequestedSignature
    ): Image {
        try {
            val pdfObject = status.pdfObject as PDFBOXObject

            val signatureProfileSettings: SignatureProfileSettings
            val visualObject: PDFBOXStamper.VisualObject
            val positioningInstruction: PositioningInstruction
            PDDocument().use { previewDoc ->
                previewDoc.addPage(PDPage(PDRectangle.A4))
                pdfObject.originalDocument = ByteArrayDataSource(
                    ByteArrayOutputStream().use {
                        previewDoc.save(it)
                        it.toByteArray()
                    })

                signatureProfileSettings = TableFactory.createProfile(
                    requestedSignature.signatureProfileID,
                    pdfObject.status.settings
                )

                visualObject =
                    PDFBOXStamper.createVisualPDFObject(
                            pdfObject,
                            TableFactory.createSigTable(
                                signatureProfileSettings, IConfigurationConstants.MAIN,
                                pdfObject.status, requestedSignature
                            )
                        )

                positioningInstruction = when (
                    val signaturePosString = pdfObject.status
                        .getSignatureProfileConfiguration(requestedSignature.signatureProfileID)
                        .defaultPositioning
                ) {
                    null -> TablePos()
                    else -> TablePos(signaturePosString)
                }.let {
                    Positioning.determineTablePositioning(
                        it,
                        previewDoc,
                        visualObject,
                        pdfObject.status.settings,
                        signatureProfileSettings
                    )
                }
            }

            val stdRes = 72.0
            val targetRes = resolution.toFloat()
            val factor = targetRes / stdRes

            requestedSignature.signaturePosition = makeSignaturePositionImpl(positioningInstruction, visualObject)
            val pageImage =
                PDFAsVisualSignature.build(
                    pdfObject,
                    visualObject,
                    positioningInstruction,
                    signatureProfileSettings,
                    true
                )
                    .let { (bytes, _) ->
                        synchronized(PDDocument::javaClass) { Loader.loadPDF(bytes) }
                    }
                    .use { visualDoc ->
                        PDFRenderer(visualDoc)
                            .renderImageWithDPI(0, targetRes, ImageType.ARGB)
                    }


            return BufferedImage(
                (requestedSignature.signaturePosition.width * factor).toInt(),
                (requestedSignature.signaturePosition.height * factor).toInt(),
                BufferedImage.TYPE_4BYTE_ABGR
            ).also { cutOut ->
                // TODO: these coordinates feel scuffed
                (cutOut.graphics as Graphics2D).drawImage(
                    pageImage, 0, 0, cutOut.width, cutOut.height,
                    (0 * factor).toInt(),
                    (pageImage.height - (requestedSignature.signaturePosition.height + 1) * factor).toInt(),
                    ((requestedSignature.signaturePosition.width + 2) * factor).toInt(),
                    (pageImage.height).toInt(),
                    null
                )
            }
        } catch (e: Throwable) {
            logger.warn("Failed to generate signature preview", e)
            throw ErrorExtractor.searchPdfAsError(e, status)
        }
    }
}