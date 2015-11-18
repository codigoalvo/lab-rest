package codigoalvo.service;

import java.sql.SQLException;
import java.util.List;
import codigoalvo.entity.Categoria;

public interface CategoriaService {

	Categoria gravar(Categoria entity) throws SQLException;
	void remover(Categoria categoria) throws SQLException;
	void removerPorId(Integer id) throws SQLException;
	Categoria buscar(Integer id);
	List<Categoria> listar();
	public Categoria buscarPorNome(String nome);

}
