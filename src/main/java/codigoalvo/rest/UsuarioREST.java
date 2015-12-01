package codigoalvo.rest;

import java.sql.SQLException;

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

import codigoalvo.entity.Usuario;
import codigoalvo.rest.util.ResponseBuilderHelper;
import codigoalvo.service.UsuarioService;
import codigoalvo.service.UsuarioServiceImpl;
import codigoalvo.util.Message;


@Path("/usuarios")
public class UsuarioREST {

	private static final String UTF8 = ";charset=UTF-8";
	private static final Logger LOG = Logger.getLogger(UsuarioREST.class);

	UsuarioService service = new UsuarioServiceImpl();

	public UsuarioREST() {
		LOG.debug("####################  construct  ####################");
	}


	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	public Response find(@Context HttpHeaders headers, @PathParam("id") int id) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		ResponseBuilder resposta = ResponseBuilderHelper.verificarAutenticacao(token, true);
		if (resposta == null) {
			Usuario entidade = this.service.buscar(id);
			resposta = Response.ok().entity(entidade);
			ResponseBuilderHelper.atualizarTokenNaRespostaSeNecessario(resposta, token);
		}
		return resposta.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	public Response list(@Context HttpHeaders headers) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		ResponseBuilder resposta = ResponseBuilderHelper.verificarAutenticacao(token, true);
		if (resposta == null) {
			Usuario[] entidades = this.service.listar().toArray(new Usuario[0]);
			resposta = Response.ok().entity(entidades);
			ResponseBuilderHelper.atualizarTokenNaRespostaSeNecessario(resposta, token);
		}
		return resposta.build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.APPLICATION_JSON + UTF8)
	public Response insert(@Context HttpHeaders headers, Usuario usuario) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		ResponseBuilder resposta = ResponseBuilderHelper.verificarAutenticacao(token, true);
		if (resposta == null) {
			try {
				Usuario entidade = this.service.gravar(usuario);
				resposta = Response.ok().entity(entidade);
				ResponseBuilderHelper.atualizarTokenNaRespostaSeNecessario(resposta, token);
			} catch (Exception e) {
				e.printStackTrace();
				resposta = Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Message("Ocorreu um erro ao salvar!"));
			}
		}
		return resposta.build();
	}

	@Path("{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.APPLICATION_JSON + UTF8)
	public Response update(@Context HttpHeaders headers, Usuario usuario, @PathParam("id") int id) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		ResponseBuilder resposta = ResponseBuilderHelper.verificarAutenticacao(token, true);
		if (resposta == null) {
			try {
				Usuario entidade = this.service.gravar(usuario);
				resposta = Response.ok().entity(entidade);
				ResponseBuilderHelper.atualizarTokenNaRespostaSeNecessario(resposta, token);
			} catch (Exception e) {
				e.printStackTrace();
				resposta = Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Message("Ocorreu um erro ao salvar!"));
			}
		}
		return resposta.build();
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	public Response remove(@Context HttpHeaders headers, @PathParam("id") int id) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		ResponseBuilder resposta = ResponseBuilderHelper.verificarAutenticacao(token, true);
		if (resposta == null) {
			try {
				this.service.removerPorId(id);
				resposta = Response.ok().entity(new Message("Usuario removido com sucesso!!!"));
				ResponseBuilderHelper.atualizarTokenNaRespostaSeNecessario(resposta, token);
			} catch (SQLException e) {
				e.printStackTrace();
				resposta = Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Message("Ocorreu um erro ao remover!"));
			}
		}
		return resposta.build();
	}

	@GET
	@Path("/tipos")
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	public Response tipos(@Context HttpHeaders headers) {
		String token = ResponseBuilderHelper.obterTokenDoCabecalhoHttp(headers);
		ResponseBuilder resposta = ResponseBuilderHelper.verificarAutenticacao(token);
		if (resposta == null) {
			String tipos = this.service.tiposUsuario();
			resposta = Response.ok().entity(tipos);
			ResponseBuilderHelper.atualizarTokenNaRespostaSeNecessario(resposta, token);
		}
		return resposta.build();
	}
}
