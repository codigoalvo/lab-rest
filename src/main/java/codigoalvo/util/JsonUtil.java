package codigoalvo.util;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {

	private static final Gson GSON = new Gson();

	public static String toJson(Object object) {
		return GSON.toJson(object);
	}

	public static <T> T fromJson(String json, Class<T> classe) {
		return GSON.fromJson(json, classe);
	}

	public static Map<String, String> fromJson(String json) {
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		Map<String, String> jsonMap = GSON.fromJson(json, type);
		return jsonMap;
	}
}
