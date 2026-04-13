package at.gv.egiz.pdfas.lib.impl.stamping.pdfbox3

import at.gv.egiz.pdfas.common.exceptions.PdfAsException
import at.gv.egiz.pdfas.common.settings.ISettings
import at.gv.egiz.pdfas.common.utils.ImageUtils
import at.gv.egiz.pdfas.common.utils.StringUtils
import at.gv.egiz.pdfas.lib.impl.pdfbox3.PDFBOXObject
import at.gv.egiz.pdfas.lib.impl.stamping.IPDFStamper
import at.gv.egiz.pdfas.lib.impl.stamping.IPDFVisualObject
import at.gv.egiz.pdfas.lib.impl.status.PDFObject
import at.knowcenter.wag.egov.egiz.pdf.PositioningInstruction
import at.knowcenter.wag.egov.egiz.table.Entry
import at.knowcenter.wag.egov.egiz.table.Style
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.UnsupportedEncodingException
import kotlin.math.floor
import kotlin.math.log
import kotlin.properties.Delegates
import at.knowcenter.wag.egov.egiz.table.Table as AbstractTable

object PDFBOXStamper: IPDFStamper<PDFBOXObject> {
    private val logger = LoggerFactory.getLogger(PDFBOXStamper::class.java)

    override fun createVisualPDFObject(pdf: PDFBOXObject, table: AbstractTable) =
        VisualObject(table, pdf)

    class VisualObject(abstractTable: AbstractTable, val pdf: PDFBOXObject): IPDFVisualObject {
        val settings get() = pdf.status.settings
        var table = Table(abstractTable, null, pdf)
        val abstractTable get() = table.abstractTable

        // this is necessary to mirror the strange behavior of setWidth/getWidth
        // setWidth needs to be followed by fixWidth to take effect
        // getWidth always returns the current effective width
        private var pendingWidth: Float? = null
        override fun getWidth() = table.width
        override fun setWidth(width: Float) { this.pendingWidth = width }
        override fun fixWidth() {
            val newWidth = pendingWidth ?: return
            try {
                table = Table(abstractTable, null, newWidth, pdf)
                pendingWidth = null
            } catch (e: Exception) {
                logger.warn("Failed to fix width of Table", e)
            }
        }

        override fun getHeight() = table.height

        var x by Delegates.notNull<Float>()
        override fun setXPos(x: Float) { this.x = x }

        var y by Delegates.notNull<Float>()
        override fun setYPos(y: Float) { this.y = y }

        private var _page: Int? = null
        override fun getPage() = _page!!
        override fun setPage(page: Int) { this._page = page }

    }

    class Table private constructor(val abstractTable: AbstractTable, parent: Table?, val pdf: PDFBOXObject, dummy: Unit) {
        constructor(abstractTable: AbstractTable, parent: Table?, pdf: PDFBOXObject) : this(abstractTable, parent, pdf, Unit) {
            colWidths = FloatArray(abstractTable.maxCols) { 0.0f }
            rowHeights = FloatArray(abstractTable.rows.size) { 0.0f }
            abstractTable.rows.forEachIndexed { i, row ->
                var j = 0
                while (j < row.size) {
                    val cell = row[j]
                    colWidths[j] = maxOf(colWidths[j], getCellWidth(cell))
                    rowHeights[i] = maxOf(rowHeights[i], getCellHeight(cell))
                    j += cell.colSpan
                }
            }
            width = colWidths.sum()
            height = rowHeights.sum()
            abstractTable.rows.forEachIndexed { i, row -> for (cell in row) {
                if (cell.type != Entry.TYPE_TABLE) continue
                (cell.value as Table).let {
                    if (rowHeights[i] != it.height) it.setHeight(rowHeights[i])
                }
            }}
        }
        constructor(abstractTable: AbstractTable, parent: Table?, fixSize: Float, pdf: PDFBOXObject) : this(abstractTable, parent, pdf, Unit) {
            val relativeSizes = abstractTable.colsRelativeWith
            if (relativeSizes != null) {
                val factor = fixSize / relativeSizes.sum()
                colWidths = FloatArray(relativeSizes.size) { relativeSizes[it] * factor }
            } else {
                val width = fixSize / abstractTable.maxCols
                colWidths = FloatArray(abstractTable.maxCols) { width }
            }

            rowHeights = FloatArray(abstractTable.rows.size) { 0.0f }

            abstractTable.rows.forEachIndexed { i, row ->
                var j = 0
                while (j < row.size) {
                    val cell = row[j]

                    val max = j + cell.colSpan
                    if (max > colWidths.size) {
                        throw IOException("Configuration error. Cannot determine column width! (Colspan ${cell.colSpan} at col $j is out of bounds for ${colWidths.size}.)")
                    }
                    val thisCellWidth = (j until max).asSequence().map(colWidths::get).sum()
                    rowHeights[i] = maxOf(rowHeights[i], getCellHeight(cell, thisCellWidth))
                    j = max
                }
            }

            width = colWidths.sum()
            height = rowHeights.sum()
            abstractTable.rows.forEachIndexed { i, row -> for (cell in row) {
                if (cell.type != Entry.TYPE_TABLE) continue
                (cell.value as Table).let {
                    if (rowHeights[i] != it.height) it.setHeight(rowHeights[i])
                }
            }}
        }
        val settings get() = pdf.status.settings
        val name get() = abstractTable.name
        val style: Style = when (parent) {
            null -> abstractTable.style
            else -> Style.doInherit(abstractTable.style, parent.style)
        } ?: throw IOException("Failed to determine Table style for ${abstractTable.name}")
        val font: Font
        val valueFont: Font
        private fun fontFor(type: Int) = when (type) {
            Entry.TYPE_CAPTION -> font
            Entry.TYPE_VALUE -> valueFont
            else -> throw IllegalArgumentException("type $type")
        }
        init {
            val valueFontString = style.valueFont
            if (parent != null && style == parent.style) {
                font = parent.font
                valueFont = parent.valueFont
            } else {
                font = Font(style.font ?: parent?.style?.font ?:
                    throw IOException("Failed to determine Table font style for $name"))
                valueFont = Font(style.valueFont ?: parent?.style?.valueFont ?:
                    throw IOException("Failed to determine Table value font style for $name"))
            }
        }
        val padding get() = style.padding
        val bgColor get() = style.bgColor

