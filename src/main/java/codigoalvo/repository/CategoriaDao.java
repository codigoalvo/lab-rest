package codigoalvo.repository;

import java.util.List;

import codigoalvo.entity.Categoria;
import codigoalvo.genericdao.GenericDao;

public interface CategoriaDao extends GenericDao<Categoria> {

	public Categoria buscarPorNome(String nome);
	public List<Categoria> categoriasDoUsuario(int usuarioId);
	public Categoria categoriaDoUsuario(Integer usuarioId, Integer categoriaId);

}
