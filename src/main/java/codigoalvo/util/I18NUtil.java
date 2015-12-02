package codigoalvo.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class I18NUtil {

	public static String getMessage(String key) {
		return getMessage(key, "pt", "BR");
	}

	public static String getMessage(String key, String language, String coutry) {
		try {
			Locale currentLocale = new Locale(language, coutry);
			ResourceBundle messages = ResourceBundle.getBundle("codigoalvo.msg.MessagesBundle", currentLocale);
			String message = key;
			if (key.contains("#")) {
				String[] items = key.split("#");
				message = messages.getString(items[0]);
				for (int i = 1; i < items.length; i++) {
					String item = items[i].replace("{", "").replace("}", "");
					message = message.replace("{"+(i-1)+"}", item);
				}
			} else {
				message = messages.getString(key);
			}
			return message;
		} catch (Throwable exc){
			Logger.getLogger(I18NUtil.class).error(exc);
			return key;
		}
	}
}
