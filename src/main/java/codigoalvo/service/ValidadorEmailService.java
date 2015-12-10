package codigoalvo.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import codigoalvo.entity.Usuario;
import codigoalvo.entity.ValidadorEmail;

public interface ValidadorEmailService {

	ValidadorEmail gravar(ValidadorEmail entity) throws SQLException;
	void remover(ValidadorEmail entity) throws SQLException;
	void removerAnterioresData(Date data) throws SQLException;
	public ValidadorEmail buscarPorUuid(UUID uuid);
	public ValidadorEmail buscarPorEmail(String email);
	public Usuario confirmarRegistroUsuario(Usuario usuario, ValidadorEmail validadorEmail) throws SQLException;

}
