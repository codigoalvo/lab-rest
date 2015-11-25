package codigoalvo.rest.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import codigoalvo.security.JasonWebTokenUtil;
import codigoalvo.util.Message;

public class ResponseBuilderHelper {

	private static final Logger LOG = Logger.getLogger(ResponseBuilderHelper.class);
	private static final boolean AUTHENTICATION_ENABLED = true;

	public ResponseBuilderHelper() {
		LOG.debug("####################  construct  ####################");
	}

	public static String getTokenFromHttpHeaders(HttpHeaders headers) {
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

	/**
	 * @param token (String raw jwt token)
	 * @return null if token is OK or a bad Response otherwise
	 */
	public static ResponseBuilder checkAuthentication(String token) {
		if (!AUTHENTICATION_ENABLED) {
			return null;
		}
		if (token == null || token.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).entity(new Message("Authorization token is missing!"));
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
					return Response.status(Status.UNAUTHORIZED).entity(new Message("Invalid authorization token!"));
				}

				return null;
			} catch (ExpiredJwtException exc) {
				return Response.status(Status.UNAUTHORIZED).entity(new Message("Authorization token is expired!"));
			}
		}
	}

	public static ResponseBuilder validateHeaderToken(HttpHeaders headers) {
		String token = getTokenFromHttpHeaders(headers);
		ResponseBuilder resposta = checkAuthentication(token);
		return resposta;
	}
}
