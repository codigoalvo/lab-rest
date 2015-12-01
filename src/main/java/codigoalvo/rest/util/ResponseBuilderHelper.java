package codigoalvo.rest.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import codigoalvo.entity.UsuarioTipo;
import codigoalvo.security.JasonWebTokenUtil;
import codigoalvo.security.LoginToken;
import codigoalvo.util.JsonUtil;
import codigoalvo.util.Message;
import codigoalvo.util.UsuarioTipoUtil;

public class ResponseBuilderHelper {

	private static final Logger LOG = Logger.getLogger(ResponseBuilderHelper.class);
	private static final boolean AUTHENTICATION_ENABLED = true;

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
		if (!AUTHENTICATION_ENABLED) {
			return null;
		}
		if (token == null || token.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).entity(new Message("Token de autorização não encontrado!"));
		} else {
			try {
				//*/
				@SuppressWarnings("rawtypes")
				Jwt jwt = JasonWebTokenUtil.decodificarJWT(token);
				LOG.debug("Decoded Token: "+ jwt.toString());
				//*/

				Claims corpoJwt = JasonWebTokenUtil.obterCorpoJWT(token);
				String issuer = corpoJwt.getIssuer();
				LOG.debug("Token Issuer: "+issuer);
				if (!JasonWebTokenUtil.ISSUER.equals(issuer)) {
					return Response.status(Status.UNAUTHORIZED).entity(new Message("Token de autorização inválido!"));
				}

				if (admin) {
					String usuarioJson = ""+corpoJwt.get("usuario");
					LoginToken usuario = JsonUtil.fromJson(usuarioJson, LoginToken.class);
					UsuarioTipo usuarioTipo = UsuarioTipoUtil.decodeTipo(usuario.getExtp());
					if (UsuarioTipo.ADMIN != usuarioTipo) {
						return Response.status(Status.FORBIDDEN).entity(new Message("Usuário não é administrador!"));
					}
				}

				return null;
			} catch (ExpiredJwtException exc) {
				return Response.status(Status.UNAUTHORIZED).entity(new Message("Token de autorização expirado!"));
			}
		}
	}

	public static ResponseBuilder validarTokenCabecalhoHttp(HttpHeaders headers) {
		String token = obterTokenDoCabecalhoHttp(headers);
		ResponseBuilder resposta = verificarAutenticacao(token);
		return resposta;
	}

	public static void atualizarTokenNaRespostaSeNecessario(ResponseBuilder resposta, String token) {
		if (!AUTHENTICATION_ENABLED) {
			return;
		}
		Claims corpoJwt = JasonWebTokenUtil.obterCorpoJWT(token);
		if (JasonWebTokenUtil.precisaRenovar(corpoJwt)) {
			String tokenAtualizado = JasonWebTokenUtil.renovaToken(corpoJwt);
			resposta.header("Authorization", tokenAtualizado);
		}
	}
}
