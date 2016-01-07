package codigoalvo.service;

import java.sql.SQLException;
import java.util.List;

import codigoalvo.entity.Transacao;
import codigoalvo.entity.TransacaoTipo;

public interface TransacaoService {

	Transacao gravar(Transacao entity) throws SQLException;
	void remover(Transacao entity) throws SQLException;
	void removerPorId(Integer id) throws SQLException;
	Transacao buscar(Integer usuarioId, Integer transacaoId);
	List<Transacao> listarDoPeriodo(Integer usuarioId, Integer mes, Integer ano, boolean considerarDataPagamento, TransacaoTipo tipo);

}
