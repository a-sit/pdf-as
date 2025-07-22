package at.gv.egiz.pdfas.lib.impl.stamping.pdfbox2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PDFAsFontCache {

	private static final Logger logger = LoggerFactory
			.getLogger(PDFAsFontCache.class);

	private static final String HELVETICA = "HELVETICA";
	private static final String COURIER = "COURIER";
	private static final String TIMES_ROMAN = "TIMES_ROMAN";
	private static final String BOLD = "BOLD";
	private static final String NORMAL = "NORMAL";
	private static final String ITALIC = "ITALIC";
	private static final String SEP = ":";

	public static PDFont defaultFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
	public static float defaultFontSize = 8;

	private Map<String, PDFont> fonts;
	
	private static Map<String, PDFont> defaultFonts = new HashMap<String, PDFont>();
	static {
		defaultFonts.put(HELVETICA + SEP + NORMAL, new PDType1Font(Standard14Fonts.FontName.HELVETICA));
		defaultFonts.put(HELVETICA + SEP + BOLD, new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD));

		defaultFonts.put(COURIER + SEP + NORMAL, new PDType1Font(Standard14Fonts.FontName.COURIER));
		defaultFonts.put(COURIER + SEP + BOLD, new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD));

		defaultFonts.put(TIMES_ROMAN + SEP + NORMAL, new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN));
		defaultFonts.put(TIMES_ROMAN + SEP + BOLD, new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD));
		defaultFonts.put(TIMES_ROMAN + SEP + ITALIC, new PDType1Font(Standard14Fonts.FontName.TIMES_ITALIC));
	}

	public PDFAsFontCache(){
		fonts = new HashMap<String, PDFont>(defaultFonts);
	}
	
	public boolean contains(String fontPath) {
		return fonts.containsKey(fontPath);
	}

	public void addFont(String fontPath, PDFont font) {
		fonts.put(fontPath, font);	
	}

	public PDFont getFont(String fontPath) {
		return fonts.get(fontPath);
	}
	
	public void showAvailableFonts() {
		Iterator<String> it = fonts.keySet().iterator();
		logger.info("Available Fonts:");
		while (it.hasNext()) {
			logger.info(it.next());
		}
	}
}
