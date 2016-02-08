package codigoalvo.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import codigoalvo.entity.Transacao;
import codigoalvo.entity.TransacaoTipo;
import codigoalvo.genericdao.GenericDaoJpa;

public class TransacaoDaoJpa extends GenericDaoJpa<Transacao> implements TransacaoDao {

	public TransacaoDaoJpa(EntityManager entityManager) {
		setEntityManager(entityManager);
		Logger.getLogger(TransacaoDaoJpa.class).trace("####################  construct  ####################");
	}

	@Override
	public void removerPorId(Object id) {
		Query query = getEntityManager().createQuery("delete Transacao where id = :id");
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public List<Transacao> transacoesDoUsuarioNoPeriodo(int usuarioId, Date dataInicial, Date dataFinal, boolean considerarDataPagamento, TransacaoTipo tipo) {
		String jpql = "FROM Transacao c WHERE c.usuario.id = :usuarioId ";
		if (considerarDataPagamento) {
			jpql += " AND (dataPagamento >= :dataInicial AND dataPagamento < :dataFinal)";
		} else {
			jpql += " AND (dataTransacao >= :dataInicial AND dataTransacao < :dataFinal)";
		}
		if (tipo != null) {
			jpql += " AND (tipo = :tipo)";
		}
		TypedQuery<Transacao> query = getEntityManager().createQuery(jpql, Transacao.class);
		query.setParameter("usuarioId", usuarioId);
		Logger.getLogger(TransacaoDaoJpa.class).debug("dataInicial: "+dataInicial);
		Logger.getLogger(TransacaoDaoJpa.class).debug("dataFinal: "+dataFinal);
		query.setParameter("dataInicial", dataInicial, TemporalType.DATE);
		query.setParameter("dataFinal", dataFinal, TemporalType.DATE);
		if (tipo != null) {
			query.setParameter("tipo", tipo.getId());
		}
		return query.getResultList();
	}

	@Override
	public Transacao transacaoDoUsuario(Integer usuarioId, Integer transacaoId) {
		TypedQuery<Transacao> query = getEntityManager().createQuery("FROM Transacao p WHERE p.usuario.id = :usuarioId and p.id = :transacaoId" , Transacao.class);
		query.setParameter("usuarioId", usuarioId);
		query.setParameter("transacaoId", transacaoId);
		try {
			return query.getSingleResult();
		} catch (NoResultException exc) {
			return null;
		}
	}

}
