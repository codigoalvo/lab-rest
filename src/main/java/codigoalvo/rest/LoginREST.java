package codigoalvo.rest;

import java.io.Serializable;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import codigoalvo.entity.Usuario;
import codigoalvo.security.JasonWebTokenUtil;
import codigoalvo.security.LoginToken;
import codigoalvo.service.LoginService;
import codigoalvo.service.LoginServiceImpl;


@Path("/login")
public class LoginREST {

	private static final String UTF8 = ";charset=UTF-8";
	private static final Logger LOG = Logger.getLogger(LoginREST.class);

	LoginService service = new LoginServiceImpl();

	public LoginREST() {
		LOG.debug("####################  construct  ####################");
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.APPLICATION_JSON + UTF8)
	public Response login(String usuarioLoginStr) {
		try {
			UsuarioLogin usuarioLogin = new Gson().fromJson(usuarioLoginStr, UsuarioLogin.class);
			System.out.println("usuarioLoginStr: "+usuarioLoginStr);
			System.out.println("usuarioLogin: "+usuarioLogin);
			Usuario usuario = this.service.efetuarLogin(usuarioLogin.getLogin(), usuarioLogin.getSenha());
			String token = JasonWebTokenUtil.criarJWT(usuario, 5);
			LoginToken login = new LoginToken(usuario.getLogin(), usuario.getNome(), usuario.getEmail(), usuario.getTipo());
			LOG.debug("TOKEN: "+token);
			return Response.status(Status.OK).header("Authorization", token).entity(login).build();
		} catch (Exception exc) {
			exc.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Usuario()).build();
		}
	}

	@XmlRootElement
	class UsuarioLogin implements Serializable {
		private static final long serialVersionUID = 276677829784526134L;
		private String login;
		private String senha;
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
		@Override
		public String toString() {
			return "UsuarioLogin: login="+this.login+", senha="+this.senha;
		}
	}
}