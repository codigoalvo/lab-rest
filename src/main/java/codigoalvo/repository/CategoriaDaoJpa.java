package codigoalvo.repository;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import codigoalvo.entity.Categoria;
import codigoalvo.genericdao.GenericDaoJpa;

public class CategoriaDaoJpa extends GenericDaoJpa<Categoria> implements CategoriaDao {

	public CategoriaDaoJpa(EntityManager entityManager) {
		setEntityManager(entityManager);
		Logger.getLogger(CategoriaDaoJpa.class).debug("####################  construct  ####################");
	}

	@Override
	public Categoria buscarPorNome(String nome) {
		if (emptyOrNull(nome)) {
			return null;
		}
		return buscarPor("nome", nome.trim());
	}

}
