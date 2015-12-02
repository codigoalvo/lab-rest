package codigoalvo.rest.util;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Resposta {
	private String mensagem;
	private Object entidade;

	public Resposta() {}

	public Resposta(String mensagem) {
		super();
		this.mensagem = mensagem;
	}

	public Resposta(Object entidade) {
		super();
		this.entidade = entidade;
	}

	public Resposta(String mensagem, Object entidade) {
		super();
		this.mensagem = mensagem;
		this.entidade = entidade;
	}

	public String getMensagem() {
		return this.mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Object getEntidade() {
		return this.entidade;
	}

	public void setEntidade(Object entidade) {
		this.entidade = entidade;
	}
}
