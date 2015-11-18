package codigoalvo.entity;

public enum UsuarioTipo {

	ADMIN("Administrador"), USER("Usuário");

	private String descricao;

	UsuarioTipo(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}
}
