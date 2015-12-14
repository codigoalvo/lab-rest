package codigoalvo.service;

import java.sql.SQLException;
import java.util.List;
import codigoalvo.entity.Conta;

public interface ContaService {

	Conta gravar(Conta entity) throws SQLException;
	void remover(Conta entity) throws SQLException;
	void removerPorId(Integer id) throws SQLException;
	Conta buscar(Integer usuarioId, Integer categoriaId);
	List<Conta> listar(Integer usuarioId);
	public Conta buscarPorNome(String nome);

}
