package codigoalvo.rest;

import io.jsonwebtoken.Claims;

import java.io.Serializable;

import javax.security.auth.login.LoginException;
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
import codigoalvo.rest.util.ResponseBuilderHelper;
import codigoalvo.security.JasonWebTokenUtil;
import codigoalvo.security.LoginToken;
import codigoalvo.service.LoginService;
import codigoalvo.service.LoginServiceImpl;
import codigoalvo.util.JsonUtil;
import codigoalvo.util.UsuarioTipoUtil;


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
	public Response login(String usuarioLoginStr) {
		try {
			UsuarioLogin usuarioLogin = JsonUtil.fromJson(usuarioLoginStr, UsuarioLogin.class);
			System.out.println("usuarioLoginStr: "+usuarioLoginStr);
			System.out.println("usuarioLogin: "+usuarioLogin);
			Usuario usuario = this.service.efetuarLogin(usuarioLogin.getLogin(), usuarioLogin.getSenha());
			LoginToken login = new LoginToken(usuario.getId(), usuario.getLogin(), usuario.getNome(), usuario.getEmail(), usuario.getTipo(), UsuarioTipoUtil.encodeTipo(usuario.getTipo(), usuario.getId()));
			String token = JasonWebTokenUtil.criarJWT(login);
			LOG.debug("TOKEN: "+token);
			return Response.status(Status.OK).header("Authorization", token).entity(login).build();
		} catch (Exception exc) {
			exc.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Usuario()).build();
		}
	}

	@Path("/senha")
	@POST
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.APPLICATION_JSON + UTF8)
	public Response senha(@Context HttpHeaders headers, String usuarioLoginStr) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		ResponseBuilder resposta = ResponseBuilderHelper.verificarAutenticacao(token);
		if (resposta == null) {
			try {
				UsuarioLogin usuarioLogin = JsonUtil.fromJson(usuarioLoginStr, UsuarioLogin.class);
				System.out.println("usuarioLoginStr: "+usuarioLoginStr);
				System.out.println("usuarioLogin: "+usuarioLogin);
				Usuario usuario = this.service.buscarUsuarioPorLoginSenha(usuarioLogin.getLogin(), usuarioLogin.getSenha());
				validarUsuario(usuario, token);
				usuario = this.service.alterarSenha(usuario, usuarioLogin.getSenhaNova());
				if (usuario == null  || usuario.getId() == null) {
					throw new LoginException("login.erroAlterarSenha");
				}
				LoginToken login = new LoginToken(usuario.getId(), usuario.getLogin(), usuario.getNome(), usuario.getEmail(), usuario.getTipo(), UsuarioTipoUtil.encodeTipo(usuario.getTipo(), usuario.getId()));
				String novoToken = JasonWebTokenUtil.criarJWT(login);
				LOG.debug("NOVO TOKEN: "+novoToken);
				resposta =  Response.status(Status.OK).header("Authorization", novoToken).entity(login);
			} catch (Exception exc) {
				LOG.error(exc);
				resposta = Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Usuario());
			}
		}
		return resposta.build();
	}

	private void validarUsuario(Usuario usuario, String token) throws LoginException {
		try {
			Claims corpoJWT = JasonWebTokenUtil.obterCorpoJWT(token);
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
			if (usuario.getTipo() != UsuarioTipoUtil.decodeTipo(login.getExtp(), login.getId())) {
				throw new LoginException("Dados invalidos no token! tipo");
			}
		} catch (Throwable exc) {
			if (exc instanceof LoginException) {
				throw (LoginException)exc;
			} else {
				LOG.error(exc);
				throw new LoginException(exc.getMessage());
			}
		}
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