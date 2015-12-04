package codigoalvo.util;

import codigoalvo.entity.Usuario;
import codigoalvo.entity.UsuarioTipo;
import codigoalvo.security.LoginToken;
import codigoalvo.security.SegurancaUtil;
import codigoalvo.security.SegurancaUtilMd5;

public class UsuarioUtil {
	private static final SegurancaUtil segurancaUtil = new SegurancaUtilMd5();

	public static LoginToken usuarioToToken(Usuario usuario) {
		LoginToken login = new LoginToken(usuario.getId(), usuario.getLogin(), usuario.getNome(), usuario.getEmail(), usuario.getTipo());
		login.setHash(encodeHash(login.toHashKey()));
		return login;
	}

	public static String encodeHash(String hashKey) {
		return segurancaUtil.criptografar(hashKey);
	}

	public static UsuarioTipo decodeTipoFromHash(LoginToken loginToken) {
		LoginToken tokenTmp = new LoginToken(loginToken);
		UsuarioTipo decoded = null;
		for (UsuarioTipo tipo : UsuarioTipo.values()) {
			tokenTmp.setTipo(tipo);
			if (encodeHash(tokenTmp.toHashKey()).equals(loginToken.getHash())) {
				return tipo;
			}
		}
		return decoded;
	}

}
