package codigoalvo.util;

import java.util.ArrayList;
import java.util.List;

import codigoalvo.entity.UsuarioTipo;
import codigoalvo.security.SegurancaUtil;
import codigoalvo.security.SegurancaUtilMd5;

public class UsuarioTipoUtil {
	private static final SegurancaUtil segurancaUtil = new SegurancaUtilMd5();
	private static final String TIPOS_USUARIO_JSON = buildTiposUsuarioJson();

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

	public static String getTiposUsuarioJson() {
		return TIPOS_USUARIO_JSON;
	}

	private static String buildTiposUsuarioJson() {
		List<String> tipos = new ArrayList<String>();
		for (UsuarioTipo usuarioTipo : UsuarioTipo.values()) {
			tipos.add(usuarioTipo.name());
		}
		return JsonUtil.toJson(tipos.toArray(new String[0]));
	}
}
