package at.gv.egiz.pdfas.lib.impl.pdfbox3.configuration

import at.gv.egiz.pdfas.common.exceptions.PdfAsSettingsValidationException
import at.gv.egiz.pdfas.common.settings.ISettings
import at.gv.egiz.pdfas.common.settings.SignatureProfileSettings
import at.gv.egiz.pdfas.lib.api.ByteArrayDataSource
import at.gv.egiz.pdfas.lib.api.PdfAsFactory
import at.gv.egiz.pdfas.lib.configuration.ConfigurationValidator
import at.gv.egiz.pdfas.lib.impl.configuration.ConfigurationImpl
import at.gv.egiz.pdfas.lib.impl.configuration.SignatureProfileConfiguration
import at.gv.egiz.pdfas.lib.impl.pdfbox3.PDFBOXObject
import at.gv.egiz.pdfas.lib.impl.stamping.TableFactory
import at.gv.egiz.pdfas.lib.impl.stamping.pdfbox3.PDFBOXStamper
import at.gv.egiz.pdfas.lib.impl.status.ICertificateProvider
import at.gv.egiz.pdfas.lib.impl.status.OperationStatus
import at.knowcenter.wag.egov.egiz.pdf.TablePos
import iaik.asn1.ObjectID
import iaik.asn1.structures.Name
import iaik.x509.X509Certificate
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.math.BigInteger

class ProfileValidator : ConfigurationValidator {

    companion object {
        private const val NAME = "PDFBOX_3_PROFILE_VALIDATOR"
        private val logger: Logger = LoggerFactory.getLogger(ProfileValidator::class.java)
    }

    @Throws(PdfAsSettingsValidationException::class)
    override fun validate(settings: ISettings) {
        val profileIds: MutableSet<String?> = HashSet()

        for (key in settings.getFirstLevelKeys("sig_obj.types.")) {
            val profile = key.substring("sig_obj.types.".length)

            if (settings.getValue(key) == "on") {
                profileIds.add(profile)
            }
        }
        logger.debug("Validating {} Profiles.", profileIds.size)

        val profileSettings = ArrayList<SignatureProfileSettings>()

        val dummyCert = X509Certificate()
        dummyCert.serialNumber = BigInteger("123")
        val n = Name()
        n.addRDN(ObjectID.country, "AT")
        n.addRDN(ObjectID.locality, "Graz")
        n.addRDN(ObjectID.organization, "test")
        n.addRDN(ObjectID.organizationalUnit, "test")
        n.addRDN(ObjectID.commonName, "testca")
        dummyCert.issuerDN = n
        dummyCert.subjectDN = n

        val certProvider: ICertificateProvider = DummyCertificateProvider(dummyCert)

        val origDoc = PDDocument()
        origDoc.addPage(PDPage(PDRectangle.A4))
        val baos = ByteArrayOutputStream()
        try {
            origDoc.save(baos)
            baos.close()
            origDoc.close()
        } catch (e1: IOException) {
            logger.info("Configuration validation failed!")
            throw PdfAsSettingsValidationException("Configuration validation failed!", e1)
        }


        for (id in profileIds) {
            try {
                val profileSetting = SignatureProfileSettings(id, settings)
                val profileConfiguration = SignatureProfileConfiguration(settings, id)
                profileSettings.add(profileSetting)
                if (!profileConfiguration.isVisualSignature) {
                    continue
                }
                val opState = OperationStatus(
                    PdfAsFactory.createSignParameter(ConfigurationImpl(settings))
                        .apply { signatureProfileId = id })
                val pdfBoxObject = PDFBOXObject(opState)
                pdfBoxObject.setOriginalDocument(ByteArrayDataSource(baos.toByteArray()))
                val t = TableFactory.createSigTable(profileSetting, "main", opState, certProvider)
                PDFBOXStamper.createVisualPDFObject(pdfBoxObject,t!!)
                profileConfiguration.defaultPositioning?.let(::TablePos)
                    ?.takeUnless(TablePos::isWauto)?.width
                    ?.let { defaultWidth -> PDFBOXStamper.Table(t, null, defaultWidth, pdfBoxObject) }
            } catch (e: Exception) {
                logger.error("Suspect signature-profile configuration found.", e)
                throw PdfAsSettingsValidationException("Configuration validation for profile {id} failed.", e)
            }
        }
    }

    override fun getName(): String {
        return NAME
    }

    private class DummyCertificateProvider(private val cert: X509Certificate?) : ICertificateProvider {
        override fun getCertificate(): X509Certificate? {
            return cert
        }
    }
}