        init { /** normalizeContent */
            try {
                for (row in abstractTable.rows) for (cell in row) {
                    val font = when (cell.type) {
                        Entry.TYPE_CAPTION -> font
                        Entry.TYPE_VALUE -> valueFont
                        else -> continue
                    }.font
                    val value = cell.value as String
                    try {
                        font.getStringWidth(value)
                    } catch (e: Exception) {
                        when (e) {
                            is IOException, is IllegalArgumentException -> {
                                logger.warn("Font ${font.name} does not support every character in value '$value'")
                                cell.value = StringUtils.convertStringToPDFFormat(value)
                            }
                            else -> throw e
                        }
                    }
                }
            } catch (e: UnsupportedEncodingException) {
                throw PdfAsException("Unsupported Encoding", e)
            }
        }

        var width by Delegates.notNull<Float>()
        var height: Float = 0.0f; private set
        fun setHeight(newHeight: Float) {
            val delta = newHeight - height
            if (delta > 0) {
                rowHeights[rowHeights.lastIndex] += delta
                height = rowHeights.sum()
            } else {
                logger.warn("Table cannot be this small! (request to resize by $delta)")
            }
        }
        val rowCount get() = abstractTable.rows.size
        fun getRow(i: Int) = abstractTable.rows[i]
        val colCount get() = abstractTable.maxCols
        val colRelativeWidths: FloatArray? get() = abstractTable.colsRelativeWith
        lateinit var colWidths: FloatArray private set
        lateinit var rowHeights: FloatArray private set

        companion object {
            private const val NB_SPACE = '\u00A0'
            private const val SPACE = ' '
            private val DEFAULT_FONT by lazy { PDType1Font(Standard14Fonts.FontName.HELVETICA) }
            private const val DEFAULT_FONT_SIZE = 8.0f
        }

        inner class Font(fontString: String) {
            val font: PDFont
            val fontSize: Float
            init {
                val fontArr = fontString.split(',')
                when {
                    fontArr.size == 3 -> {
                        font = pdf.generateFont(fontArr[0], fontArr[2])
                        fontSize = fontArr[1].toFloat()
                    }
                    (fontArr.size == 2) && fontArr[0].startsWith("TTF:") -> {
                        font = pdf.generateFont(fontArr[0], null)
                        fontSize = fontArr[1].toFloat()
                    }
                    else -> {
                        logger.warn("Using default font because $fontString is not a valid font descriptor.")
                        font = DEFAULT_FONT
                        fontSize = DEFAULT_FONT_SIZE
                    }
                }
            }
        }

