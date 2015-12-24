package codigoalvo.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import codigoalvo.entity.Conta;
import codigoalvo.genericdao.GenericDaoJpa;

public class ContaDaoJpa extends GenericDaoJpa<Conta> implements ContaDao {

	public ContaDaoJpa(EntityManager entityManager) {
		setEntityManager(entityManager);
		Logger.getLogger(ContaDaoJpa.class).debug("####################  construct  ####################");
	}

	@Override
	public Conta buscarPorNome(String nome) {
		if (emptyOrNull(nome)) {
			return null;
		}
		return buscarPor("nome", nome.trim());
	}

	@Override
	public List<Conta> contasDoUsuario(int usuarioId, boolean exibirInativos) {
		String jpql = "FROM Conta c WHERE c.usuario.id = :usuarioId";
		if (!exibirInativos) {
			jpql += " and c.dataInativo = null";
		}
		TypedQuery<Conta> query = getEntityManager().createQuery(jpql, Conta.class);
		query.setParameter("usuarioId", usuarioId);
		return query.getResultList();
	}

	@Override
	public Conta contaDoUsuario(Integer usuarioId, Integer contaId) {
		TypedQuery<Conta> query = getEntityManager().createQuery("FROM Conta c WHERE c.usuario.id = :usuarioId and c.id = :contaId" , Conta.class);
		query.setParameter("usuarioId", usuarioId);
		query.setParameter("contaId", contaId);
		try {
			return query.getSingleResult();
		} catch (NoResultException exc) {
			return null;
		}
	}

}
