package codigoalvo.service;

import javax.security.auth.login.LoginException;

import codigoalvo.entity.Usuario;

public interface LoginService {

	public Usuario efetuarLogin(String email, String senha) throws LoginException;
	public Usuario alterarSenha(String email, String senhaAtual, String senhaNova) throws LoginException;
	public Usuario alterarSenha(Usuario usuario, String senhaNova) throws LoginException;
	Usuario buscarUsuarioPorEmailSenha(String email, String senha) throws LoginException;

}