        private fun resolveTableCell(cell: Entry): Table = when (val v = cell.value) {
            is AbstractTable -> Table(v, this, pdf).also { cell.value = it }
            is Table -> v
            else -> throw IOException("Failed to build PDFBox Table")
        }
        private fun resolveTableCell(cell: Entry, fixedWidth: Float): Table = when (val v = cell.value) {
            is AbstractTable -> Table(v, this, fixedWidth, pdf).also { cell.value = it }
            is Table -> if (v.width == fixedWidth) v else Table(v.abstractTable, this, fixedWidth, pdf).also { cell.value = it }
            else -> throw IOException("Failed to build PDFBox Table")
        }
        private fun getCellWidth(cell: Entry): Float =
            when (cell.type) {
                Entry.TYPE_CAPTION, Entry.TYPE_VALUE -> {
                    val theFont = fontFor(cell.type)
                    if (cell.value == null) cell.value = ""
                    (cell.value as String).replace(NB_SPACE, SPACE).split("\n").maxOf {
                        theFont.font.getStringWidth(it) / 1000 * theFont.fontSize
                    }
                }
                Entry.TYPE_IMAGE -> style.imageScaleToFit?.width ?: 80.0f
                Entry.TYPE_TABLE -> resolveTableCell(cell).width
                else -> {
                    logger.warn("Invalid cell entry type ${cell.type} when calculating width")
                    0.0f
                }
            }
        private fun getCellHeight(cell: Entry): Float =
            when (cell.type) {
                Entry.TYPE_CAPTION, Entry.TYPE_VALUE -> {
                    val theFontSize = fontFor(cell.type).fontSize
                    (cell.value as String).splitToSequence('\n').count() * theFontSize + 2 * padding
                }
                Entry.TYPE_IMAGE -> {
                    2*padding + (style.imageScaleToFit?.height
                        ?: minOf(
                            ImageUtils.getImageDimensions(cell.value as String, settings).height,
                            80).toFloat())
                }
                Entry.TYPE_TABLE -> {
                    resolveTableCell(cell).height
                }
                else -> {
                    logger.warn("Invalid cell entry type ${cell.type} when calculating height")
                    0.0f
                }
            }

        private fun getCellHeight(cell: Entry, width: Float): Float =
            when (cell.type) {
                Entry.TYPE_CAPTION, Entry.TYPE_VALUE -> {
                    val theFont = fontFor(cell.type)
                    // string splitting dark magic
                    val lines = breakString(cell.value as String, theFont, width - 2*padding)
                    cell.value = lines.joinToString("\n")
                    2*padding + lines.size*theFont.fontSize
                }
                Entry.TYPE_IMAGE -> {
                    2*padding + (style.imageScaleToFit?.height
                        ?: ImageUtils.getImageDimensions(cell.value as String, settings).let {
                            floor(it.height * ((width - 2 * padding) / it.width))
                        })
                }
                Entry.TYPE_TABLE -> {
                    resolveTableCell(cell, width).height
                }
                else -> {
                    logger.warn("Invalid cell entry type ${cell.type} when calculating height")
                    0.0f
                }
            }

        private fun breakString(str: String, font: Font, boxWidth: Float) = buildList<String> {
            str.splitToSequence('\n').forEach { line ->
                /* null means there is nothing to put on this line (the line should not be added);
                   by contrast, empty string is "something" (will be added as a line) */
                var currentLine: String? = null
                line.splitToSequence(SPACE)
                    .map { it.replace(NB_SPACE, SPACE) }
                    .forEach { word ->
                        if (word.isEmpty()) {
                            // make sure the current line will get printed even if it is an empty line
                            if (currentLine == null) currentLine = ""
                            return@forEach
                        }
                        /* try to append the word to the current line... */
                        val maybeNewCurrentLine = when (currentLine) {
                            null, "" -> word
                            else -> "$currentLine $word"
                        }
                        /* ... and check if this would still fit in the box */
                        val newCurrentLineWidth = font.font.getStringWidth(maybeNewCurrentLine) / 1000.0f * font.fontSize
                        if (newCurrentLineWidth <= boxWidth) {
                            /* if it does, add the word to the current line and continue */
                            currentLine = maybeNewCurrentLine
                        } else {
                            /* if it does not, print the current line (if there is one) and start a new line */
                            /* note that if the word by itself is overly wide it will still be printed
                              (in a line by itself) on the next iteration */
                            currentLine?.let { add(it) }
                            currentLine = word
                        }
                    }
                currentLine?.let { add(it) }
            }
        }
    }
}