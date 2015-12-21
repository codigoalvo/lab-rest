package codigoalvo.exceptions;

import javax.ws.rs.core.Response.ResponseBuilder;

public class RestException extends Exception {

	private static final long serialVersionUID = 6688585934283568587L;

	private ResponseBuilder response;

	public RestException(ResponseBuilder response) {
		super();
		this.setResponse(response);
	}

	public RestException(ResponseBuilder response, String message) {
		super(message);
		this.setResponse(response);
	}

	public RestException(ResponseBuilder response, Throwable cause) {
		super(cause);
		this.setResponse(response);
	}

	public RestException(ResponseBuilder response, String message, Throwable cause) {
		super(message, cause);
		this.setResponse(response);
	}

	public ResponseBuilder getResponse() {
		return response;
	}

	public void setResponse(ResponseBuilder response) {
		this.response = response;
	}

}
