package codigoalvo.service;

import java.util.Date;

import javax.persistence.NoResultException;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;

import codigoalvo.entity.Usuario;
import codigoalvo.repository.UsuarioDao;
import codigoalvo.repository.UsuarioDaoJpa;
import codigoalvo.util.EntityManagerUtil;
import codigoalvo.util.MsgParamUtil;
import codigoalvo.util.SegurancaUtil;
import codigoalvo.util.SegurancaUtilMd5;

public class LoginServiceImpl implements LoginService {

	public static final Integer MAXIMO_TENTATIVAS_LOGIN = 5;
	private static final Logger LOGGER = Logger.getLogger(LoginServiceImpl.class);

	private UsuarioDao usuarioDao;
	SegurancaUtil segurancaUtil;

	public LoginServiceImpl() {
		LOGGER.debug("####################  construct  ####################");
		this.usuarioDao = new UsuarioDaoJpa(EntityManagerUtil.getEntityManager());
		this.segurancaUtil = new SegurancaUtilMd5();
	}

	@Override
	public Usuario efetuarLogin(String login, String senha) throws LoginException {
		Usuario usuario = null;
		try {
			usuario = this.usuarioDao.buscarPorLogin(login);
		} catch (NoResultException nre) {
			LOGGER.debug("Usuario não encontrado (login): " + login);
		}
		if (usuario == null || usuario.getId() == null) {
			LOGGER.debug("login.invalido");
			throw new LoginException("login.invalido");
		} else {
			if (!usuario.getAtivo()) {
				LOGGER.debug("login.usuarioDesativado");
				throw new LoginException("login.usuarioDesativado");
			} else if (usuario.getSenha().equals(this.segurancaUtil.criptografar(senha))) {
				usuario.setDataUltimoLogin(new Date());
				usuario.setTentativasLoginInvalido(0);
				usuario.setDataUltimaFalhaLogin(null);
				LOGGER.debug("login.sucesso");
				this.usuarioDao.atualizar(usuario);
				return usuario;
			} else if (usuario.getTentativasLoginInvalido() >= MAXIMO_TENTATIVAS_LOGIN) {
				usuario.setDataUltimaFalhaLogin(new Date());
				usuario.setAtivo(false);
				this.usuarioDao.atualizar(usuario);
				LOGGER.debug("login.senhaDesativada");
				throw new LoginException("login.senhaDesativada");
			} else {
				usuario.setTentativasLoginInvalido(usuario.getTentativasLoginInvalido() + 1);
				usuario.setDataUltimaFalhaLogin(new Date());
				this.usuarioDao.atualizar(usuario);
				String msg = "login.senhaInvalida";
				msg += MsgParamUtil.buildParams(usuario.getTentativasLoginInvalido().toString(),
						MAXIMO_TENTATIVAS_LOGIN.toString());
				LOGGER.debug(msg);
				throw new LoginException(msg);
			}
		}
	}

}
