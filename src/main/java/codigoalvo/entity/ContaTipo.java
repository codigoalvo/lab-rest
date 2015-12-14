package codigoalvo.entity;

public enum ContaTipo {

	DINHEIRO('D', "Dinheiro"),
	DEBITO('B', "Débito"),
	CARTAO('C', "Crédito"),
	CHEQUE('Q', "Cheque"),
	OUTROS('O',	"Outros");

	private char id;
	private String descricao;

	ContaTipo(char key, String descricao) {
		this.id = key;
		this.descricao = descricao;
	}

	public static ContaTipo getTipo(Character id) {
		if (id == null) {
			return null;
		}
		for (ContaTipo position : ContaTipo.values()) {
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
