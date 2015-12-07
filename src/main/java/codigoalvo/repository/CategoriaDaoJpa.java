package codigoalvo.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

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

	@Override
	public List<Categoria> categoriasDoUsuario(int usuarioId) {
		TypedQuery<Categoria> query = getEntityManager().createQuery("FROM Categoria c WHERE c.usuario.id = :usuarioId", Categoria.class);
		query.setParameter("usuarioId", usuarioId);
		return query.getResultList();
	}

	@Override
	public Categoria categoriaDoUsuario(Integer usuarioId, Integer categoriaId) {
		TypedQuery<Categoria> query = getEntityManager().createQuery("FROM Categoria c WHERE c.usuario.id = :usuarioId and c.id = :categoriaId" , Categoria.class);
		query.setParameter("usuarioId", usuarioId);
		query.setParameter("categoriaId", categoriaId);
		try {
			return query.getSingleResult();
		} catch (NoResultException exc) {
			return null;
		}
	}

}
