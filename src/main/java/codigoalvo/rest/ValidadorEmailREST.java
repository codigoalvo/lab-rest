package codigoalvo.rest;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import codigoalvo.entity.Usuario;
import codigoalvo.entity.UsuarioTipo;
import codigoalvo.entity.ValidadorEmail;
import codigoalvo.rest.util.ResponseBuilderHelper;
import codigoalvo.rest.util.Resposta;
import codigoalvo.service.UsuarioService;
import codigoalvo.service.UsuarioServiceImpl;
import codigoalvo.service.ValidadorEmailService;
import codigoalvo.service.ValidadorEmailServiceImpl;
import codigoalvo.util.EmailUtil;
import codigoalvo.util.I18NUtil;
import codigoalvo.util.JsonUtil;

@Path("/email")
public class ValidadorEmailREST {

	private static final String UTF8 = ";charset=UTF-8";
	private static final Logger LOG = Logger.getLogger(ValidadorEmailREST.class);
	private static final String APP_PATH = "http://localhost:8080/lab-rest/";

	ValidadorEmailService emailService = new ValidadorEmailServiceImpl();
	UsuarioService usuarioService = new UsuarioServiceImpl();

	public ValidadorEmailREST() {
		LOG.debug("####################  construct  ####################");
	}

	@Path("/registrar")
	@POST
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.TEXT_PLAIN + UTF8)
	public Response registrar(String email, @Context HttpServletRequest req) {
		try {
			String origem = ResponseBuilderHelper.obterOrigemHostDoRequest(req);
			LOG.debug("registrar.origem: "+origem);
			LOG.debug("registrar.email: "+email);
			ValidadorEmail entidade = emailService.gravar(new ValidadorEmail(email, new Date(), origem));
			if (entidade != null  &&  entidade.getId() != null) {
				boolean envioOk = EmailUtil.sendMail(entidade.getEmail(), "Verificação de email", corpoEmail(entidade));
				if (envioOk) {
					return Response.created(new URI("ws/email/verificar/"+entidade.getId())) .entity(new Resposta(I18NUtil.getMessage("registro.sucesso"), entidade)).build();
				}
			}
		} catch (Exception exc) {
			LOG.error(exc);
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(I18NUtil.getMessage("registro.erro"))).build();
	}

	@Path("/verificar/{hash}")
	@GET
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.TEXT_PLAIN + UTF8)
	public Response verificar(@PathParam("hash") String hash, @Context HttpServletRequest req) {
		try {
			String origem = ResponseBuilderHelper.obterOrigemHostDoRequest(req);
			LOG.debug("verificar.origem: "+origem);
			LOG.debug("verificar.hash (uuid): "+hash);
			ValidadorEmail entidade = emailService.buscarPorUuid(UUID.fromString(hash));
			if (entidade != null  && entidade.getId() != null) {
				LOG.debug("verificar.entidade: "+entidade);
				return Response.status(Status.OK).entity(entidade).build();
			}
		} catch (Exception exc) {
			LOG.error(exc);
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(I18NUtil.getMessage("registro.invalido"))).build();
	}

	@Path("/confirmar")
	@POST
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response confirmar(String usuarioStr, @Context HttpServletRequest req) {
		try {
			String origem = ResponseBuilderHelper.obterOrigemHostDoRequest(req);
			LOG.debug("confirmar.origem: "+origem);
			LOG.debug("confirmar.usuarioStr: "+usuarioStr);
			Usuario usuario = JsonUtil.fromJson(usuarioStr, Usuario.class);
			usuario.setTipo(UsuarioTipo.USER);
			Usuario entidade = usuarioService.gravar(usuario);
			if (entidade != null  &&  entidade.getId() != null) {
				return Response.created(new URI("ws/usuarios/"+entidade.getId())).entity(entidade).build();
			}
		} catch (Exception exc) {
			LOG.error(exc);
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(I18NUtil.getMessage("registro.erro"))).build();
	}

	private static String corpoEmail(ValidadorEmail entidade) {
		StringBuilder corpo = new StringBuilder();
		corpo.append("<h3>Clique no link abaixo para confirmar o email <b>").append(entidade.getEmail()).append("</b>").append("</h3>").append("<br/>");
		corpo.append("<a href='").append(APP_PATH).append("ws/email/verificar/"+entidade.getId()).append("'>").append("[VERIFICAR]").append("</a>").append("<br/>");
		corpo.append("<a href='").append(APP_PATH).append("#/registro/confirmar/"+entidade.getId()).append("'>").append("[CONFIRMAR]").append("</a>").append("<br/>");
		return corpo.toString();
	}
}
