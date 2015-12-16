package codigoalvo.util;

public class ErrosUtil {
	public static String getMensagemErro(Throwable exc) {
		while (exc.getCause() != null) {
			exc = exc.getCause();
		}
		String msg = exc.getMessage();
		return I18NUtil.getMessage(msg);
	}
}
