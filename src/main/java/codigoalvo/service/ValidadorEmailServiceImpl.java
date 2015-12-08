package codigoalvo.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import codigoalvo.entity.ValidadorEmail;
import codigoalvo.repository.ValidadorEmailDao;
import codigoalvo.repository.ValidadorEmailDaoJpa;
import codigoalvo.util.EntityManagerUtil;

public class ValidadorEmailServiceImpl implements ValidadorEmailService {

	private ValidadorEmailDao dao;
	private static final Logger LOG = Logger.getLogger(ValidadorEmailServiceImpl.class);

	public ValidadorEmailServiceImpl() {
		LOG.debug("####################  construct  ####################");
		this.dao = new ValidadorEmailDaoJpa(EntityManagerUtil.getEntityManager());
	}

	@Override
	public ValidadorEmail gravar(ValidadorEmail validadorEmail) throws SQLException {

		try {
			this.dao.beginTransaction();
			if (validadorEmail.getId() == null) {
				this.dao.criar(validadorEmail);
			} else {
				this.dao.atualizar(validadorEmail);
			}
			this.dao.commit();
			return validadorEmail;
		} catch (Throwable exc) {
			LOG.error(exc);
			this.dao.rollback();
			throw new SQLException(exc);
		}

	}

	@Override
	public void remover(ValidadorEmail validadorEmail) throws SQLException {
		try {
			this.dao.beginTransaction();
			this.dao.remover(validadorEmail.getId());
			this.dao.commit();
		} catch (Throwable exc) {
			this.dao.rollback();
			throw new SQLException(exc);
		}

	}

	@Override
	public void removerAnterioresData(Date data) throws SQLException {
		try {
			this.dao.beginTransaction();
			this.dao.removerAnterioresData(data);
			this.dao.commit();
		} catch (Throwable exc) {
			this.dao.rollback();
			throw new SQLException(exc);
		}
	}

	@Override
	public ValidadorEmail buscarPorUuid(UUID uuid) {
		ValidadorEmail validadorEmail = null;
		try {
			validadorEmail = this.dao.buscar(uuid);
		} catch (NoResultException nre) {
			LOG.debug("ValidadorEmail não encontrado (uuid): " + uuid);
		}
		return validadorEmail;
	}

	@Override
	public ValidadorEmail buscarPorEmail(String email) {
		ValidadorEmail validadorEmail = null;
		try {
			validadorEmail = this.dao.buscarPorEmail(email);
		} catch (NoResultException nre) {
			LOG.debug("ValidadorEmail não encontrado (email): " + email);
		}
		return validadorEmail;
	}

}
