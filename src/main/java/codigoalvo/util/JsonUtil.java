package codigoalvo.util;

import com.google.gson.Gson;

public class JsonUtil {

	private static final Gson GSON = new Gson();

	public static String toJson(Object object) {
		return GSON.toJson(object);
	}

	public static <T> T fromJson(String json, Class<T> classe) {
		return GSON.fromJson(json, classe);
	}
}
