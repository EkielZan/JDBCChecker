package com.ekielzan.JDBCChecker;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "com.ekielzan.JDBCChecker.messages.messages";
	private static Locale currentLocale = Locale.getDefault();
	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME,currentLocale);

	private Messages() {
	}

  public static void setLanguage(String lang){
		currentLocale= new Locale(lang);
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME,currentLocale);
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
