package codigoalvo.entity;

public enum PagamentoTipo {

	DINHEIRO('D', "Dinheiro"),
	DEBITO('B', "Débito"),
	CARTAO('C', "Crédito"),
	CHEQUE('Q', "Cheque"),
	OUTROS('O',	"Outros");

	private char id;
	private String descricao;

	PagamentoTipo(char key, String descricao) {
		this.id = key;
		this.descricao = descricao;
	}

	public static PagamentoTipo getTipo(Character id) {
		if (id == null) {
			return null;
		}
		for (PagamentoTipo position : PagamentoTipo.values()) {
			if (id.equals(position.getId())) {
				return position;
			}
		}
		throw new IllegalArgumentException("No matching type for id " + id);
	}

	public char getId() {
		return this.id;
	}

	public void setId(char id) {
		this.id = id;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
