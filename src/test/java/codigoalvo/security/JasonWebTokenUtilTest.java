package codigoalvo.security;

import io.jsonwebtoken.Claims;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import codigoalvo.entity.Usuario;
import codigoalvo.entity.UsuarioTipo;
import codigoalvo.util.DateUtil;
import codigoalvo.util.UsuarioUtil;

public class JasonWebTokenUtilTest {

	private static final LoginToken LOGIN_TOKEN = UsuarioUtil.usuarioToToken(new Usuario(1, "admin", "Administrador", "admin@email.com", UsuarioTipo.ADMIN), "127.0.0.1");
	private static final long AGORA = System.currentTimeMillis();
	private static String token;

	@BeforeClass
	public static void prepare() {
		token = JsonWebTokenUtil.criarJWT(LOGIN_TOKEN , AGORA, 4);
		System.out.println("[JasonWebTokenUtilTest prepare] agora (Date): "+new Date(AGORA));
		System.out.println("[JasonWebTokenUtilTest prepare] agora: "+AGORA);
		System.out.println("[JasonWebTokenUtilTest prepare] loginToken: "+LOGIN_TOKEN);
		System.out.println("[JasonWebTokenUtilTest prepare] token: "+token);
	}

	@Test
	public void testObterUsuarioJWT() {
		LoginToken usuario = JsonWebTokenUtil.obterLoginToken(token);
		UsuarioTipo usuarioTipo = UsuarioUtil.decodeTipoFromHash(usuario);
		System.out.println("[testObterUsuarioJWT] usuario: "+usuario);
		System.out.println("[testObterUsuarioJWT] usuario.tipo: "+usuarioTipo);
		assertTrue("Login do usuario deveria ser admin", "admin".equals(usuario.getLogin()));
		assertTrue("Tipo do usuario deveria ser ADMIN", UsuarioTipo.ADMIN == usuarioTipo  &&  usuario.getTipo() == usuarioTipo);
	}

	@Test
	public void testObterCorpoJWT() {
		Claims corpoJWT = JsonWebTokenUtil.obterCorpoJWT(token);
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
		Claims corpoJWT = JsonWebTokenUtil.obterCorpoJWT(token);
		assertFalse("Token com validade de 4 minutos não deveria precisar validar com 3", JsonWebTokenUtil.precisaRenovar(corpoJWT, 3));
		assertTrue("Token com validade de 4 minutos deveria precisar validar com 5", JsonWebTokenUtil.precisaRenovar(corpoJWT, 5));
	}

	@Test
	public void testRenovaToken() {
		Claims corpoJWT = JsonWebTokenUtil.obterCorpoJWT(token);
		Date dataOri = corpoJWT.getIssuedAt();
		Date expOri = corpoJWT.getExpiration();
		int minutosOri = DateUtil.diferencaMinutosEntreDatas(dataOri, expOri);
		System.out.println("[testRenovaToken] minutosOri: "+minutosOri);
		String novoToken = JsonWebTokenUtil.renovaToken(corpoJWT, 10);
		//System.out.println("[renovaTokenTest] novoToken: "+novoToken);
		Claims novoCorpoJWT = JsonWebTokenUtil.obterCorpoJWT(novoToken);
		Date dataNova = novoCorpoJWT.getIssuedAt();
		Date expNova = novoCorpoJWT.getExpiration();
		int minutosNova = DateUtil.diferencaMinutosEntreDatas(dataNova, expNova);
		System.out.println("[testRenovaToken] minutosNova: "+minutosNova);
		System.out.println("[testRenovaToken] novoCorpoToken"+novoCorpoJWT.toString());
		assertTrue("Novo tempo de expiração do token deveria ser 10 minutos ou mais", minutosNova >= 10);
	}

}
