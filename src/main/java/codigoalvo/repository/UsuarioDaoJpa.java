package codigoalvo.repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import codigoalvo.entity.Usuario;
import codigoalvo.genericdao.GenericDaoJpa;

public class UsuarioDaoJpa extends GenericDaoJpa<Usuario> implements UsuarioDao {

	public UsuarioDaoJpa(EntityManager entityManager) {
		setEntityManager(entityManager);
		Logger.getLogger(UsuarioDaoJpa.class).debug("####################  construct  ####################");
	}

	@Override
	public Usuario buscarPorEmail(String email) {
		if (emptyOrNull(email)) {
			return null;
		}
		return buscarPor("email", email.trim());
	}

	@Override
	public String buscarSenhaDoUsuario(Integer usuarioId) {
		String jpql = "SELECT u.senha FROM Usuario u WHERE u.id = :usuarioId";
		TypedQuery<String> query = getEntityManager().createQuery(jpql, String.class);
		query.setParameter("usuarioId", usuarioId);
		return query.getSingleResult();
	}

}
