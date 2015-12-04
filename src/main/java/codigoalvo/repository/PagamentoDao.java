package codigoalvo.repository;

import codigoalvo.entity.Pagamento;
import codigoalvo.genericdao.GenericDao;

public interface PagamentoDao extends GenericDao<Pagamento> {

	public Pagamento buscarPorNome(String nome);

}
