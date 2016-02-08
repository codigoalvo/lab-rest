package codigoalvo.rest.util;

import io.jsonwebtoken.ExpiredJwtException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import codigoalvo.entity.UsuarioTipo;
import codigoalvo.exceptions.RestException;
import codigoalvo.security.JsonWebTokenUtil;
import codigoalvo.security.LoginToken;
import codigoalvo.util.ErrosUtil;
import codigoalvo.util.Globals;
import codigoalvo.util.I18NUtil;
import codigoalvo.util.UsuarioUtil;

public class ResponseBuilderHelper {

	private static final Logger LOG = Logger.getLogger(ResponseBuilderHelper.class);

	public ResponseBuilderHelper() {
		LOG.trace("####################  construct  ####################");
	}

	public static String obterOrigemHostDoRequest(HttpServletRequest httpServletRequest) {
		String remoteHost = httpServletRequest.getRemoteHost();
		String remoteAddr = httpServletRequest.getRemoteAddr();
		int remotePort = httpServletRequest.getRemotePort();
		String origem = remoteHost + "(" + remoteAddr + ":" + remotePort + ")";
		LOG.debug(origem);
		return origem;
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

	public static void verificarAutenticacao(String token) throws RestException {
		verificarAutenticacao(token, false, false);
	}

	public static void verificarAutenticacao(String token, boolean admin) throws RestException {
		verificarAutenticacao(token, admin, false);
	}

	/**
	 * @param token (token jwt em texto)
	 * @return null se o token estiver OK ou uma Response "ruim" caso contrário
	 */
	public static void verificarAutenticacao(String token, boolean admin, boolean tokenDeEmail) throws RestException {
		if (!Globals.isAuhenticationEnabled()) {
			return;
		}
		if (token == null || token.isEmpty()) {
			throw new RestException(Response.status(Status.UNAUTHORIZED).entity(new Resposta("Token de autorização não encontrado!")));
		} else {
			try {
				if (!JsonWebTokenUtil.isValidToken(token)) {
					throw new RestException(Response.status(Status.UNAUTHORIZED).entity(new Resposta("Token de autorização inválido!")));
				}

				if (!tokenDeEmail) {
					LoginToken loginToken = JsonWebTokenUtil.obterLoginToken(token);
					if (loginToken == null || !loginToken.isValid()) {
						throw new RestException(Response.status(Status.UNAUTHORIZED).entity(new Resposta("Usuário do token de autorização inválido!")));
					}

					if (admin) {
						UsuarioTipo usuarioTipo = UsuarioUtil.decodeTipoFromHash(loginToken);
						if (UsuarioTipo.ADMIN != usuarioTipo) {
							throw new RestException(Response.status(Status.FORBIDDEN).entity(new Resposta("Usuário não é administrador!")));
						}
					}
				}

				return;
			} catch (ExpiredJwtException exc) {
				throw new RestException(Response.status(Status.UNAUTHORIZED).entity(new Resposta("Token de autorização expirado!")));
			}
		}
	}

	public static void validarTokenCabecalhoHttp(HttpHeaders headers) throws RestException {
		String token = obterTokenDoCabecalhoHttp(headers);
		verificarAutenticacao(token);
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

	public static ResponseBuilder montarResponseDoErro(Throwable exc) {
		if (exc instanceof RestException) {
			RestException rsexc = (RestException)exc;
			if (rsexc.getResponse() != null) {
				return rsexc.getResponse();
			}
		}
		String excMsg = ErrosUtil.getMensagemErro(exc);
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(excMsg));
	}

	public static ResponseBuilder respostaErroInterno(String menssagem) {
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new Resposta(I18NUtil.getMessage(menssagem)));
	}

}
