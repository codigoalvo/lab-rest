package codigoalvo.service;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import codigoalvo.entity.Pagamento;
import codigoalvo.entity.PagamentoTipo;
import codigoalvo.repository.PagamentoDao;
import codigoalvo.repository.PagamentoDaoJpa;
import codigoalvo.util.EntityManagerUtil;

public class PagamentoServiceImpl implements PagamentoService {

	private PagamentoDao dao;
	private static final Logger LOG = Logger.getLogger(PagamentoServiceImpl.class);

	public PagamentoServiceImpl() {
		LOG.debug("####################  construct  ####################");
		this.dao = new PagamentoDaoJpa(EntityManagerUtil.getEntityManager());
	}

	@Override
	public Pagamento gravar(Pagamento pagamento) throws SQLException {

		try {
			this.dao.beginTransaction();
			if (pagamento.getTipo() != PagamentoTipo.CARTAO) {
				pagamento.setDiaFechamento(null);
				pagamento.setDiaPagamento(null);
			}
			if (pagamento.getId() == null) {
				this.dao.criar(pagamento);
			} else {
				this.dao.atualizar(pagamento);
			}
			this.dao.commit();
			return pagamento;
		} catch (Throwable exc) {
			LOG.error(exc);
			this.dao.rollback();
			throw new SQLException(exc);
		}

	}

	@Override
	public void remover(Pagamento pagamento) throws SQLException {
		try {
			this.dao.beginTransaction();
			this.dao.remover(pagamento.getId());
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
	public Pagamento buscar(Integer id) {
		return this.dao.buscar(id);
	}

	@Override
	public List<Pagamento> listar() {
		return this.dao.listar();
	}

	@Override
	public Pagamento buscarPorNome(String nome) {
		Pagamento pagamento = null;
		try {
			pagamento = this.dao.buscarPorNome(nome);
		} catch (NoResultException nre) {
			LOG.debug("Pagamento não encontrado (nome): " + nome);
		}
		return pagamento;
	}

}
