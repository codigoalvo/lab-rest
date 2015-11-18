package codigoalvo.repository;

import codigoalvo.entity.Categoria;
import codigoalvo.genericdao.GenericDao;

public interface CategoriaDao extends GenericDao<Categoria> {

	public Categoria buscarPorNome(String nome);

}
