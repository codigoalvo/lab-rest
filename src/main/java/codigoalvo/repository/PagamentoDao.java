package codigoalvo.repository;

import java.util.List;

import codigoalvo.entity.Pagamento;
import codigoalvo.genericdao.GenericDao;

public interface PagamentoDao extends GenericDao<Pagamento> {

	public Pagamento buscarPorNome(String nome);
	public List<Pagamento> pagamentosDoUsuario(int usuarioId);
	public Pagamento pagamentoDoUsuario(Integer usuarioId, Integer pagamentoId);

}
