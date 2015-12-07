package codigoalvo.security;

import java.math.BigInteger;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.login.LoginException;
import javax.xml.bind.DatatypeConverter;

import codigoalvo.entity.Usuario;
import codigoalvo.util.Globals;
import codigoalvo.util.JsonUtil;
import codigoalvo.util.UsuarioUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JsonWebTokenUtil {

	private static SecureRandom random = new SecureRandom();

	@SuppressWarnings("unused")
	private static String criarIdentificadorSessao() {
		return new BigInteger(130, random).toString(32);
	}

	private static SignatureAlgorithm obterAlgoritmo() {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
		return signatureAlgorithm;
	}

	private static Key obterChaveAssinatura(SignatureAlgorithm signatureAlgorithm) {
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(DatatypeConverter.printBase64Binary(Globals.getSecret().getBytes()));
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		return signingKey;
	}

	public static String criarJWT(LoginToken usuario) {
		return criarJWT(usuario, System.currentTimeMillis(), Globals.getMinutosDuracaoToken());
	}

	public static String criarJWT(LoginToken usuario, long agora, int minutosExpiracao) {
		String usuarioJson = JsonUtil.toJson(usuario);
		SignatureAlgorithm algoritmo = obterAlgoritmo();
		Date dataAgora = new Date(agora);
		JwtBuilder token = Jwts.builder()
				.setIssuer(Globals.getIssuer())
				.setId(usuario.getId().toString())
				.setIssuedAt(dataAgora)
				.setSubject(usuario.getLogin())
				.claim("usuario", usuarioJson)
				.signWith(algoritmo, obterChaveAssinatura(algoritmo));

		if (minutosExpiracao > 0) {
			long expDataMilisegundos = agora + ((minutosExpiracao * 1000) * 60);
			token.setExpiration(new Date(expDataMilisegundos));
		}

		return token.compact();
	}

	protected static Claims obterCorpoJWT(String token) {
		return Jwts.parser().setSigningKey(obterChaveAssinatura(obterAlgoritmo())).parseClaimsJws(token).getBody();
	}

	public static boolean precisaRenovar(Claims corpoToken) {
		return precisaRenovar(corpoToken, Globals.getMinutosMinimosToken());
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

	public static String renovaTokenSeNecessario(String token) {
		Claims corpoJwt = JsonWebTokenUtil.obterCorpoJWT(token);
		if (JsonWebTokenUtil.precisaRenovar(corpoJwt)) {
			String tokenAtualizado = JsonWebTokenUtil.renovaToken(corpoJwt);
			return tokenAtualizado;
		}
		return token;
	}

	public static String renovaToken(Claims corpoToken) {
		return renovaToken(corpoToken, Globals.getMinutosDuracaoToken());
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

	public static void validarUsuario(Usuario usuario, String token) throws LoginException {
		if (!Globals.isAuhenticationEnabled()) {
			return;
		}
		try {
			Claims corpoJWT = JsonWebTokenUtil.obterCorpoJWT(token);
			String subject = corpoJWT.getSubject();
			Integer id = Integer.parseInt(corpoJWT.getId());
			String loginJson = ""+corpoJWT.get("usuario");
			LoginToken login = JsonUtil.fromJson(loginJson, LoginToken.class);
			if (subject == null || id == null  ||  login == null) {
				throw new LoginException("Dados invalidos no token! null");
			}
			if (!usuario.getLogin().equalsIgnoreCase(subject)  || !usuario.getLogin().equalsIgnoreCase(login.getLogin())) {
				throw new LoginException("Dados invalidos no token! login");
			}
			if (!usuario.getId().equals(id) || !usuario.getId().equals(login.getId())) {
				throw new LoginException("Dados invalidos no token! id");
			}
			if (usuario.getTipo() != UsuarioUtil.decodeTipoFromHash(login)) {
				throw new LoginException("Dados invalidos no token! tipo");
			}
		} catch (Throwable exc) {
			if (exc instanceof LoginException) {
				throw (LoginException)exc;
			} else {
				throw new LoginException(exc.getMessage());
			}
		}
	}

	public static LoginToken obterLoginToken(String token) {
		Claims corpoJwt = JsonWebTokenUtil.obterCorpoJWT(token);
		String usuarioJson = ""+corpoJwt.get("usuario");
		LoginToken loginToken = JsonUtil.fromJson(usuarioJson, LoginToken.class);
		return loginToken;
	}

	public static boolean isValidToken(String token) {
		Claims corpoJwt = obterCorpoJWT(token);
		String issuer = corpoJwt.getIssuer();
		if (!Globals.getIssuer().equals(issuer)) {
			return false;
		}
		return true;
	}

}
