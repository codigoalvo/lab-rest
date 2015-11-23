package codigoalvo.util;

import java.math.BigInteger;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import codigoalvo.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JasonWebTokenUtil {

	private static final String SECRET = "Pr3ç15ÃoEmT3cn010Gi@DA1NF0RMAÇ@0"; // TODO: Alterar!
	private static SecureRandom random = new SecureRandom();

	private static String criarIdentificadorSessao() {
		return new BigInteger(130, random).toString(32);
	}

	private static SignatureAlgorithm obterAlgoritmo() {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		return signatureAlgorithm;
	}

	private static Key obterChaveAssinatura(SignatureAlgorithm signatureAlgorithm) {
		//We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(DatatypeConverter.printBase64Binary(SECRET.getBytes()));
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		return signingKey;
	}

	public static String criarJWT(Usuario usuario, int segundosExpiracao) {
		//The JWT signature algorithm we will be using to sign the token

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		SignatureAlgorithm algoritmo = obterAlgoritmo();

		  //Let's set the JWT Claims
		JwtBuilder token = Jwts.builder()
		                                .setIssuedAt(now)
		                                .setSubject(usuario.getLogin())
		                                .setIssuer("www.codigoalvo.com.br")
		                                .setId(criarIdentificadorSessao())
		                                .signWith(algoritmo, obterChaveAssinatura(algoritmo));

		if (segundosExpiracao > 0) {
			long expDataMilisegundos = nowMillis + (segundosExpiracao * 1000);
			token.setExpiration(new Date(expDataMilisegundos));
		}

		return token.compact();
	}

	public static Claims obterCorpoJWT(String token) {
		return Jwts.parser().setSigningKey(obterChaveAssinatura(obterAlgoritmo())).parseClaimsJws(token).getBody();
	}

	public static Jwt decodificarJWT(String token) {
		return Jwts.parser().setSigningKey(obterChaveAssinatura(obterAlgoritmo())).parse(token);
	}

}
