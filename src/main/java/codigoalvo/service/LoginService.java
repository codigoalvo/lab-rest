package codigoalvo.service;

import javax.security.auth.login.LoginException;
import codigoalvo.entity.Usuario;

public interface LoginService {

	public Usuario efetuarLogin(String login, String senha) throws LoginException;

}
