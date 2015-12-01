package codigoalvo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import codigoalvo.entity.UsuarioTipo;
import codigoalvo.util.DateUtil;
import codigoalvo.util.JsonUtil;
import codigoalvo.util.UsuarioTipoUtil;

public class JasonWebTokenUtilTest {

	private static final LoginToken LOGIN_TOKEN = new LoginToken(1, "admin", "Administrador", "admin@email.com", UsuarioTipo.ADMIN, UsuarioTipoUtil.encodeTipo(UsuarioTipo.ADMIN, 1));
	private static final long AGORA = System.currentTimeMillis();
	private static String token;

	@BeforeClass
	public static void prepare() {
		token = JasonWebTokenUtil.criarJWT(LOGIN_TOKEN , AGORA, 4);
		System.out.println("[JasonWebTokenUtilTest prepare] agora (Date): "+new Date(AGORA));
		System.out.println("[JasonWebTokenUtilTest prepare] agora: "+AGORA);
		System.out.println("[JasonWebTokenUtilTest prepare] loginToken: "+LOGIN_TOKEN);
		System.out.println("[JasonWebTokenUtilTest prepare] token: "+token);
	}

	@Test
	public void testDecodificarJWT() {
		@SuppressWarnings("rawtypes")
		Jwt jwtToken = JasonWebTokenUtil.decodificarJWT(token);
		String decodedToken = jwtToken.toString();
		System.out.println("[testDecodificarJWT] decodedToken: "+decodedToken);
		assertTrue("Issuer deveria ser www.codigoalvo.com.br", decodedToken.contains("iss=www.codigoalvo.com.br"));
	}

	@Test
	public void testObterUsuarioJWT() {
		Claims corpoJWT = JasonWebTokenUtil.obterCorpoJWT(token);
		String usuarioJson = corpoJWT.get("usuario").toString();
		LoginToken usuario = JsonUtil.fromJson(usuarioJson, LoginToken.class);
		UsuarioTipo usuarioTipo = UsuarioTipoUtil.decodeTipo(usuario.getExtp(), usuario.getId());
		System.out.println("[testObterUsuarioJWT] usuarioJson: "+usuarioJson);
		System.out.println("[testObterUsuarioJWT] usuario: "+usuario);
		System.out.println("[testObterUsuarioJWT] usuario.tipo: "+usuarioTipo);
		assertTrue("Login do usuario deveria ser admin", "admin".equals(usuario.getLogin()));
		assertTrue("Tipo do usuario deveria ser ADMIN", UsuarioTipo.ADMIN == usuarioTipo  &&  usuario.getTipo() == usuarioTipo);
	}

	@Test
	public void testObterCorpoJWT() {
		Claims corpoJWT = JasonWebTokenUtil.obterCorpoJWT(token);
		String issuer = corpoJWT.getIssuer();
		System.out.println("[testObterCorpoJWT] issuer: "+issuer);
		assertTrue("Issuer deveria ser www.codigoalvo.com.br", issuer.equals("www.codigoalvo.com.br"));
		// Data da emissão difere por alguns milisegundos. Não deveria!
		Date issuedAt = DateUtil.zeraSegundosMilisegundosData(corpoJWT.getIssuedAt());
		Date dataAgora = DateUtil.zeraSegundosMilisegundosData(new Date(AGORA));
		System.out.println("[testObterCorpoJWT] issuedAt (Date): "+issuedAt);
		System.out.println("[testObterCorpoJWT] issuedAt: "+issuedAt.getTime());
		assertTrue("Data de emissão do token incorreta! ("+issuedAt.getTime()+" != "+dataAgora.getTime()+")", issuedAt.getTime() == dataAgora.getTime());
	}

	@Test
	public void testPrecisaRenovar() {
		Claims corpoJWT = JasonWebTokenUtil.obterCorpoJWT(token);
		assertFalse("Token com validade de 4 minutos não deveria precisar validar com 3", JasonWebTokenUtil.precisaRenovar(corpoJWT, 3));
		assertTrue("Token com validade de 4 minutos deveria precisar validar com 5", JasonWebTokenUtil.precisaRenovar(corpoJWT, 5));
	}

	@Test
	public void testRenovaToken() {
		Claims corpoJWT = JasonWebTokenUtil.obterCorpoJWT(token);
		Date dataOri = corpoJWT.getIssuedAt();
		Date expOri = corpoJWT.getExpiration();
		int minutosOri = DateUtil.diferencaMinutosEntreDatas(dataOri, expOri);
		System.out.println("[testRenovaToken] minutosOri: "+minutosOri);
		String novoToken = JasonWebTokenUtil.renovaToken(corpoJWT, 10);
		//System.out.println("[renovaTokenTest] novoToken: "+novoToken);
		Claims novoCorpoJWT = JasonWebTokenUtil.obterCorpoJWT(novoToken);
		Date dataNova = novoCorpoJWT.getIssuedAt();
		Date expNova = novoCorpoJWT.getExpiration();
		int minutosNova = DateUtil.diferencaMinutosEntreDatas(dataNova, expNova);
		System.out.println("[testRenovaToken] minutosNova: "+minutosNova);
		System.out.println("[testRenovaToken] novoCorpoToken"+novoCorpoJWT.toString());
		assertTrue("Novo tempo de expiração do token deveria ser 10 minutos ou mais", minutosNova >= 10);
	}

}
