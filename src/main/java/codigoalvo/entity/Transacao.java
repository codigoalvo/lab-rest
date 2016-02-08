package codigoalvo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Transacao implements Serializable {

	private static final long serialVersionUID = 9078636796732067248L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@XmlTransient
	@NotNull
	@ManyToOne(cascade=CascadeType.ALL, optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_transacao_usuario"))
	@OnDelete(action=OnDeleteAction.CASCADE)
	private	Usuario usuario;

	@NotNull
	@ManyToOne(cascade=CascadeType.ALL, optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_transacao_conta"))
	@OnDelete(action=OnDeleteAction.CASCADE)
	private	Conta conta;

	@NotNull
	@ManyToOne(cascade=CascadeType.ALL, optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_transacao_categoria"))
	@OnDelete(action=OnDeleteAction.CASCADE)
	private	Categoria categoria;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date dataTransacao;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date dataPagamento;

	@NotNull
	private Character tipo;

	@NotNull
	@NotBlank
	@Length(max = 80)
	private	String descricao;

	@Length(max = 250)
	private	String detalhes;

	@NotNull
	private BigDecimal valor;

	public Transacao() {
		super();
	}

	@Override
	public String toString() {
		return "Transacao [id=" + id + ", dataTransacao=" + dataTransacao + ", dataPagamento=" + dataPagamento
				+ ", tipo=" + tipo + ", descricao=" + descricao + ", valor=" + valor
				+ ", categoriaId=" + (categoria==null?"null":categoria.getId())
				+ ", contaId=" + (conta==null?"null":conta.getId())
				+ ", usuarioId=" + (usuario==null?"null":usuario.getId())
				+ "]";
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
		Transacao other = (Transacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Date getDataTransacao() {
		return dataTransacao;
	}

	public void setDataTransacao(Date dataTransacao) {
		this.dataTransacao = dataTransacao;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public TransacaoTipo getTipo() {
		return TransacaoTipo.getTipo(this.tipo);
	}

	public void setTipo(TransacaoTipo tipo) {
		if (tipo == null) {
			this.tipo = null;
		} else {
			this.tipo = tipo.getId();
		}
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
