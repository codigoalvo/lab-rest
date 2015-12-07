package codigoalvo.rest.util;

import io.jsonwebtoken.ExpiredJwtException;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import codigoalvo.entity.UsuarioTipo;
import codigoalvo.security.JsonWebTokenUtil;
import codigoalvo.security.LoginToken;
import codigoalvo.util.Globals;
import codigoalvo.util.UsuarioUtil;

public class ResponseBuilderHelper {

	private static final Logger LOG = Logger.getLogger(ResponseBuilderHelper.class);

	public ResponseBuilderHelper() {
		LOG.debug("####################  construct  ####################");
	}

	public static String obterTokenDoCabecalhoHttp(HttpHeaders headers) {
		/*/
		for(String header : headers.getRequestHeaders().keySet()){
			LOG.debug("### Header ###   "+header+" = "+headers.getRequestHeaders().get(header));
		}
		//*/
		if (!headers.getRequestHeaders().containsKey("Authorization")) {
			LOG.debug("Token does not exists in headers!");
			return null;
		} else {
			String token = headers.getRequestHeaders().getFirst("Authorization");
			LOG.debug("RAW Token from headers: "+token);
			return token;
		}
	}

	public static ResponseBuilder verificarAutenticacao(String token) {
		return verificarAutenticacao(token, false);
	}

	/**
	 * @param token (token jwt em texto)
	 * @return null se o token estiver OK ou uma Response "ruim" caso contrário
	 */
	public static ResponseBuilder verificarAutenticacao(String token, boolean admin) {
		if (!Globals.isAuhenticationEnabled()) {
			return null;
		}
		if (token == null || token.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).entity(new Resposta("Token de autorização não encontrado!"));
		} else {
			try {
				if (!JsonWebTokenUtil.isValidToken(token)) {
					return Response.status(Status.UNAUTHORIZED).entity(new Resposta("Token de autorização inválido!"));
				}

				if (admin) {
					LoginToken loginToken = JsonWebTokenUtil.obterLoginToken(token);
					UsuarioTipo usuarioTipo = UsuarioUtil.decodeTipoFromHash(loginToken);
					if (UsuarioTipo.ADMIN != usuarioTipo) {
						return Response.status(Status.FORBIDDEN).entity(new Resposta("Usuário não é administrador!"));
					}
				}

				return null;
			} catch (ExpiredJwtException exc) {
				return Response.status(Status.UNAUTHORIZED).entity(new Resposta("Token de autorização expirado!"));
			}
		}
	}

	public static ResponseBuilder validarTokenCabecalhoHttp(HttpHeaders headers) {
		String token = obterTokenDoCabecalhoHttp(headers);
		ResponseBuilder resposta = verificarAutenticacao(token);
		return resposta;
	}

	public static void atualizarTokenNaRespostaSeNecessario(ResponseBuilder response, String token) {
		if (!Globals.isAuhenticationEnabled()) {
			return;
		}
		String tokenAtualizado = JsonWebTokenUtil.renovaTokenSeNecessario(token);
		if (!token.equals(tokenAtualizado)) {
			response.header("Authorization", tokenAtualizado);
		}
	}
}
