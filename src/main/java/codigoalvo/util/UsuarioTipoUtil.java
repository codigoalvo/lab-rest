package codigoalvo.util;

import codigoalvo.entity.UsuarioTipo;
import codigoalvo.security.SegurancaUtil;
import codigoalvo.security.SegurancaUtilMd5;

public class UsuarioTipoUtil {
	private static final SegurancaUtil segurancaUtil = new SegurancaUtilMd5();

	public static String encodeTipo(UsuarioTipo tipo) {
		return segurancaUtil.criptografar(tipo.name());
	}

	public static UsuarioTipo decodeTipo(String encoded) {
		UsuarioTipo decoded = null;
		for (UsuarioTipo usuarioTipo : UsuarioTipo.values()) {
			if (encodeTipo(usuarioTipo).equals(encoded)) {
				return usuarioTipo;
			}
		}
		return decoded;
	}
}
