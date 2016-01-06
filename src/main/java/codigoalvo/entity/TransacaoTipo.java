package codigoalvo.entity;

public enum TransacaoTipo {

	RECEITA('R', "Receita"),
	DESPESA('D', "Despesa");

	private char id;
	private String descricao;

	TransacaoTipo(char key, String descricao) {
		this.id = key;
		this.descricao = descricao;
	}

	public static TransacaoTipo getTipo(Character id) {
		if (id == null) {
			return null;
		}
		for (TransacaoTipo position : TransacaoTipo.values()) {
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
