package codigoalvo.util;

public class Globals {

	private static Globals instance;
	private static PropertiesUtil P = new PropertiesUtil("Globals.properties");

	private Globals() {}

	public static Globals getInstance() {
		if (instance == null) {
			instance = new Globals();
		}
		return instance;
	}

	public static PropertiesUtil getPropertiesUtil() {
		return P;
	}

	public static String getProperty(String key) {
		return P.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		return P.getProperty(key, defaultValue);
	}

	public static int getInt(String key) {
		return P.getInt(key);
	}

	public static int getInt(String key, int defaultValue) {
		return P.getInt(key, defaultValue);
	}

	public static boolean getBoolean(String key) {
		return P.getBoolean(key);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return P.getBoolean(key, defaultValue);
	}

	public static String getSecret() {
		return P.getProperty("SECRET");
	}

	public static String getIssuer() {
		return P.getProperty("ISSUER");
	}

	public static boolean isAuhenticationEnabled() {
		return P.getBoolean("AUTHENTICATION_ENABLED", true);
	}

	public static int getMinutosDuracaoToken() {
		return P.getInt("MINUTOS_DURACAO_TOKEN");
	}

	public static int getMinutosMinimosToken() {
		return P.getInt("MINUTOS_MINIMOS_TOKEN");
	}

}
