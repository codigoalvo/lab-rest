package codigoalvo.util;

public class ErrosUtil {
	public static String getMensagemErro(Throwable exc) {
		while (exc.getCause() != null) {
			exc = exc.getCause();
		}
		String msg = exc.getMessage();
		msg = trataErros(msg);
		return I18NUtil.getMessage(msg);
	}

	public static String trataErros(String excMsg) {
		if (excMsg ==  null) {
			return "";
		}
		if (excMsg.toUpperCase().contains("KEY VIOLATION")  ||  excMsg.toUpperCase().contains("DUPLICATE KEY")) {
			excMsg = "gravar.erroJaExiste";
		}
		return excMsg;
	}
}
