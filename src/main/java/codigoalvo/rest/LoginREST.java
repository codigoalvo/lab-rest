package codigoalvo.rest;

import java.util.Map;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.log4j.Logger;

import codigoalvo.entity.Usuario;
import codigoalvo.rest.util.Resposta;
import codigoalvo.rest.util.ResponseBuilderHelper;
import codigoalvo.security.JsonWebTokenUtil;
import codigoalvo.security.LoginToken;
import codigoalvo.service.LoginService;
import codigoalvo.service.LoginServiceImpl;
import codigoalvo.util.I18NUtil;
import codigoalvo.util.JsonUtil;
import codigoalvo.util.UsuarioUtil;


@Path("/auth")
public class LoginREST {

	private static final String UTF8 = ";charset=UTF-8";
	private static final Logger LOG = Logger.getLogger(LoginREST.class);

	LoginService service = new LoginServiceImpl();

	public LoginREST() {
		LOG.debug("####################  construct  ####################");
	}

	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.APPLICATION_JSON + UTF8)
	public Response efetuarLogin(String usuarioLoginStr, @Context HttpServletRequest req) {
		try {
			String origem = ResponseBuilderHelper.obterOrigemHostDoRequest(req);
			Map<String, String> jsonMap = JsonUtil.fromJson(usuarioLoginStr);
			System.out.println("efetuarLogin.usuarioLoginStr: "+usuarioLoginStr);
			System.out.println("efetuarLogin.jsonMap: "+jsonMap);
			Usuario usuario = this.service.efetuarLogin(jsonMap.get("email"),jsonMap.get("senha"));
			LoginToken login = UsuarioUtil.usuarioToToken(usuario, origem);
			String token = JsonWebTokenUtil.criarJWT(login);
			LOG.debug("efetuarLogin.token: "+token);
			return Response.status(Status.OK).header("Authorization", token).entity(new Resposta(I18NUtil.getMessage("login.sucesso"))).build();
		} catch (Exception exc) {
			LOG.error(exc);
			return ResponseBuilderHelper.montarResponseDoErro(exc).build();
		}
	}

	@Path("/senha")
	@POST
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.APPLICATION_JSON + UTF8)
	public Response alterarSenha(@Context HttpHeaders headers, String usuarioLoginStr, @Context HttpServletRequest req) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		try {
			ResponseBuilderHelper.verificarAutenticacao(token);
			String origem = ResponseBuilderHelper.obterOrigemHostDoRequest(req);
			Map<String, String> jsonMap = JsonUtil.fromJson(usuarioLoginStr);
			System.out.println("alterarSenha.usuarioLoginStr: "+usuarioLoginStr);
			System.out.println("alterarSenha.jsonMap: "+jsonMap);
			Usuario usuario = this.service.buscarUsuarioPorEmailSenha(jsonMap.get("email"), jsonMap.get("senha"));
			JsonWebTokenUtil.validarUsuario(usuario, token);
			usuario = this.service.alterarSenha(usuario, jsonMap.get("senhaNova"));
			if (usuario == null  || usuario.getId() == null) {
				throw new LoginException("login.invalido");
			}
			LoginToken login = UsuarioUtil.usuarioToToken(usuario, origem);
			String novoToken = JsonWebTokenUtil.criarJWT(login);
			LOG.debug("alterarSenha.novoToken: "+novoToken);
			return Response.status(Status.OK).header("Authorization", novoToken).entity(new Resposta(I18NUtil.getMessage("senha.alterarSucesso"))).build();
		} catch (Exception exc) {
			LOG.error(exc);
			return ResponseBuilderHelper.montarResponseDoErro(exc).build();
		}
	}

}