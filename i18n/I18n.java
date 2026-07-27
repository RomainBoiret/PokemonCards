package i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class I18n {

    private static Locale locale = Locale.FRENCH;
    private static ResourceBundle bundle = load(locale);

    private I18n() {
    }

    public static void setLocale(Locale newLocale) {
        locale = newLocale;
        bundle = load(newLocale);
    }

    public static Locale getLocale() {
        return locale;
    }

    public static boolean isEnglish() {
        return "en".equals(locale.getLanguage());
    }

    public static String t(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }

    public static String t(String key, Object... args) {
        return MessageFormat.format(t(key), args);
    }

    private static ResourceBundle load(Locale loc) {
        return ResourceBundle.getBundle("i18n.Messages", loc);
    }
}
