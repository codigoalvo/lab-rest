package codigoalvo.repository;

import javax.persistence.EntityManager;

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

}
