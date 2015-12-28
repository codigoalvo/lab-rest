package codigoalvo.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import codigoalvo.entity.Planejamento;
import codigoalvo.genericdao.GenericDaoJpa;
import codigoalvo.util.DateUtil;

public class PlanejamentoDaoJpa extends GenericDaoJpa<Planejamento> implements PlanejamentoDao {

	public PlanejamentoDaoJpa(EntityManager entityManager) {
		setEntityManager(entityManager);
		Logger.getLogger(PlanejamentoDaoJpa.class).debug("####################  construct  ####################");
	}

	@Override
	public List<Planejamento> planejamentosDoUsuarioNoPeriodo(int usuarioId, int mes, int ano) {
		String jpql = "FROM Planejamento c WHERE c.usuario.id = :usuarioId  AND (periodo >= :dataInicial AND periodo < :dataFinal)";
		TypedQuery<Planejamento> query = getEntityManager().createQuery(jpql, Planejamento.class);
		query.setParameter("usuarioId", usuarioId);
		Logger.getLogger(PlanejamentoDaoJpa.class).debug("Mes: "+mes+", ano: "+ano);
		Calendar periodo = DateUtil.primeiroDiaDoMes(mes, ano);
		Date dataInicial = periodo.getTime();
		periodo.add(Calendar.MONTH, 1);
		Date dataFinal = periodo.getTime();
		Logger.getLogger(PlanejamentoDaoJpa.class).debug("dataInicial: "+dataInicial);
		Logger.getLogger(PlanejamentoDaoJpa.class).debug("dataFinal: "+dataFinal);
		query.setParameter("dataInicial", dataInicial, TemporalType.DATE);
		query.setParameter("dataFinal", dataFinal, TemporalType.DATE);
		return query.getResultList();
	}

	@Override
	public Planejamento planejamentoDoUsuario(Integer usuarioId, Integer planejamentoId) {
		TypedQuery<Planejamento> query = getEntityManager().createQuery("FROM Planejamento p WHERE p.usuario.id = :usuarioId and p.id = :planejamentoId" , Planejamento.class);
		query.setParameter("usuarioId", usuarioId);
		query.setParameter("planejamentoId", planejamentoId);
		try {
			return query.getSingleResult();
		} catch (NoResultException exc) {
			return null;
		}
	}

}
