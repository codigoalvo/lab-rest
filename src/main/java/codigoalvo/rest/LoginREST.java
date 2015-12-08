package codigoalvo.rest;

import java.io.Serializable;

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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import codigoalvo.entity.Usuario;
import codigoalvo.rest.util.Resposta;
import codigoalvo.rest.util.ResponseBuilderHelper;
import codigoalvo.security.JsonWebTokenUtil;
import codigoalvo.security.LoginToken;
import codigoalvo.service.LoginService;
import codigoalvo.service.LoginServiceImpl;
import codigoalvo.util.ErrosUtil;
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
	public Response login(String usuarioLoginStr, @Context HttpServletRequest req) {
		try {
			String origem = getOrigemHost(req);
			UsuarioLogin usuarioLogin = JsonUtil.fromJson(usuarioLoginStr, UsuarioLogin.class);
			System.out.println("usuarioLoginStr: "+usuarioLoginStr);
			System.out.println("usuarioLogin: "+usuarioLogin);
			Usuario usuario = this.service.efetuarLogin(usuarioLogin.getLogin(), usuarioLogin.getSenha());
			LoginToken login = UsuarioUtil.usuarioToToken(usuario, origem);
			String token = JsonWebTokenUtil.criarJWT(login);
			LOG.debug("TOKEN: "+token);
			return Response.status(Status.OK).header("Authorization", token).entity(new Resposta(I18NUtil.getMessage("login.sucesso"))).build();
		} catch (Exception exc) {
			LOG.error(ErrosUtil.getMensagemErro(exc), exc);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(ErrosUtil.getMensagemErro(exc))).build();
		}
	}

	private String getOrigemHost(HttpServletRequest httpServletRequest) {
		String remoteHost = httpServletRequest.getRemoteHost();
		String remoteAddr = httpServletRequest.getRemoteAddr();
		int remotePort = httpServletRequest.getRemotePort();
		String origem = remoteHost + "(" + remoteAddr + ":" + remotePort + ")";
		LOG.debug(origem);
		return origem;
	}

	@Path("/senha")
	@POST
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.APPLICATION_JSON + UTF8)
	public Response senha(@Context HttpHeaders headers, String usuarioLoginStr, @Context HttpServletRequest req) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		ResponseBuilder resposta = ResponseBuilderHelper.verificarAutenticacao(token);
		if (resposta == null) {
			try {
				String origem = getOrigemHost(req);
				UsuarioLogin usuarioLogin = JsonUtil.fromJson(usuarioLoginStr, UsuarioLogin.class);
				System.out.println("usuarioLoginStr: "+usuarioLoginStr);
				System.out.println("usuarioLogin: "+usuarioLogin);
				Usuario usuario = this.service.buscarUsuarioPorLoginSenha(usuarioLogin.getLogin(), usuarioLogin.getSenha());
				JsonWebTokenUtil.validarUsuario(usuario, token);
				usuario = this.service.alterarSenha(usuario, usuarioLogin.getSenhaNova());
				if (usuario == null  || usuario.getId() == null) {
					throw new LoginException("login.invalido");
				}
				LoginToken login = UsuarioUtil.usuarioToToken(usuario, origem);
				String novoToken = JsonWebTokenUtil.criarJWT(login);
				LOG.debug("NOVO TOKEN: "+novoToken);
				resposta =  Response.status(Status.OK).header("Authorization", novoToken).entity(new Resposta(I18NUtil.getMessage("senha.alterarSucesso")));
			} catch (Exception exc) {
				LOG.error(exc);
				resposta = Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(ErrosUtil.getMensagemErro(exc)));
			}
		}
		return resposta.build();
	}

	@XmlRootElement
	class UsuarioLogin implements Serializable {
		private static final long serialVersionUID = 276677829784526134L;
		private String login;
		private String senha;
		private String senhaNova;
		public String getLogin() {
			return this.login;
		}
		public void setLogin(String login) {
			this.login = login;
		}
		public String getSenha() {
			return this.senha;
		}
		public void setSenha(String senha) {
			this.senha = senha;
		}
		public String getSenhaNova() {
			return this.senhaNova;
		}
		public void setSenhaNova(String senhaNova) {
			this.senhaNova = senhaNova;
		}
		@Override
		public String toString() {
			return "UsuarioLogin: login="+this.login+", senha="+this.senha+", senhaNova="+this.senhaNova;
		}
	}
}