package codigoalvo.util;

public class ErrosUtil {
	public static String getMensagemErro(Exception exc) {
		while (exc.getCause() != null) {
			exc = (Exception) exc.getCause();
		}
		String msg = exc.getMessage();
		return I18NUtil.getMessage(msg);
	}
}
