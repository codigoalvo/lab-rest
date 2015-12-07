package codigoalvo.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(indexes = {	@Index(name = "pagamento_codigo", columnList = "codigo, usuario_id", unique = true),
					@Index(name = "pagamento_nome", columnList = "nome, usuario_id", unique = true)})
public class Pagamento implements Serializable {

	private static final long serialVersionUID = 919312467078802157L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private	Integer id;

	@NotNull
	@NotBlank
	@Length(max = 10)
	private	String codigo;

	@NotNull
	@NotBlank
	@Length(max = 100)
	private	String nome;

	@NotNull
	private Character tipo;

	private Integer diaFechamento;

	private Integer diaPagamento;

	@XmlTransient
	@ManyToOne
	private	Usuario usuario;

	public Pagamento() {}

	public Pagamento(String codigo, String nome, PagamentoTipo tipo) {
		super();
		this.codigo = codigo;
		this.nome = nome;
		setTipo(tipo);
	}

	@Override
	public String toString() {
		return this.codigo;
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
		Pagamento other = (Pagamento) obj;
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

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public PagamentoTipo getTipo() {
		return PagamentoTipo.getTipo(this.tipo);
	}

	public void setTipo(PagamentoTipo tipo) {
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

}
