package codigoalvo.service;

import java.sql.SQLException;
import java.util.List;
import codigoalvo.entity.Pagamento;

public interface PagamentoService {

	Pagamento gravar(Pagamento entity) throws SQLException;
	void remover(Pagamento entity) throws SQLException;
	void removerPorId(Integer id) throws SQLException;
	Pagamento buscar(Integer id);
	List<Pagamento> listar();
	public Pagamento buscarPorNome(String nome);

}
