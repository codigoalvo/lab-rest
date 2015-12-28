package codigoalvo.service;

import java.sql.SQLException;
import java.util.List;

import codigoalvo.entity.Planejamento;

public interface PlanejamentoService {

	Planejamento gravar(Planejamento entity) throws SQLException;
	void remover(Planejamento entity) throws SQLException;
	void removerPorId(Integer id) throws SQLException;
	Planejamento buscar(Integer usuarioId, Integer planejamentoId);
	List<Planejamento> listarDoPeriodo(Integer usuarioId, Integer mes, Integer ano);

}
