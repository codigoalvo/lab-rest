package codigoalvo.repository;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import codigoalvo.entity.ValidadorEmail;
import codigoalvo.genericdao.GenericDaoJpa;

public class ValidadorEmailDaoJpa extends GenericDaoJpa<ValidadorEmail> implements ValidadorEmailDao {

	public ValidadorEmailDaoJpa(EntityManager entityManager) {
		setEntityManager(entityManager);
		Logger.getLogger(ValidadorEmailDaoJpa.class).debug("####################  construct  ####################");
	}

	@Override
	public ValidadorEmail criar(final ValidadorEmail entity) {
		entity.setId(UUID.randomUUID());
		getEntityManager().persist(entity);
		return entity;
	}

	@Override
	public ValidadorEmail buscarPorEmail(String email) {
		if (emptyOrNull(email)) {
			return null;
		}
		return buscarPor("email", email.trim());
	}

	@Override
	public void removerAnterioresData(Date data) {
		Query query = getEntityManager().createQuery("DELETE FROM ValidadorEmail v WHERE v.data < :data");
		query.setParameter("data", data);
		query.executeUpdate();
	}

}
