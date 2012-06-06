import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: ${wa7chen}
 * Time: PM10:34
 */
public class LocalTest {
	public static void main(String[] args) {
		Locale[] locales = Locale.getAvailableLocales();
		for (Locale locale : locales) {
			System.out.println(locale.getDisplayCountry() + ":" + locale.getCountry());
			System.out.println(locale.getDisplayLanguage() + ":" + locale.getLanguage());
		}
	}
}
