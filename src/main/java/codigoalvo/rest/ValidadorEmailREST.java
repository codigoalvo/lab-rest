package codigoalvo.rest;

import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import codigoalvo.entity.Usuario;
import codigoalvo.entity.UsuarioTipo;
import codigoalvo.entity.ValidadorEmail;
import codigoalvo.rest.util.ResponseBuilderHelper;
import codigoalvo.rest.util.Resposta;
import codigoalvo.security.JsonWebTokenUtil;
import codigoalvo.service.UsuarioService;
import codigoalvo.service.UsuarioServiceImpl;
import codigoalvo.service.ValidadorEmailService;
import codigoalvo.service.ValidadorEmailServiceImpl;
import codigoalvo.templates.TemplateUtil;
import codigoalvo.util.EmailUtil;
import codigoalvo.util.Globals;
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
					return Response.created(new URI("ws/email/verificar/"+entidade.getId())).entity(new Resposta(I18NUtil.getMessage("registro.sucesso"), entidade)).build();
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
				String token = JsonWebTokenUtil.criarJWT(entidade.getId().toString(), Globals.getProperty("REGISTER_TOKEN_SUBJECT", "REGISTER"), origem);
				LOG.debug("TOKEN de registro: "+token);
				return Response.status(Status.OK).header("Authorization", token).entity(entidade).build();
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
	public Response confirmar(@Context HttpHeaders headers, String usuarioStr, @Context HttpServletRequest req) {
		ResponseBuilder response = null;
		try {
			String origem = ResponseBuilderHelper.obterOrigemHostDoRequest(req);
			LOG.debug("confirmar.origem: "+origem);
			LOG.debug("confirmar.usuarioStr: "+usuarioStr);

			String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
			LOG.debug("TOKEN de Registro do Header: "+token);
			response = ResponseBuilderHelper.verificarAutenticacao(token, false, true);
			if (response != null) {
				return response.build();
			}

			Map<String, Object> claimsMap = JsonWebTokenUtil.getTokenClaimsMap(token);

			String subject = (String)claimsMap.get("sub");
			if (subject == null  ||  !subject.equals(Globals.getProperty("REGISTER_TOKEN_SUBJECT", "REGISTER"))) {
				String msg = "Token de autorização de registro inválido!";
				LOG.debug(msg);
				return Response.status(Status.UNAUTHORIZED).entity(new Resposta(msg)).build();
			}

			String id = (String)claimsMap.get("jti");
			ValidadorEmail validadorEmail = null;
			if (id != null  &&  !id.isEmpty()) {
				validadorEmail = emailService.buscarPorUuid(UUID.fromString(id));
			}
			if (validadorEmail == null || validadorEmail.getId() == null || validadorEmail.getEmail() == null) {
				String msg = "Identificação do token de autorização de registro inválida ou expirada!";
				LOG.debug(msg+" : "+id);
				return Response.status(Status.FORBIDDEN).entity(new Resposta(msg)).build();
			}

			Usuario usuario = JsonUtil.fromJson(usuarioStr, Usuario.class);
			if (usuario.getEmail() == null || usuario.getEmail().isEmpty() || !usuario.getEmail().toLowerCase().equals(validadorEmail.getEmail().toLowerCase())) {
				String msg = "Email no token de autorização difere do email do usuário a ser cadastrado!";
				LOG.debug(msg);
				LOG.debug("Possivel tentativa de fraude: token.email='"+validadorEmail.getEmail()+"', usuario.email='"+usuario.getEmail()+"'");
				return Response.status(Status.FORBIDDEN).entity(new Resposta(msg)).build();
			}

			// TODO! Gravar o usuário e apagar o token na mesma transação. Para isso o EntityManager de ambos os services devem ser o mesmo.
			usuario.setTipo(UsuarioTipo.USER);
			Usuario entidade = usuarioService.gravar(usuario);
			LOG.debug("Usuário criado com sucesso! "+usuario.getId());

			emailService.remover(validadorEmail);
			LOG.debug("Token validador de registro de email removido com sucesso!");

			if (entidade != null  &&  entidade.getId() != null) {
				return Response.created(new URI("ws/usuarios/"+entidade.getId())).entity(entidade).build();
			}
		} catch (Exception exc) {
			LOG.error(exc);
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(I18NUtil.getMessage("registro.erro"))).build();
	}

	private static String corpoEmail(ValidadorEmail entidade) {
		String emailCorpo = TemplateUtil.getHtmlTemplateEmail(entidade.getEmail(), entidade.getId().toString());
		LOG.debug("emailCorppo: "+emailCorpo);
		if (emailCorpo == null  ||  emailCorpo.isEmpty())
			throw new IllegalArgumentException("Error on get email body!");
		return emailCorpo;
	}
}
