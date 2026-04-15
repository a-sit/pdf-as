package at.gv.egiz.pdfas.lib.impl.pdfbox3.configuration

import at.gv.egiz.pdfas.common.exceptions.PDFASError
import at.gv.egiz.pdfas.common.exceptions.PdfAsSettingsValidationException
import at.gv.egiz.pdfas.common.settings.ISettings
import at.gv.egiz.pdfas.common.settings.SignatureProfileSettings
import at.gv.egiz.pdfas.lib.api.ByteArrayDataSource
import at.gv.egiz.pdfas.lib.configuration.ConfigurationValidator
import at.gv.egiz.pdfas.lib.impl.pdfbox3.PDFBOXObject
import at.gv.egiz.pdfas.lib.impl.status.ICertificateProvider
import at.gv.egiz.pdfas.lib.impl.status.OperationStatus
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
        val profileIds: MutableSet<String?> = HashSet<String?>()

        for (key in settings.getFirstLevelKeys("sig_obj.types.")) {
            val profile = key.substring("sig_obj.types.".length)

            if (settings.getValue(key) == "on") {
                profileIds.add(profile)
            }
        }
        logger.debug("Validating {} Profiles.", profileIds.size)

        val profileSettings = ArrayList<SignatureProfileSettings?>()

        val opState = OperationStatus(settings, null, null, null)

        val dummyCert = X509Certificate()
        dummyCert.setSerialNumber(BigInteger("123"))
        val n = Name()
        n.addRDN(ObjectID.country, "AT")
        n.addRDN(ObjectID.locality, "Graz")
        n.addRDN(ObjectID.organization, "test")
        n.addRDN(ObjectID.organizationalUnit, "test")
        n.addRDN(ObjectID.commonName, "testca")
        dummyCert.setIssuerDN(n)
        dummyCert.setSubjectDN(n)

        val certProvider: ICertificateProvider = DummyCertificateProvider(dummyCert)

        val pdfBoxObject = PDFBOXObject(opState)
        val origDoc = PDDocument()
        origDoc.addPage(PDPage(PDRectangle.A4))
        val baos = ByteArrayOutputStream()
        try {
            origDoc.save(baos)
            baos.close()
            origDoc.close()

            pdfBoxObject.setOriginalDocument(ByteArrayDataSource(baos.toByteArray()))
        } catch (e1: IOException) {
            logger.info("Configuration validation failed!")
            throw PdfAsSettingsValidationException("Configuration validation failed!", e1)
        }


        for (id in profileIds) {
            try {
                val profileSetting = SignatureProfileSettings(id, settings)
                profileSettings.add(profileSetting)
                if (profileSetting.getValue("isvisible") != null) {
                    if (profileSetting.getValue("isvisible") == "false") {
                        continue
                    }
                }
            } catch (e: PDFASError) {
                logger.error("Find suspect signature-profile configuration. Ignore it", e)
            }
        }
    }

    override fun usedAsDefault(): Boolean {
        return true
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