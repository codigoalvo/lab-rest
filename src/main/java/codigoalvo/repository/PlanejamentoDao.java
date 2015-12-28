package codigoalvo.repository;

import java.util.List;

import codigoalvo.entity.Planejamento;
import codigoalvo.genericdao.GenericDao;

public interface PlanejamentoDao extends GenericDao<Planejamento> {

	public List<Planejamento> planejamentosDoUsuarioNoPeriodo(int usuarioId, int mes, int ano);
	public Planejamento planejamentoDoUsuario(Integer usuarioId, Integer planejamentoId);

}
