package codigoalvo.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import codigoalvo.entity.Usuario;
import codigoalvo.entity.UsuarioTipo;
import codigoalvo.entity.ValidadorEmail;
import codigoalvo.repository.UsuarioDao;
import codigoalvo.repository.UsuarioDaoJpa;
import codigoalvo.repository.ValidadorEmailDao;
import codigoalvo.repository.ValidadorEmailDaoJpa;
import codigoalvo.security.SegurancaUtil;
import codigoalvo.security.SegurancaUtilMd5;
import codigoalvo.util.EntityManagerUtil;

public class ValidadorEmailServiceImpl implements ValidadorEmailService {

	private ValidadorEmailDao validadorEmailDao;
	private SegurancaUtil segurancaUtil;
	private static final Logger LOG = Logger.getLogger(ValidadorEmailServiceImpl.class);

	public ValidadorEmailServiceImpl() {
		LOG.debug("####################  construct  ####################");
		this.validadorEmailDao = new ValidadorEmailDaoJpa(EntityManagerUtil.getEntityManager());
		this.segurancaUtil = new SegurancaUtilMd5();
	}

	@Override
	public ValidadorEmail gravar(ValidadorEmail validadorEmail) throws SQLException {

		try {
			this.validadorEmailDao.beginTransaction();
			if (validadorEmail.getId() == null) {
				this.validadorEmailDao.criar(validadorEmail);
			} else {
				this.validadorEmailDao.atualizar(validadorEmail);
			}
			this.validadorEmailDao.commit();
			return validadorEmail;
		} catch (Throwable exc) {
			LOG.error(exc);
			this.validadorEmailDao.rollback();
			throw new SQLException(exc);
		}

	}

	@Override
	public void remover(ValidadorEmail validadorEmail) throws SQLException {
		try {
			this.validadorEmailDao.beginTransaction();
			this.validadorEmailDao.remover(validadorEmail.getId());
			this.validadorEmailDao.commit();
		} catch (Throwable exc) {
			this.validadorEmailDao.rollback();
			throw new SQLException(exc);
		}

	}

	@Override
	public void removerAnterioresData(Date data) throws SQLException {
		try {
			this.validadorEmailDao.beginTransaction();
			this.validadorEmailDao.removerAnterioresData(data);
			this.validadorEmailDao.commit();
		} catch (Throwable exc) {
			this.validadorEmailDao.rollback();
			throw new SQLException(exc);
		}
	}

	@Override
	public ValidadorEmail buscarPorUuid(UUID uuid) {
		ValidadorEmail validadorEmail = null;
		try {
			validadorEmail = this.validadorEmailDao.buscar(uuid);
		} catch (NoResultException nre) {
			LOG.debug("ValidadorEmail não encontrado (uuid): " + uuid);
		}
		return validadorEmail;
	}

	@Override
	public ValidadorEmail buscarPorEmail(String email) {
		ValidadorEmail validadorEmail = null;
		try {
			validadorEmail = this.validadorEmailDao.buscarPorEmail(email);
		} catch (NoResultException nre) {
			LOG.debug("ValidadorEmail não encontrado (email): " + email);
		}
		return validadorEmail;
	}

	@Override
	public Usuario confirmarRegistroUsuario(Usuario usuario, ValidadorEmail validadorEmail) throws SQLException {
		//Ambos os DAO's devem ter o mesmo EntityMaganer para poderem ter ações dentro da mesma transaction;
		EntityManager entityManager = this.validadorEmailDao.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		UsuarioDao usuarioDao = new UsuarioDaoJpa(entityManager);
		Usuario usuarioGravado = null;
		try {
			transaction.rollback();
		} catch (Exception exc) {}
		try {
			transaction.begin();
			usuario.setSenha(this.segurancaUtil.criptografar(usuario.getSenha()));
			usuario.setTipo(UsuarioTipo.USER);
			usuarioGravado = usuarioDao.criar(usuario);
			LOG.debug("Usuário criado com sucesso! "+usuarioGravado.getId());
			this.validadorEmailDao.remover(validadorEmail.getId());
			LOG.debug("Token validador de registro de email removido com sucesso!");
			transaction.commit();
			return usuarioGravado;
		} catch (Throwable exc) {
			transaction.rollback();
			throw new SQLException(exc);
		}
	}

}
