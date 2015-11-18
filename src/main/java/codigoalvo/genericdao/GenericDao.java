package codigoalvo.genericdao;

import java.util.List;

import javax.persistence.EntityManager;

public interface GenericDao<T> {

	T criar(T entity);
	void remover(Object id);
	T buscar(Object id);
	T atualizar(T entity);
	List<T> listar();
	T buscarPor(String campo, Object valor);
	T buscarPor(String campo, Object valor, String comparador, boolean caseSensitive);
	void beginTransaction();
	void commit();
	void rollback();
	EntityManager getEntityManager();
	void setEntityManager(EntityManager entityManager);

}
