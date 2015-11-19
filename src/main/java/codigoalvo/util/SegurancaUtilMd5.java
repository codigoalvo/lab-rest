package codigoalvo.util;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class SegurancaUtilMd5 implements SegurancaUtil {

	public SegurancaUtilMd5() {
		Logger.getLogger(SegurancaUtilMd5.class).debug("####################  construct  ####################");
	}

	@Override
	public String criptografar(String conteudo) {
		if (conteudo == null) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(conteudo.trim().toUpperCase().getBytes("UTF-8"));
			return new String(Base64.encodeBase64(bytes));
		} catch (Exception exc) {
			exc.printStackTrace();
			throw new RuntimeException("Erro ao criptografar conteúdo. " + exc.getMessage());
		}
	}

	@Override
	public boolean criptografado(String conteudo) {
		return conteudo != null && conteudo.endsWith("==");
	}

}
