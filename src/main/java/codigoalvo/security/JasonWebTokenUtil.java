package codigoalvo.security;

import java.math.BigInteger;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import codigoalvo.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JasonWebTokenUtil {

	private static final String SECRET = "Pr3ç15ÃoEmT3cn010Gi@DA1NF0RMAÇ@0"; // TODO: Alterar!
	public static final String ISSUER = "www.codigoalvo.com.br";
	private static SecureRandom random = new SecureRandom();

	private static String criarIdentificadorSessao() {
		return new BigInteger(130, random).toString(32);
	}

	private static SignatureAlgorithm obterAlgoritmo() {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
		return signatureAlgorithm;
	}

	private static Key obterChaveAssinatura(SignatureAlgorithm signatureAlgorithm) {
		//We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(DatatypeConverter.printBase64Binary(SECRET.getBytes()));
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		return signingKey;
	}

	public static String criarJWT(Usuario usuario, int minutosExpiracao) {
		//The JWT signature algorithm we will be using to sign the token

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		String usuarioJson = new Gson().toJson(usuario);
		SignatureAlgorithm algoritmo = obterAlgoritmo();

		  //Let's set the JWT Claims
		JwtBuilder token = Jwts.builder()
		                                .setIssuedAt(now)
		                                .setSubject(usuario.getLogin())
		                                .setIssuer(ISSUER)
		                                .setId(criarIdentificadorSessao())
		                                .claim("usuario", usuarioJson)
		                                .signWith(algoritmo, obterChaveAssinatura(algoritmo));

		if (minutosExpiracao > 0) {
			long expDataMilisegundos = nowMillis + ((minutosExpiracao * 1000) * 60);
			token.setExpiration(new Date(expDataMilisegundos));
		}

		return token.compact();
	}

	public static Claims obterCorpoJWT(String token) {
		return Jwts.parser().setSigningKey(obterChaveAssinatura(obterAlgoritmo())).parseClaimsJws(token).getBody();
	}

	public static Jwt decodificarJWT(String token) { //TODO: Tratar ExpiredJwtException
		return Jwts.parser().setSigningKey(obterChaveAssinatura(obterAlgoritmo())).parse(token);
	}

	public static boolean precisaRenovar(Claims corpoToken, int minutos) {
		Calendar cincoMinutos = GregorianCalendar.getInstance();
		cincoMinutos.add(Calendar.MINUTE, minutos);
		Date expiration = corpoToken.getExpiration();
		if (expiration.before(cincoMinutos.getTime())) {
			return true;
		}
		return false;
	}

	public static String renovaToken(Claims corpoToken, int minutosExpiracao) {
		SignatureAlgorithm algoritmo = obterAlgoritmo();
		JwtBuilder novoToken = Jwts.builder().setClaims(corpoToken).signWith(algoritmo, obterChaveAssinatura(algoritmo));
		Calendar expires = GregorianCalendar.getInstance();
		expires.add(Calendar.MINUTE, minutosExpiracao);
		novoToken.setExpiration(expires.getTime());
		novoToken.claim("refreshed", new Date());
		return novoToken.compact();
	}

}