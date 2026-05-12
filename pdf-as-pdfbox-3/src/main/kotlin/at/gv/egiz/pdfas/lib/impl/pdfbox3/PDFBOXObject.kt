package at.gv.egiz.pdfas.lib.impl.pdfbox3

import at.gv.egiz.pdfas.lib.impl.status.OperationStatus
import at.gv.egiz.pdfas.lib.impl.status.PDFObject
import at.knowcenter.wag.egov.egiz.table.Style
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Path
import jakarta.activation.DataSource

class PDFBOXObject(status: OperationStatus) : PDFObject(status) {
    companion object { private val logger = LoggerFactory.getLogger(PDFBOXObject::class.java) }
    public val settings get() = status.settings
    public var document: PDDocument? = null; private set

    override fun close() {
        try {
            document?.close()
        } finally {
            document = null
        }
    }

    override fun setOriginalDocument(originalDocument: DataSource) {
        this.originalDocument = originalDocument
        close()
        synchronized(PDDocument::class.java) {
            // TODO: can we somehow make this leverage random access
            document = Loader.loadPDF(originalDocument.inputStream.readAllBytes())
        }
    }

    override fun getPDFVersion() =
        document!!.document.version.toString()

    fun generateFont(fontType: String, fontDerivative: String?) = when {
        fontType.startsWith("TTF:") -> generateTTFFont(fontType)
        else -> getCachedFont(fontType, fontDerivative)
    }

    private val ttfFontCache: MutableMap<Path, PDFont> = mutableMapOf()
    private fun generateTTFFont(fontType: String): PDFont {
        require(fontType.startsWith("TTF:"))
        val fontPath = Path.of(settings.workingDirectory, "fonts", fontType.substring(4))
        logger.debug("Font from: \"{}\".", fontPath)
        return ttfFontCache.computeIfAbsent(fontPath) {
            PDType0Font.load(document, it.toFile())
        }
    }

    private fun getCachedFont(fontType: String, fontDerivative: String?): PDFont {
        val fontDescriptor = "$fontType:${fontDerivative ?: "NORMAL"}"
        return PDType1Font(DEFAULT_FONT_DESCRIPTORS[fontDescriptor] ?: run {
            logger.error("Invalid font descriptor: \"$fontDescriptor\"")
            logger.warn("Available fonts:")
            DEFAULT_FONT_DESCRIPTORS.forEach { (descriptor, _) -> logger.warn(" - $descriptor") }
            throw IOException("Invalid font descriptor: \"$fontDescriptor\"")
        })
    }
}

private val DEFAULT_FONT_DESCRIPTORS = mapOf(
    "${Style.HELVETICA}:${Style.NORMAL}" to Standard14Fonts.FontName.HELVETICA,
    "${Style.HELVETICA}:${Style.BOLD}" to Standard14Fonts.FontName.HELVETICA_BOLD,
    "${Style.HELVETICA}:${Style.ITALIC}" to Standard14Fonts.FontName.HELVETICA_OBLIQUE,
    "${Style.HELVETICA}:${Style.BOLDITALIC}" to Standard14Fonts.FontName.HELVETICA_BOLD_OBLIQUE,
    "${Style.COURIER}:${Style.NORMAL}" to Standard14Fonts.FontName.COURIER,
    "${Style.COURIER}:${Style.BOLD}" to Standard14Fonts.FontName.COURIER_BOLD,
    "${Style.COURIER}:${Style.ITALIC}" to Standard14Fonts.FontName.COURIER_OBLIQUE,
    "${Style.COURIER}:${Style.BOLDITALIC}" to Standard14Fonts.FontName.COURIER_BOLD_OBLIQUE,
    "${Style.TIMES_ROMAN}:${Style.NORMAL}" to Standard14Fonts.FontName.TIMES_ROMAN,
    "${Style.TIMES_ROMAN}:${Style.BOLD}" to Standard14Fonts.FontName.TIMES_BOLD,
    "${Style.TIMES_ROMAN}:${Style.ITALIC}" to Standard14Fonts.FontName.TIMES_ITALIC,
    "${Style.TIMES_ROMAN}:${Style.BOLDITALIC}" to Standard14Fonts.FontName.TIMES_BOLD_ITALIC,
)
