package codigoalvo.rest;

import java.net.URI;

import javax.security.auth.login.LoginException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import codigoalvo.entity.Planejamento;
import codigoalvo.entity.Usuario;
import codigoalvo.exceptions.RestException;
import codigoalvo.rest.util.Resposta;
import codigoalvo.rest.util.ResponseBuilderHelper;
import codigoalvo.security.JsonWebTokenUtil;
import codigoalvo.service.PlanejamentoService;
import codigoalvo.service.PlanejamentoServiceImpl;
import codigoalvo.service.UsuarioService;
import codigoalvo.service.UsuarioServiceImpl;
import codigoalvo.util.I18NUtil;


@Path("/usuarios/{usuarioId}/planejamentos")
public class PlanejamentoREST {

	private static final String UTF8 = ";charset=UTF-8";
	private static final Logger LOG = Logger.getLogger(PlanejamentoREST.class);

	PlanejamentoService planejamentoService = new PlanejamentoServiceImpl();
	UsuarioService usuarioService = new UsuarioServiceImpl();

	public PlanejamentoREST() {
		LOG.trace("####################  construct  ####################");
	}

	@Path("{planejamentoId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	public Response find(@Context HttpHeaders headers, @PathParam("usuarioId") int usuarioId, @PathParam("planejamentoId") int planejamentoId) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		try {
			ResponseBuilderHelper.verificarAutenticacao(token);
			validaUsuarioId(usuarioId, token);
			Planejamento entidade = this.planejamentoService.buscar(usuarioId, planejamentoId);
			if (entidade == null) {
				throw new RestException(Response.status(Status.NOT_FOUND).entity(new Resposta("registro.naoEncontrado")));
			}
			LOG.debug("planejamento.find "+entidade);
			LOG.debug("planejamento.find.usuario :"+entidade.getUsuario());
			ResponseBuilder resposta = Response.ok().entity(entidade);
			ResponseBuilderHelper.atualizarTokenNaRespostaSeNecessario(resposta, token);
			return resposta.build();
		} catch (Exception exc) {
			LOG.error(exc);
			return ResponseBuilderHelper.montarResponseDoErro(exc).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	public Response list(@Context HttpHeaders headers, @PathParam("usuarioId") int usuarioId, @QueryParam("mes") Integer mes, @QueryParam("ano") Integer ano) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		try {
			ResponseBuilderHelper.verificarAutenticacao(token);
			validaUsuarioId(usuarioId, token);
			Planejamento[] entidades = this.planejamentoService.listarDoPeriodo(usuarioId, mes, ano).toArray(new Planejamento[0]);
			ResponseBuilder resposta = Response.ok().entity(entidades);
			ResponseBuilderHelper.atualizarTokenNaRespostaSeNecessario(resposta, token);
			return resposta.build();
		} catch (Exception exc) {
			LOG.error(exc);
			return ResponseBuilderHelper.montarResponseDoErro(exc).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.APPLICATION_JSON + UTF8)
	public Response insert(@Context HttpHeaders headers, Planejamento planejamento, @PathParam("usuarioId") int usuarioId) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		try {
			ResponseBuilderHelper.verificarAutenticacao(token);
			LOG.debug("gravar.planejamento.usuario: "+planejamento.getUsuario());
			Usuario usuario = validaUsuarioId(usuarioId, token);
			planejamento.setUsuario(usuario);
			Planejamento entidade = this.planejamentoService.gravar(planejamento);
			ResponseBuilder resposta = Response.created(new URI("planejamentos/"+entidade.getId())).entity(new Resposta(I18NUtil.getMessage("gravar.sucesso"),entidade));
			ResponseBuilderHelper.atualizarTokenNaRespostaSeNecessario(resposta, token);
			return resposta.build();
		} catch (Exception exc) {
			LOG.error(exc);
			return ResponseBuilderHelper.montarResponseDoErro(exc).build();
		}
	}

	@Path("{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.APPLICATION_JSON + UTF8)
	public Response update(@Context HttpHeaders headers, Planejamento planejamento, @PathParam("usuarioId") int usuarioId, @PathParam("id") int planejamentoId) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		try {
			ResponseBuilderHelper.verificarAutenticacao(token);
			LOG.debug("gravar.planejamento.usuario: "+planejamento.getUsuario());
			Usuario usuario = validaUsuarioId(usuarioId, token);
			planejamento.setUsuario(usuario);
			Planejamento entidade = this.planejamentoService.gravar(planejamento);
			ResponseBuilder resposta = Response.ok().entity(new Resposta(I18NUtil.getMessage("gravar.sucesso"),entidade));
			ResponseBuilderHelper.atualizarTokenNaRespostaSeNecessario(resposta, token);
			return resposta.build();
		} catch (Exception exc) {
			LOG.error(exc);
			return ResponseBuilderHelper.montarResponseDoErro(exc).build();
		}
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	public Response remove(@Context HttpHeaders headers, @PathParam("usuarioId") int usuarioId, @PathParam("id") int id) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		try {
			ResponseBuilderHelper.verificarAutenticacao(token);
			validaUsuarioId(usuarioId, token);
			this.planejamentoService.removerPorId(id);
			ResponseBuilder resposta = Response.ok().entity(new Resposta(I18NUtil.getMessage("remover.sucesso")));
			ResponseBuilderHelper.atualizarTokenNaRespostaSeNecessario(resposta, token);
			return resposta.build();
		} catch (Exception exc) {
			LOG.error(exc);
			return ResponseBuilderHelper.montarResponseDoErro(exc).build();
		}
	}

	private Usuario validaUsuarioId(int usuarioId, String token) throws LoginException {
		Usuario usuario = this.usuarioService.buscar(usuarioId);
		JsonWebTokenUtil.validarUsuario(usuario, token);
		return usuario;
	}
}
