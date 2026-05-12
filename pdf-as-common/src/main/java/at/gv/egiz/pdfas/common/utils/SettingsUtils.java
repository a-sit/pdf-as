package at.gv.egiz.pdfas.common.utils;

import at.gv.egiz.pdfas.common.settings.ISettings;

public class SettingsUtils {
	public static boolean getBooleanValue(ISettings setting, String key, boolean defaultValue) {
		String theValue = setting.getValue(key);
		if(theValue != null) {
			if(theValue.equalsIgnoreCase("true")) {
				return true;
			} else if(theValue.equalsIgnoreCase("false")) {
				return false;
			} else {
				return defaultValue;
			}
		}
		return defaultValue;
	}
}
