package codigoalvo.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(indexes = {	@Index(name = "idx_conta_nome", columnList = "usuario_id, nome", unique = true)})
public class Conta implements Serializable {

	private static final long serialVersionUID = 919312467078802157L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private	Integer id;

	@NotNull
	@NotBlank
	@Length(max = 40)
	private	String nome;

	@NotNull
	private Character tipo;

	private Integer diaFechamento;

	private Integer diaPagamento;

	@XmlTransient
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_conta_usuario"))
	@OnDelete(action=OnDeleteAction.CASCADE)
	private	Usuario usuario;

	private Date dataInativo;

	public Conta() {}

	public Conta(String nome, ContaTipo tipo) {
		super();
		this.nome = nome;
		setTipo(tipo);
	}

	@Override
	public String toString() {
		return this.nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
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
		Conta other = (Conta) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ContaTipo getTipo() {
		return ContaTipo.getTipo(this.tipo);
	}

	public void setTipo(ContaTipo tipo) {
		if (tipo == null) {
			this.tipo = null;
		} else {
			this.tipo = tipo.getId();
		}
	}

	public Integer getDiaFechamento() {
		return this.diaFechamento;
	}

	public void setDiaFechamento(Integer diaFechamento) {
		this.diaFechamento = diaFechamento;
	}

	public Integer getDiaPagamento() {
		return this.diaPagamento;
	}

	public void setDiaPagamento(Integer diaPagamento) {
		this.diaPagamento = diaPagamento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@XmlElement
	public Boolean getAtivo() {
		return this.dataInativo == null || this.dataInativo.after(new Date());
	}

	@XmlElement
	public void setAtivo(Boolean ativo) {
		if (ativo) {
			this.dataInativo = null;
		} else {
			this.dataInativo = new Date();
		}
	}

	public Date getDataInativo() {
		return dataInativo;
	}

	public void setDataInativo(Date dataInativo) {
		this.dataInativo = dataInativo;
	}

}
