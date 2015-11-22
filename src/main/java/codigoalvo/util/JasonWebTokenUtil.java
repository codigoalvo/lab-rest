package codigoalvo.util;

import java.security.Key;

import codigoalvo.entity.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class JasonWebTokenUtil {

	private static final String SECRET = "Pr3ç15ÃoEmT3cn010Gi@DA1NF0RMAÇ@0"; // TODO: Alterar!

	public static String criarJWT(Usuario usuario, int expiration) {
		Key key = MacProvider.generateKey();
		String token = Jwts.builder().setSubject(SECRET).signWith(SignatureAlgorithm.HS512, key).compact();
		return token;
	}
}
