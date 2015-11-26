package codigoalvo.security;

import java.io.Serializable;

import codigoalvo.entity.UsuarioTipo;

public class LoginToken implements Serializable {

	private static final long serialVersionUID = -8107572215093548341L;

	private String login;
	private String nome;
	private String email;
	private UsuarioTipo tipo;

	public LoginToken() {
	}

	public LoginToken(String login, String nome, String email, UsuarioTipo tipo) {
		super();
		this.login = login;
		this.nome = nome;
		this.setEmail(email);
		this.setTipo(tipo);
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UsuarioTipo getTipo() {
		return this.tipo;
	}

	public void setTipo(UsuarioTipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "LoginToken [login=" + this.login + ", nome=" + this.nome + ", email=" + this.email + ", tipo="
				+ this.tipo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.login == null) ? 0 : this.login.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoginToken other = (LoginToken) obj;
		if (this.login == null) {
			if (other.login != null)
				return false;
		} else if (!this.login.equals(other.login))
			return false;
		return true;
	}

}
