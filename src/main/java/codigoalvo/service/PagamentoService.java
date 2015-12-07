package codigoalvo.service;

import java.sql.SQLException;
import java.util.List;
import codigoalvo.entity.Pagamento;

public interface PagamentoService {

	Pagamento gravar(Pagamento entity) throws SQLException;
	void remover(Pagamento entity) throws SQLException;
	void removerPorId(Integer id) throws SQLException;
	Pagamento buscar(Integer usuarioId, Integer categoriaId);
	List<Pagamento> listar(Integer usuarioId);
	public Pagamento buscarPorNome(String nome);

}
