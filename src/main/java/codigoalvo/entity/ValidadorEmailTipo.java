package codigoalvo.entity;

public enum ValidadorEmailTipo {

	REGISTRO('R', "Registrar"),
	SENHA('S', "Alterar Senha"),
	EMAIL('E', "Alterar Email");

	private char id;
	private String descricao;

	ValidadorEmailTipo(char key, String descricao) {
		this.id = key;
		this.descricao = descricao;
	}

	public static ValidadorEmailTipo getTipo(Character id) {
		if (id == null) {
			return null;
		}
		for (ValidadorEmailTipo position : ValidadorEmailTipo.values()) {
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
