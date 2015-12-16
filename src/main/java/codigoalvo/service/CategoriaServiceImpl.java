package codigoalvo.service;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.NoResultException;
import org.apache.log4j.Logger;

import codigoalvo.entity.Categoria;
import codigoalvo.repository.CategoriaDao;
import codigoalvo.repository.CategoriaDaoJpa;
import codigoalvo.util.EntityManagerUtil;

public class CategoriaServiceImpl implements CategoriaService {

	private CategoriaDao dao;
	private static final Logger LOG = Logger.getLogger(CategoriaServiceImpl.class);

	public CategoriaServiceImpl() {
		LOG.debug("####################  construct  ####################");
		this.dao = new CategoriaDaoJpa(EntityManagerUtil.getEntityManager());
	}

	@Override
	public Categoria gravar(Categoria categoria) throws SQLException {

		try {
			this.dao.beginTransaction();
			if (categoria.getId() == null) {
				this.dao.criar(categoria);
			} else {
				this.dao.atualizar(categoria);
			}
			this.dao.commit();
			return categoria;
		} catch (Throwable exc) {
			this.dao.rollback();
			LOG.debug("gravar.rollback");
			this.dao.getEntityManager().clear();
			LOG.debug("gravar.dao.em.clear");
			throw new SQLException(exc);
		}

	}

	@Override
	public void remover(Categoria categoria) throws SQLException {
		try {
			this.dao.beginTransaction();
			this.dao.remover(categoria.getId());
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
	public Categoria buscar(Integer usuarioId, Integer categoriaId) {
		return this.dao.categoriaDoUsuario(usuarioId, categoriaId);
	}

	@Override
	public List<Categoria> listar(Integer usuarioId) {
		List<Categoria> response = this.dao.categoriasDoUsuario(usuarioId);
		//LOG.debug(response);
		return response;
	}

	@Override
	public Categoria buscarPorNome(String nome) {
		Categoria categoria = null;
		try {
			categoria = this.dao.buscarPorNome(nome);
		} catch (NoResultException nre) {
			LOG.debug("Categoria não encontrada (nome): " + nome);
		}
		return categoria;
	}

}
