package codigoalvo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(indexes = {	@Index(name = "idx_validador_id", columnList = "id", unique = true),
					@Index(name = "idx_validador_email", columnList = "email", unique = true),
					@Index(name = "idx_validador_data", columnList = "data", unique = false)})
public class ValidadorEmail implements Serializable {

	private static final long serialVersionUID = 4502437052474880802L;

	@Id
	@Column(columnDefinition = "UUID")
	//@org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
	private UUID id;

	@NotNull
	@NotBlank
	@Length(max = 250)
	@Email
	private String email;

	@NotNull
	@Length(max = 150)
	private String origem;

	@NotNull
	private Date data;

	@NotNull
	private Character tipo;

	@XmlTransient
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_validador_usuario"))
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Usuario usuario;

	public ValidadorEmail() {
	}

	public ValidadorEmail(String email, Date data, String origem, ValidadorEmailTipo tipo) {
		super();
		this.email = email;
		this.data = data;
		this.origem = origem;
		setTipo(tipo);
	}

	public ValidadorEmail(String email, Date data, String origem, ValidadorEmailTipo tipo, Usuario usuario) {
		super();
		this.email = email;
		this.data = data;
		this.origem = origem;
		this.usuario = usuario;
		setTipo(tipo);
	}

	@Override
	public String toString() {
		return "ValidadorEmail [id=" + id + ", email=" + email + ", origem=" + origem + ", data=" + data +
				", tipo=" + tipo + (usuario==null?"":", usuarioId="+usuario.getId())+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ValidadorEmail other = (ValidadorEmail) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public ValidadorEmailTipo getTipo() {
		return ValidadorEmailTipo.getTipo(this.tipo);
	}

	public void setTipo(ValidadorEmailTipo tipo) {
		if (tipo == null) {
			this.tipo = null;
		} else {
			this.tipo = tipo.getId();
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
