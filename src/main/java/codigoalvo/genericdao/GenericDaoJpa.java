package codigoalvo.genericdao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

public abstract class GenericDaoJpa<T> implements GenericDao<T> {

	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	private Class<T> getTypeClass() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	public T criar(final T entity) {
		this.entityManager.persist(entity);
		return entity;
	}

	@Override
	public void remover(final Object id) {
		this.entityManager.remove(this.getEntityManager().getReference(getTypeClass(), id));
	}

	@Override
	public T buscar(final Object id) {
		return this.entityManager.find(getTypeClass(), id);
	}

	@Override
	public T atualizar(final T entity) {
		return this.entityManager.merge(entity);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> listar() {
		return this.entityManager.createQuery(("FROM " + getTypeClass().getName())).getResultList();
	}

	@Override
	public T buscarPor(String campo, Object valor) {
		return buscarPor(campo, valor, "=", false);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T buscarPor(String campo, Object valor, String comparador, boolean caseSensitive) {
		String className = getTypeClass().getSimpleName();
		String alias = className.substring(0, 1).toLowerCase();
		StringBuilder jpql = new StringBuilder("from ");
		jpql.append(className).append(" ").append(alias).append(" where ");
		if (!caseSensitive) {
			jpql.append("upper(");
		}
		jpql.append(alias).append(".").append(campo);
		if (!caseSensitive) {
			jpql.append(")");
		}
		jpql.append(" ").append(comparador).append(" ");
		jpql.append(":valor");
		Logger.getLogger(GenericDaoJpa.class).debug("JPQL  -->  " + jpql.toString());
		Query query = entityManager.createQuery(jpql.toString());
		if (!caseSensitive) {
			query.setParameter("valor", valor.toString().toUpperCase());
		} else {
			query.setParameter("valor", valor);
		}
		T result = null;
		result = (T) query.getSingleResult();
		if (result == null) {
			Logger.getLogger(GenericDaoJpa.class)
					.debug(className + " (buscarPor=" + campo + "): " + valor + " ! Não encontrado !");
		}
		return result;
	}

	protected boolean emptyOrNull(String valor) {
		Logger.getLogger(GenericDaoJpa.class).debug(valor);
		return (valor == null || valor.trim().isEmpty());
	}

	@Override
	public void beginTransaction() {
		rollback();
		this.entityManager.getTransaction().begin();
	}

	@Override
	public void commit() {
		this.entityManager.getTransaction().commit();
	}

	@Override
	public void rollback() {
		try {
			this.entityManager.getTransaction().rollback();
		} catch (Exception exc) {}
	}

	@Override
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
