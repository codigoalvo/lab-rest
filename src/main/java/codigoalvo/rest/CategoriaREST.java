package codigoalvo.rest;

import java.sql.SQLException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;

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
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import codigoalvo.entity.Categoria;
import codigoalvo.service.CategoriaService;
import codigoalvo.service.CategoriaServiceImpl;
import codigoalvo.util.JasonWebTokenUtil;
import codigoalvo.util.Message;


@Path("/categorias")
public class CategoriaREST {

	private static final String UTF8 = ";charset=UTF-8";
	private static final Logger LOG = Logger.getLogger(CategoriaREST.class);
	private static final boolean AUTHENTICATION_ENABLED = true;

	CategoriaService service = new CategoriaServiceImpl();

	public CategoriaREST() {
		LOG.debug("####################  construct  ####################");
	}

	private Response checkAuthentication(HttpHeaders headers) {
		if (!AUTHENTICATION_ENABLED) {
			return null;
		}
		for(String header : headers.getRequestHeaders().keySet()){
			LOG.debug("### Header ###   "+header+" = "+headers.getRequestHeaders().get(header));
		}
		String token = headers.getRequestHeaders().getFirst("Authorization");
		LOG.debug("TOKEN: "+token);
		if (!headers.getRequestHeaders().containsKey("Authorization")) {
			LOG.debug("Token does not exists. Returning 401");
			return Response.status(Status.UNAUTHORIZED).entity(new Message("Authorization token is missing!")).build();
		} else {
			try {
				@SuppressWarnings("rawtypes")
				Jwt jwt = JasonWebTokenUtil.decodificarJWT(token);
				LOG.debug("JWT: "+ jwt.toString());
				Claims corpoJwt = JasonWebTokenUtil.obterCorpoJWT(token);
				String issuer = corpoJwt.getIssuer();
				if (!JasonWebTokenUtil.ISSUER.equals(issuer)) {
					return Response.status(Status.UNAUTHORIZED).entity(new Message("Invalid authorization token!")).build();
				}
				LOG.debug("JWt Subject: "+corpoJwt.getSubject());
				return null;
			} catch (ExpiredJwtException exc) {
				return Response.status(Status.UNAUTHORIZED).entity(new Message("Authorization token is expired!")).build();
			}
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	public Response find(@Context HttpHeaders headers, @PathParam("id") int id) {
		Response resposta = checkAuthentication(headers);
		if (resposta == null) {
			Categoria entidade = this.service.buscar(id);
			resposta = Response.ok().entity(entidade).build();
		}
		return resposta;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	public Response list(@Context HttpHeaders headers) {
		Response resposta = checkAuthentication(headers);
		if (resposta == null) {
			Categoria[] entidades = this.service.listar().toArray(new Categoria[0]);
			resposta = Response.ok().entity(entidades).build();
		}
		return resposta;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insert(@Context HttpHeaders headers, Categoria categoria) {
		Response resposta = checkAuthentication(headers);
		if (resposta == null) {
			try {
				Categoria entidade = this.service.gravar(categoria);
				resposta = Response.ok().entity(entidade).build();
			} catch (Exception e) {
				e.printStackTrace();
				resposta = Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity(new Message("Ocorreu um erro ao salvar!")).build();
			}
		}
		return resposta;
	}

	@Path("{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@Context HttpHeaders headers, Categoria categoria, @PathParam("id") int id) {
		Response resposta = checkAuthentication(headers);
		if (resposta == null) {
			try {
				Categoria entidade = this.service.gravar(categoria);
				resposta = Response.ok().entity(entidade).build();
			} catch (Exception e) {
				e.printStackTrace();
				resposta = Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity(new Message("Ocorreu um erro ao salvar!")).build();
			}
		}
		return resposta;
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	public Response remove(@Context HttpHeaders headers, @PathParam("id") int id) {
		Response resposta = checkAuthentication(headers);
		if (resposta == null) {
			try {
				this.service.removerPorId(id);
				resposta = Response.ok().entity(new Message("Categoria removida com sucesso!!!")).build();
			} catch (SQLException e) {
				e.printStackTrace();
				resposta = Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity(new Message("Ocorreu um erro ao remover!")).build();
			}
		}
		return resposta;
	}
}
