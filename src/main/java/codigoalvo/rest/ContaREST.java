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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import codigoalvo.entity.Conta;
import codigoalvo.entity.Usuario;
import codigoalvo.exceptions.RestException;
import codigoalvo.rest.util.Resposta;
import codigoalvo.security.JsonWebTokenUtil;
import codigoalvo.rest.util.ResponseBuilderHelper;
import codigoalvo.service.ContaService;
import codigoalvo.service.ContaServiceImpl;
import codigoalvo.service.UsuarioService;
import codigoalvo.service.UsuarioServiceImpl;
import codigoalvo.util.I18NUtil;
import codigoalvo.util.TipoUtil;


@Path("/usuarios/{usuarioId}/contas")
public class ContaREST {

	private static final String UTF8 = ";charset=UTF-8";
	private static final Logger LOG = Logger.getLogger(ContaREST.class);

	ContaService contaService = new ContaServiceImpl();
	UsuarioService usuarioService = new UsuarioServiceImpl();

	public ContaREST() {
		LOG.debug("####################  construct  ####################");
	}

	@Path("{contaId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	public Response find(@Context HttpHeaders headers, @PathParam("usuarioId") int usuarioId, @PathParam("contaId") int contaId) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		try {
			ResponseBuilderHelper.verificarAutenticacao(token);
			validaUsuarioId(usuarioId, token);
			Conta entidade = this.contaService.buscar(usuarioId, contaId);
			if (entidade == null) {
				throw new RestException(Response.status(Status.NOT_FOUND).entity(new Resposta("registro.naoEncontrado")));
			}

			LOG. debug("conta.find "+entidade);
			LOG.debug("conta.find.usuario :"+entidade.getUsuario());
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
	public Response list(@Context HttpHeaders headers, @PathParam("usuarioId") int usuarioId) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		try {
			ResponseBuilderHelper.verificarAutenticacao(token);
			validaUsuarioId(usuarioId, token);
			Conta[] entidades = this.contaService.listar(usuarioId).toArray(new Conta[0]);
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
	public Response insert(@Context HttpHeaders headers, Conta conta, @PathParam("usuarioId") int usuarioId) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		try {
			ResponseBuilderHelper.verificarAutenticacao(token);
			LOG.debug("gravar.conta.usuario: "+conta.getUsuario());
			Usuario usuario = validaUsuarioId(usuarioId, token);
			conta.setUsuario(usuario);
			Conta entidade = this.contaService.gravar(conta);
			ResponseBuilder resposta = Response.created(new URI("contas/"+entidade.getId())).entity(new Resposta(I18NUtil.getMessage("gravar.sucesso"),entidade));
			ResponseBuilderHelper.atualizarTokenNaRespostaSeNecessario(resposta, token);
			return resposta.build();
		} catch (Exception exc) {
			LOG.error(exc);
			return ResponseBuilderHelper.montarResponseDoErro(exc).build();
		}
	}

	@Path("{contaId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.APPLICATION_JSON + UTF8)
	public Response update(@Context HttpHeaders headers, Conta conta, @PathParam("usuarioId") int usuarioId, @PathParam("contaId") int contaId) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		try {
			ResponseBuilderHelper.verificarAutenticacao(token);
			LOG.debug("gravar.conta.usuario: "+conta.getUsuario());
			Usuario usuario = validaUsuarioId(usuarioId, token);
			conta.setUsuario(usuario);
			Conta entidade = this.contaService.gravar(conta);
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
			this.contaService.removerPorId(id);
			ResponseBuilder resposta = Response.ok().entity(new Resposta(I18NUtil.getMessage("remover.sucesso")));
			ResponseBuilderHelper.atualizarTokenNaRespostaSeNecessario(resposta, token);
			return resposta.build();
		} catch (Exception exc) {
			LOG.error(exc);
			return ResponseBuilderHelper.montarResponseDoErro(exc).build();
		}
	}

	@GET
	@Path("/tipos")
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	public Response tipos(@Context HttpHeaders headers) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		try {
			ResponseBuilderHelper.verificarAutenticacao(token);
			String tipos = TipoUtil.getTiposContaJson();
			ResponseBuilder resposta = Response.ok().entity(tipos);
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
