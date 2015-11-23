package codigoalvo.util;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import codigoalvo.entity.Usuario;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JasonWebTokenUtil {

	private static final String SECRET = "Pr3ç15ÃoEmT3cn010Gi@DA1NF0RMAÇ@0"; // TODO: Alterar!

	public static String criarJWT(Usuario usuario, int segundosExpiracao) {
		//The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		//We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(DatatypeConverter.printBase64Binary(SECRET.getBytes()));
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		  //Let's set the JWT Claims
		JwtBuilder token = Jwts.builder().setId(Integer.toString(usuario.getId()))
		                                .setIssuedAt(now)
		                                .setSubject(usuario.getNome())
		                                .setIssuer("codigoalvo")
		                                .signWith(signatureAlgorithm, signingKey);

		if (segundosExpiracao > 0) {
			long expDataMilisegundos = nowMillis + (segundosExpiracao * 1000);
			token.setExpiration(new Date(expDataMilisegundos));
		}

		return token.compact();
	}
}
