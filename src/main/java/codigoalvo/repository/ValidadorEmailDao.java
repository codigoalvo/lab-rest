package codigoalvo.repository;

import java.util.Date;
import java.util.List;

import codigoalvo.entity.ValidadorEmail;
import codigoalvo.genericdao.GenericDao;

public interface ValidadorEmailDao extends GenericDao<ValidadorEmail> {

	public ValidadorEmail buscarPorEmail(String email);
	public void removerAnterioresData(Date data);
	public List<ValidadorEmail> buscarRegistrosDepoisDe(Date data);

}
