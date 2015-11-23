package codigoalvo.rest;

import io.jsonwebtoken.Jwt;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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


@Path("/categoria")
public class CategoriaREST {

	private static final String UTF8 = ";charset=UTF-8";
	private static final Logger LOG = Logger.getLogger(CategoriaREST.class);

	CategoriaService service = new CategoriaServiceImpl();

	public CategoriaREST() {
		LOG.debug("####################  construct  ####################");
	}

	@Path("/list")
	@GET
	@Produces(MediaType.APPLICATION_JSON + UTF8)
	public Response list(@Context HttpHeaders headers) {
//		for(String header : headers.getRequestHeaders().keySet()){
//			LOG.debug("### Header ###   "+header+" = "+headers.getRequestHeaders().get(header));
//		}
		String token = headers.getRequestHeaders().getFirst("Authorization");
		LOG.debug("TOKEN: "+token);
		if (!headers.getRequestHeaders().containsKey("Authorization")) {
			return Response.status(Status.UNAUTHORIZED).build();
		} else {
			Jwt jwt = JasonWebTokenUtil.decodificarJWT(token);
			LOG.debug("JWT: "+ jwt.toString());
//			Claims corpoJwt = JasonWebTokenUtil.obterCorpoJWT(token);
//			LOG.debug("JWt Subject: "+corpoJwt.getSubject());
			return Response.ok().entity(this.service.listar().toArray(new Categoria[0])).build();
		}
	}

	@Path("/save")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public Object save(Categoria categoria) {
		try {
			this.service.gravar(categoria);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Ocorreu um erro ao salvar. Consulte o log do servidor para averiguar a causa.").build();
		}
	}
}
