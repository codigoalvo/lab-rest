package codigoalvo.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import codigoalvo.entity.Planejamento;
import codigoalvo.repository.PlanejamentoDao;
import codigoalvo.repository.PlanejamentoDaoJpa;
import codigoalvo.util.EntityManagerUtil;

public class PlanejamentoServiceImpl implements PlanejamentoService {

	private PlanejamentoDao dao;
	private static final Logger LOG = Logger.getLogger(PlanejamentoServiceImpl.class);

	public PlanejamentoServiceImpl() {
		LOG.debug("####################  construct  ####################");
		this.dao = new PlanejamentoDaoJpa(EntityManagerUtil.getEntityManager());
	}

	@Override
	public Planejamento gravar(Planejamento planejamento) throws SQLException {

		try {
			this.dao.beginTransaction();
			if (planejamento.getId() == null) {
				this.dao.criar(planejamento);
			} else {
				this.dao.atualizar(planejamento);
			}
			this.dao.commit();
			return planejamento;
		} catch (Throwable exc) {
			this.dao.rollback();
			LOG.debug("gravar.rollback");
			this.dao.getEntityManager().clear();
			LOG.debug("gravar.dao.em.clear");
			throw new SQLException(exc);
		}

	}

	@Override
	public void remover(Planejamento planejamento) throws SQLException {
		try {
			this.dao.beginTransaction();
			this.dao.remover(planejamento.getId());
			this.dao.commit();
		} catch (Throwable exc) {
			this.dao.rollback();
			throw new SQLException(exc);
		}

	}

	@Override
	public void removerPorId(Integer id) throws SQLException {
		try {
			this.dao.beginTransaction();
			this.dao.remover(id);
			this.dao.commit();
		} catch (Throwable exc) {
			this.dao.rollback();
			throw new SQLException(exc);
		}
	}

	@Override
	public Planejamento buscar(Integer usuarioId, Integer planejamentoId) {
		return this.dao.planejamentoDoUsuario(usuarioId, planejamentoId);
	}

	@Override
	public List<Planejamento> listarDoPeriodo(Integer usuarioId, Integer mes, Integer ano) {
		List<Planejamento> response = this.dao.planejamentosDoUsuarioNoPeriodo(usuarioId, mes, ano);
		//LOG.debug(response);
		return response;
	}

}
