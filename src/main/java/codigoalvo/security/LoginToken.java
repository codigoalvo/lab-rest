package codigoalvo.security;

import java.io.Serializable;

import codigoalvo.entity.UsuarioTipo;

public class LoginToken implements Serializable {

	private static final long serialVersionUID = -8107572215093548341L;

	private Integer id;
	private String login;
	private String nome;
	private String email;
	private UsuarioTipo tipo;
	private String hash;

	public LoginToken() {
	}

	public LoginToken(LoginToken toCopy) {
		this(toCopy.getId(), toCopy.getLogin(), toCopy.getNome(), toCopy.getEmail(), toCopy.getTipo());
	}

	public LoginToken(Integer id, String login, String nome, String email, UsuarioTipo tipo) {
		super();
		this.id = id;
		this.login = login;
		this.nome = nome;
		this.email = email;
		this.tipo = tipo;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getHash() {
		return this.hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String toHashKey() {
		return ""+this.id+"#"+this.login+"#"+this.tipo.name();
	}

	@Override
	public String toString() {
		return "LoginToken [id=" + this.id + ", login=" + this.login + ", nome=" + this.nome + ", email=" + this.email + ", tipo=" + this.tipo
				+ ", extp=" + this.hash + "]";
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
