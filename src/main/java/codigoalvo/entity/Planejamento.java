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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import codigoalvo.util.DateUtil;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(indexes = {	@Index(name = "idx_planejamento_unique", columnList = "usuario_id, categoria_id, periodo", unique = true) })
public class Planejamento implements Serializable {

	private static final long serialVersionUID = -5030476348119403866L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@XmlTransient
	@NotNull
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_planejamento_usuario"))
	@OnDelete(action=OnDeleteAction.CASCADE)
	private	Usuario usuario;

	@NotNull
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_planejamento_categoria"))
	@OnDelete(action=OnDeleteAction.CASCADE)
	private	Categoria categoria;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date periodo;

	@NotNull
	private BigDecimal valor;

	public Planejamento() {
	}

	@Override
	public String toString() {
		return "Planejamento [id=" + id + ", usuario=" + usuario + ", categoria=" + categoria + ", periodo=" + periodo
				+ ", valor=" + valor + "]";
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
		Planejamento other = (Planejamento) obj;
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

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Date getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Date periodo) {
		this.periodo = periodo;
	}

	public void setPeriodo(int mes, int ano) {
		this.periodo = DateUtil.primeiroDiaDoMes(mes, ano).getTime();
	}

	@SuppressWarnings("deprecation")
	public Integer getPeriodoMes() {
		if (periodo == null) {
			return null;
		}
		return this.periodo.getMonth();
	}

	@SuppressWarnings("deprecation")
	public Integer getPeriodoAno() {
		if (periodo == null) {
			return null;
		}
		return this.periodo.getYear();
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public void setValor(double valor) {
		this.valor = BigDecimal.valueOf(valor);
	}

	public Double getValorDouble() {
		if (this.valor == null) {
			return null;
		}
		return this.valor.doubleValue();
	}

}
