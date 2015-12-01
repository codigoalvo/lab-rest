package codigoalvo.service;

import javax.security.auth.login.LoginException;

import codigoalvo.entity.Usuario;

public interface LoginService {

	public Usuario efetuarLogin(String login, String senha) throws LoginException;
	public Usuario alterarSenha(String login, String senhaAtual, String senhaNova) throws LoginException;
	public Usuario alterarSenha(Usuario usuario, String senhaNova) throws LoginException;
	Usuario buscarUsuarioPorLoginSenha(String login, String senha) throws LoginException;

}
