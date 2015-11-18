package codigoalvo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MsgParamUtil {

	private static final Pattern PARAM_REGEX = Pattern.compile("\\#\\{(.+?)\\}");

	public static String buildParams(String... params) {
		if (params == null || params.length == 0)
			return "";
		StringBuilder paramsBuilder = new StringBuilder();
		for (String param : params) {
			paramsBuilder.append("#").append("{").append(param).append("}");
		}
		return paramsBuilder.toString();
	}

	public static String getMessageId(String message) {
		Matcher matcher = PARAM_REGEX.matcher(message);
		return matcher.replaceAll("");
	}

	public static String[] getParamArray(final String message) {
		return getParamValues(message).toArray(new String[0]);
	}

	public static List<String> getParamValues(final String message) {
		List<String> paramValues = new ArrayList<String>();
		Matcher matcher = PARAM_REGEX.matcher(message);
		while (matcher.find()) {
			paramValues.add(matcher.group(1));
		}
		return paramValues;
	}

}
