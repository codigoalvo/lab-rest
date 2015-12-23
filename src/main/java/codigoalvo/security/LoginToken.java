package codigoalvo.security;

import java.io.Serializable;

import codigoalvo.entity.UsuarioTipo;

public class LoginToken implements Serializable {

	private static final long serialVersionUID = -8107572215093548341L;

	private Integer id;
	private String nome;
	private String email;
	private String apelido;
	private UsuarioTipo tipo;
	private String origem;
	private String hash;

	public LoginToken() {
	}

	public LoginToken(LoginToken toCopy) {
		this(toCopy.getId(), toCopy.getEmail(), toCopy.getNome(), toCopy.getApelido(), toCopy.getTipo(), toCopy.getOrigem());
		this.hash = toCopy.getHash();
	}

	public LoginToken(Integer id, String email, String nome, String apelido, UsuarioTipo tipo, String origem) {
		super();
		this.id = id;
		this.email = email;
		this.nome = nome;
		this.apelido = apelido;
		this.tipo = tipo;
		this.origem = origem;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getApelido() {
		return this.apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
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

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getHash() {
		return this.hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String toHashKey() {
		return ""+this.id+"#"+this.email+"#"+this.tipo.name();
	}

	public boolean isValid() {
		if (this.id == null  || this.email == null  ||  this.tipo == null  ||  this.origem == null  ||  this.hash == null) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "LoginToken [id=" + this.id + ", email=" + this.email + ", nome=" + this.nome + ", apelido=" + this.apelido + ", tipo=" + this.tipo
				 + ", origem=" + this.origem + ", extp=" + this.hash + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.apelido == null) ? 0 : this.apelido.hashCode());
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
		if (this.email == null) {
			if (other.email != null)
				return false;
		} else if (!this.email.equals(other.email))
			return false;
		return true;
	}

}
