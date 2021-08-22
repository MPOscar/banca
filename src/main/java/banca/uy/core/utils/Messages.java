package banca.uy.core.utils;

import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "messages";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key) {
		try {
			Optional<String> resourceString = Optional.ofNullable(System.getenv(key));
			return resourceString.orElse(RESOURCE_BUNDLE.getString(key));
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
