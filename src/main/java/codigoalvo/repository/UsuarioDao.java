package codigoalvo.repository;

import codigoalvo.entity.Usuario;
import codigoalvo.genericdao.GenericDao;

public interface UsuarioDao extends GenericDao<Usuario> {

	public Usuario buscarPorLogin(String login);
	public Usuario buscarPorEmail(String email);

}
