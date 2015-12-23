package codigoalvo.service;

import java.util.Date;

import javax.persistence.NoResultException;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;

import codigoalvo.entity.Usuario;
import codigoalvo.repository.UsuarioDao;
import codigoalvo.repository.UsuarioDaoJpa;
import codigoalvo.security.SegurancaUtil;
import codigoalvo.security.SegurancaUtilMd5;
import codigoalvo.util.EntityManagerUtil;
import codigoalvo.util.MsgParamUtil;

public class LoginServiceImpl implements LoginService {

	public static final Integer MAXIMO_TENTATIVAS_LOGIN = 5;
	private static final Logger LOG = Logger.getLogger(LoginServiceImpl.class);

	private UsuarioDao usuarioDao;
	SegurancaUtil segurancaUtil;

	public LoginServiceImpl() {
		LOG.debug("####################  construct  ####################");
		this.usuarioDao = new UsuarioDaoJpa(EntityManagerUtil.getEntityManager());
		this.segurancaUtil = new SegurancaUtilMd5();
	}

	@Override
	public Usuario efetuarLogin(String email, String senha) throws LoginException {
		Usuario usuario = null;
		try {
			usuario = this.usuarioDao.buscarPorEmail(email);
		} catch (NoResultException nre) {
			LOG.debug("Usuario não encontrado (email): " + email);
		}
		if (usuario == null || usuario.getId() == null) {
			LOG.debug("login.invalido");
			throw new LoginException("login.invalido");
		} else {
			if (!usuario.getAtivo()) {
				LOG.debug("login.usuarioDesativado");
				throw new LoginException("login.usuarioDesativado");
			} else if (usuario.getSenha().equals(this.segurancaUtil.criptografar(senha))) {
				usuario.setDataUltimoLogin(new Date());
				usuario.setTentativasLoginInvalido(0);
				usuario.setDataUltimaFalhaLogin(null);
				LOG.debug("login.sucesso");
				this.usuarioDao.atualizar(usuario);
				return usuario;
			} else if (usuario.getTentativasLoginInvalido() >= MAXIMO_TENTATIVAS_LOGIN) {
				usuario.setDataUltimaFalhaLogin(new Date());
				usuario.setAtivo(false);
				this.usuarioDao.atualizar(usuario);
				LOG.debug("login.senhaDesativada");
				throw new LoginException("login.senhaDesativada");
			} else {
				usuario.setTentativasLoginInvalido(usuario.getTentativasLoginInvalido() + 1);
				usuario.setDataUltimaFalhaLogin(new Date());
				this.usuarioDao.atualizar(usuario);
				String msg = "login.senhaInvalida";
				msg += MsgParamUtil.buildParams(usuario.getTentativasLoginInvalido().toString(),
						MAXIMO_TENTATIVAS_LOGIN.toString());
				LOG.debug(msg);
				throw new LoginException(msg);
			}
		}
	}

	@Override
	public Usuario buscarUsuarioPorEmailSenha(String email, String senha) throws LoginException {
		try {
			Usuario usuario = null;
			usuario = this.usuarioDao.buscarPorEmail(email);
			if (usuario == null || usuario.getId() == null) {
				throw new LoginException("login.invalido");
			} else if (!usuario.getSenha().equals(this.segurancaUtil.criptografar(senha))) {
				throw new LoginException("senha.atualInvalida");
			}
			return usuario;
		} catch (NoResultException nre) {
			LOG.debug("login.invalido" + email);
			throw new LoginException("login.invalido");
		}
	}

	@Override
	public Usuario alterarSenha(Usuario usuario, String senhaNova) throws LoginException {
		String senhaAtual = usuario.getSenha();
		usuario.setSenha(this.segurancaUtil.criptografar(senhaNova));
		try {
			this.usuarioDao.beginTransaction();
			this.usuarioDao.atualizar(usuario);
			this.usuarioDao.commit();
		} catch (Throwable exc) {
			usuario.setSenha(senhaAtual);
			this.usuarioDao.rollback();
			LOG.debug("senha.erroGravar", exc);
			throw new LoginException("senha.erroGravar");
		}
		return usuario;
	}

	@Override
	public Usuario alterarSenha(String email, String senhaAtual, String senhaNova) throws LoginException {
		Usuario usuario = null;
		usuario = buscarUsuarioPorEmailSenha(email, senhaAtual);
		return alterarSenha(usuario, senhaNova);
	}

}
