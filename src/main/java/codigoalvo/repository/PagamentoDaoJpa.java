package codigoalvo.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import codigoalvo.entity.Pagamento;
import codigoalvo.genericdao.GenericDaoJpa;

public class PagamentoDaoJpa extends GenericDaoJpa<Pagamento> implements PagamentoDao {

	public PagamentoDaoJpa(EntityManager entityManager) {
		setEntityManager(entityManager);
		Logger.getLogger(PagamentoDaoJpa.class).debug("####################  construct  ####################");
	}

	@Override
	public Pagamento buscarPorNome(String nome) {
		if (emptyOrNull(nome)) {
			return null;
		}
		return buscarPor("nome", nome.trim());
	}

	@Override
	public List<Pagamento> pagamentosDoUsuario(int usuarioId) {
		TypedQuery<Pagamento> query = getEntityManager().createQuery("FROM Pagamento c WHERE c.usuario.id = :usuarioId", Pagamento.class);
		query.setParameter("usuarioId", usuarioId);
		return query.getResultList();
	}

	@Override
	public Pagamento pagamentoDoUsuario(Integer usuarioId, Integer pagamentoId) {
		TypedQuery<Pagamento> query = getEntityManager().createQuery("FROM Pagamento c WHERE c.usuario.id = :usuarioId and c.id = :pagamentoId" , Pagamento.class);
		query.setParameter("usuarioId", usuarioId);
		query.setParameter("pagamentoId", pagamentoId);
		try {
			return query.getSingleResult();
		} catch (NoResultException exc) {
			return null;
		}
	}

}
