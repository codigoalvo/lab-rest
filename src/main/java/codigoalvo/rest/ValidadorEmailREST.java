package codigoalvo.rest;

import java.net.URI;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
import codigoalvo.entity.ValidadorEmail;
import codigoalvo.entity.ValidadorEmailTipo;
import codigoalvo.exceptions.RestException;
import codigoalvo.rest.util.ResponseBuilderHelper;
import codigoalvo.rest.util.Resposta;
import codigoalvo.security.JsonWebTokenUtil;
import codigoalvo.service.ValidadorEmailService;
import codigoalvo.service.ValidadorEmailServiceImpl;
import codigoalvo.templates.TemplateUtil;
import codigoalvo.util.EmailUtil;
import codigoalvo.util.Globals;
import codigoalvo.util.I18NUtil;
import codigoalvo.util.JsonUtil;
import codigoalvo.util.MsgParamUtil;

@Path("/email")
public class ValidadorEmailREST {

	private static final String UTF8 = ";charset=UTF-8";
	private static final Logger LOG = Logger.getLogger(ValidadorEmailREST.class);

	ValidadorEmailService emailService = new ValidadorEmailServiceImpl();

	public ValidadorEmailREST() {
		LOG.debug("####################  construct  ####################");
	}

	@Path("/{tipo}/enviar")
	@POST
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.TEXT_PLAIN + UTF8)
	public Response enviarEmail(String email, @PathParam("tipo") String tipo, @Context HttpServletRequest req) {
		limparRegistrosExpirados();
		try {
			ResponseBuilder response = null;
			ValidadorEmailTipo emailTipo = ValidadorEmailTipo.getTipo(tipo.charAt(0));
			if (emailTipo == ValidadorEmailTipo.REGISTRO) {
				response = validarEmailRegistro(email);
			}
			Usuario usuario = emailService.buscarUsuarioPorEmail(email);

			LOG.debug("enviarEmail.emailTipo: "+emailTipo);
			if (emailTipo == ValidadorEmailTipo.REGISTRO) {
				if (usuario != null && usuario.getId() != null) {
					response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(I18NUtil.getMessage("registro.emailUsuarioJaExiste")));
				}
			} else if (usuario == null  ||   usuario.getId() == null) {
				response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(I18NUtil.getMessage("email.usuarioNaoEncontrado")));
			}

			if (response != null) {
				return response.build();
			}
			String origem = ResponseBuilderHelper.obterOrigemHostDoRequest(req);
			String urlValidacao = getUrlValidacao(req);
			LOG.debug("enviarEmail.origem: "+origem);
			LOG.debug("enviarEmail.email: "+email);
			LOG.debug("enviarEmail.urlValidacao: "+urlValidacao);
			ValidadorEmail entidade = emailService.gravar(new ValidadorEmail(email, new Date(), origem, emailTipo, usuario));
			if (entidade == null  ||  entidade.getId() == null) {
				throw new RestException(ResponseBuilderHelper.respostaErroInterno("registro.erro"));
			}
			boolean envioOk = EmailUtil.sendMail(entidade.getEmail(), tituloEmail(entidade), corpoEmail(entidade, urlValidacao));
			if (!envioOk) {
				throw new RestException(ResponseBuilderHelper.respostaErroInterno("email.erroEnviar"));
			}
			return Response.created(new URI("ws/email/verificar/"+entidade.getId())).entity(new Resposta(I18NUtil.getMessage("registro.sucesso"), entidade)).build();
		} catch (Exception exc) {
			LOG.error(exc);
			return ResponseBuilderHelper.montarResponseDoErro(exc).build();
		}

	}

	@Path("/verificar/{hash}")
	@GET
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.TEXT_PLAIN + UTF8)
	public Response verificar(@PathParam("hash") String hash, @Context HttpServletRequest req) {
		limparRegistrosExpirados();
		try {
			String origem = ResponseBuilderHelper.obterOrigemHostDoRequest(req);
			LOG.debug("verificar.origem: "+origem);
			LOG.debug("verificar.hash (uuid): "+hash);
			ValidadorEmail entidade = emailService.buscarPorUuid(UUID.fromString(hash));
			if (entidade == null  ||  entidade.getId() == null) {
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(I18NUtil.getMessage("registro.invalido"))).build();
			}

			LOG.debug("verificar.entidade: "+entidade);
			String token = JsonWebTokenUtil.criarJWT(entidade.getId().toString(), Globals.getProperty("EMAIL_TOKEN_SUBJECT", "EMAIL"), origem);
			LOG.debug("TOKEN de registro: "+token);
			return Response.status(Status.OK).header("Authorization", token).entity(entidade).build();
		} catch (Exception exc) {
			LOG.error(exc);
			return ResponseBuilderHelper.montarResponseDoErro(exc).build();
		}
	}

	@Path("/confirmar")
	@POST
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response confirmarRegistro(@Context HttpHeaders headers, String usuarioStr, @Context HttpServletRequest req) {
		try {
			String origem = ResponseBuilderHelper.obterOrigemHostDoRequest(req);
			String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
			LOG.debug("confirmarRegistro.origem: "+origem);
			LOG.debug("confirmarRegistro.token: "+token);
			LOG.debug("confirmarRegistro.usuarioStr: "+usuarioStr);

			ResponseBuilderHelper.verificarAutenticacao(token, false, true);

			Map<String, Object> claimsMap = JsonWebTokenUtil.getTokenClaimsMap(token);

			validarSubject(claimsMap);

			ValidadorEmail validadorEmail = validarEmailId(claimsMap);

			Usuario usuario = JsonUtil.fromJson(usuarioStr, Usuario.class);

			validarEmailUsuario(origem, validadorEmail.getEmail(), usuario.getEmail());

			Usuario entidade = emailService.confirmarRegistroUsuario(usuario, validadorEmail);
			if (entidade == null  ||  entidade.getId() == null) {
				throw new RestException(Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(I18NUtil.getMessage("registro.erro"))));
			}

			return Response.created(new URI("ws/usuarios/"+entidade.getId())).entity(entidade).build();

		} catch (Exception exc) {
			LOG.error(exc);
			return ResponseBuilderHelper.montarResponseDoErro(exc).build();
		}
	}

	@Path("/senha")
	@POST
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response recuperarSenha(String json, @Context HttpHeaders headers, @Context HttpServletRequest req) {
		try {
			String origem = ResponseBuilderHelper.obterOrigemHostDoRequest(req);
			String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
			Map<String, String> jsonMap = JsonUtil.fromJson(json);
			LOG.debug("recuperarSenha.origem: "+origem);
			LOG.debug("recuperarSenha.token: "+token);
			LOG.debug("recuperarSenha.json: "+json);
			LOG.debug("recuperarSenha.jsonMap: "+jsonMap);

			ResponseBuilderHelper.verificarAutenticacao(token, false, true);

			Map<String, Object> claimsMap = JsonWebTokenUtil.getTokenClaimsMap(token);

			validarSubject(claimsMap);

			ValidadorEmail validadorEmail = validarEmailId(claimsMap);

			Usuario usuario = validadorEmail.getUsuario();

			String email = jsonMap.get("email");
			String novaSenha = jsonMap.get("senha");

			validarEmailUsuario(origem, validadorEmail.getEmail(), email);
			validarEmailUsuario(origem, validadorEmail.getEmail(), usuario.getEmail());

			if (novaSenha == null  || novaSenha.trim().isEmpty()) {
				throw new RestException(Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(I18NUtil.getMessage("senha.novaInvalida"))));
			}

			Usuario entidade = emailService.alterarSenhaUsuario(usuario, validadorEmail, novaSenha);
			if (entidade == null  ||  entidade.getId() == null) {
				throw new RestException(Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(I18NUtil.getMessage("senha.erro"))));
			}

			return Response.created(new URI("ws/usuarios/"+entidade.getId())).entity(entidade).build();
		} catch (Exception exc) {
			LOG.error(exc);
			return ResponseBuilderHelper.montarResponseDoErro(exc).build();
		}
	}

	private static String getUrlValidacao(HttpServletRequest req) {
		String registerUrl = "";
		if (Globals.getBoolean("USAR_URL_APLICACAO_GLOBALS", false)) {
			registerUrl = Globals.getProperty("URL_APLICACAO");
		} else if (req.getContextPath() == null  || req.getContextPath().isEmpty()  ||  req.getContextPath().equals("/")) {
			registerUrl = "http"+(req.isSecure()?"s:":":")+"//"+req.getLocalName()+":"+req.getLocalPort()+req.getContextPath();
		} else {
			String requestUrl = req.getRequestURL().toString();
			registerUrl = requestUrl.substring(0, requestUrl.indexOf(req.getContextPath()));
			registerUrl += req.getContextPath();
		}
		registerUrl	+= Globals.getProperty("CAMINHO_CONFIRMAR_REGISTRO");
		return registerUrl;
	}

	private void validarEmailUsuario(String origem, String emailValidador, String emailUsuario) throws RestException {
		if (emailUsuario == null || emailUsuario.isEmpty() || !emailUsuario.toLowerCase().equals(emailValidador.toLowerCase())) {
			String msg = "Email no token de autorização difere do email informado!";
			LOG.debug(msg);
			LOG.error("Possivel tentativa de fraude: token.email='"+emailValidador+"', usuario.email='"+emailUsuario+"', origem: '"+origem+"'");
			throw new RestException(Response.status(Status.FORBIDDEN).entity(new Resposta(msg)));
		}
	}

	private ValidadorEmail validarEmailId(Map<String, Object> claimsMap) throws RestException {
		String id = (String)claimsMap.get("jti");
		ValidadorEmail validadorEmail = null;
		if (id != null  &&  !id.isEmpty()) {
			validadorEmail = emailService.buscarPorUuid(UUID.fromString(id));
		}
		if (validadorEmail == null || validadorEmail.getId() == null || validadorEmail.getEmail() == null) {
			String msg = "Identificação do token de autorização inválida ou expirada!";
			LOG.debug(msg+" : "+id);
			throw new RestException(Response.status(Status.FORBIDDEN).entity(new Resposta(msg)));
		}
		return validadorEmail;
	}

	private void validarSubject(Map<String, Object> claimsMap) throws RestException {
		String subject = (String)claimsMap.get("sub");
		if (subject == null  ||  !subject.equals(Globals.getProperty("EMAIL_TOKEN_SUBJECT", "EMAIL"))) {
			String msg = "Token de autorização de registro inválido!";
			LOG.debug(msg);
			throw new RestException(Response.status(Status.UNAUTHORIZED).entity(new Resposta(msg)));
		}
	}

	private ResponseBuilder validarEmailRegistro(String email) {

		if (!Globals.getBoolean("VALIDADOR_PERMITIR_REGISTROS", false)) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(I18NUtil.getMessage("registro.naoPermitido")));
		}

		ValidadorEmail validadorEmail = emailService.buscarPorEmail(email);
		if (validadorEmail != null  &&  validadorEmail.getId() != null) {
			String msg = "registro.emailRegistroJaExiste";
			msg += MsgParamUtil.buildParams(Globals.getProperty("VALIDADOR_EMAIL_HORAS_VALIDADE"));
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(I18NUtil.getMessage(msg)));
		}

		int minutos = Globals.getInt("VALIDADOR_EMAIL_MINUTOS_INTERVALO", 0);
		if (minutos > 0) {
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.add(Calendar.MINUTE, -minutos);
			List<ValidadorEmail> registros = emailService.buscarRegistrosDepoisDe(calendar.getTime());
			if (registros != null && !registros.isEmpty()) {
				String msg = "registro.excessoTentativasMinutos";
				msg += MsgParamUtil.buildParams(Globals.getProperty("VALIDADOR_EMAIL_MINUTOS_INTERVALO"));
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(I18NUtil.getMessage(msg)));
			}
		}

		return null;
	}

	private void limparRegistrosExpirados() {
		int horas = Globals.getInt("VALIDADOR_EMAIL_HORAS_VALIDADE", 0);
		if (horas > 0) {
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.add(Calendar.HOUR, -horas);
			try {
				LOG.debug("Limpando validadores de registros expirados. (Anteriores à :"+calendar.getTime()+")");
				emailService.removerAnterioresData(calendar.getTime());
			} catch (SQLException exc) {
			}
		}
	}

	private static String tituloEmail(ValidadorEmail entidade) {
		String titulo = "codigoalvo - ";
		if (entidade.getTipo() == ValidadorEmailTipo.REGISTRO) {
			titulo += "Verificação de email";
		} else if (entidade.getTipo() == ValidadorEmailTipo.SENHA) {
			titulo += "Recuperação de senha";
		} else if (entidade.getTipo() == ValidadorEmailTipo.EMAIL) {
			titulo += "Alteração de email";
		}
		titulo += " ("+entidade.getId()+")";
		return titulo;
	}

	private static String corpoEmail(ValidadorEmail entidade, String urlRegistro) {
		String emailCorpo = TemplateUtil.getHtmlTemplateEmail(entidade, urlRegistro);
		LOG.debug("emailCorppo: "+emailCorpo);
		if (emailCorpo == null  ||  emailCorpo.isEmpty())
			throw new IllegalArgumentException("Error on get email body!");
		return emailCorpo;
	}
}
