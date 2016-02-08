package codigoalvo.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
	private UsuarioDao usuarioDao;

	private SegurancaUtil segurancaUtil;
	private static final Logger LOG = Logger.getLogger(ValidadorEmailServiceImpl.class);

	public ValidadorEmailServiceImpl() {
		LOG.trace("####################  construct  ####################");
		this.validadorEmailDao = new ValidadorEmailDaoJpa(EntityManagerUtil.getEntityManager());
		//Ambos os DAO's devem ter o mesmo EntityMaganer para poderem ter ações dentro da mesma transaction;
		this.usuarioDao = new UsuarioDaoJpa(this.validadorEmailDao.getEntityManager());
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
			LOG.debug("gravar.rollback");
			this.validadorEmailDao.getEntityManager().clear();
			LOG.debug("gravar.dao.em.clear");
			throw new SQLException(exc);
		}

	}

	@Override
	public void remover(ValidadorEmail validadorEmail) throws SQLException {
		try {
			this.validadorEmailDao.beginTransaction();
			this.validadorEmailDao.removerPorId(validadorEmail.getId());
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
			LOG.error("Houve um erro ao tentar limpar os validadores de registros vencidos!", exc);
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
	public List<ValidadorEmail> buscarRegistrosDepoisDe(Date data) {
		List<ValidadorEmail> result;
		try {
			LOG.debug("Buscando registros depois de: "+data.toString());
			result = validadorEmailDao.buscarRegistrosDepoisDe(data);
		} catch (Exception exc) {
			LOG.error(exc);
			result = new ArrayList<ValidadorEmail>();
		}
		return result;
	}

	@Override
	public Usuario buscarUsuarioPorEmail(String email) {
		Usuario usuario = null;
		try {
			usuario = this.usuarioDao.buscarPorEmail(email);
		} catch (NoResultException nre) {
			LOG.debug("Usuario não encontrado (email): " + email);
		}
		return usuario;
	}

	@Override
	public Usuario confirmarRegistroUsuario(Usuario usuario, ValidadorEmail validadorEmail) throws SQLException {
		EntityTransaction transaction = pegarTransacaoSincronizada();
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
			this.validadorEmailDao.removerPorId(validadorEmail.getId());
			LOG.debug("Token validador de registro de email removido com sucesso!");
			transaction.commit();
			return usuarioGravado;
		} catch (Throwable exc) {
			transaction.rollback();
			LOG.debug("gravar.rollback");
			this.validadorEmailDao.getEntityManager().clear();
			LOG.debug("gravar.dao.em.clear");
			throw new SQLException(exc);
		}
	}

	@Override
	public Usuario alterarSenhaUsuario(Usuario usuario, ValidadorEmail validadorEmail, String novaSenha) throws SQLException {
		EntityTransaction transaction = pegarTransacaoSincronizada();
		Usuario usuarioGravado = null;
		try {
			transaction.rollback();
		} catch (Exception exc) {}
		try {
			transaction.begin();
			usuario.setSenha(this.segurancaUtil.criptografar(novaSenha));
			usuarioGravado = usuarioDao.atualizar(usuario);
			LOG.debug("Usuário criado com sucesso! "+usuarioGravado.getId());
			this.validadorEmailDao.removerPorId(validadorEmail.getId());
			LOG.debug("Token validador de registro de email removido com sucesso!");
			transaction.commit();
			return usuarioGravado;
		} catch (Throwable exc) {
			transaction.rollback();
			LOG.debug("gravar.rollback");
			this.validadorEmailDao.getEntityManager().clear();
			LOG.debug("gravar.dao.em.clear");
			throw new SQLException(exc);
		}
	}

	private EntityTransaction pegarTransacaoSincronizada() {
		if (this.usuarioDao.getEntityManager() != this.validadorEmailDao.getEntityManager()) {
			this.usuarioDao.setEntityManager(this.validadorEmailDao.getEntityManager());
		}
		return this.usuarioDao.getEntityManager().getTransaction();
	}

}
