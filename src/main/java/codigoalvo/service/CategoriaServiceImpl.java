package codigoalvo.service;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import codigoalvo.entity.Categoria;
import codigoalvo.repository.CategoriaDao;
import codigoalvo.repository.CategoriaDaoJpa;

public class CategoriaServiceImpl implements CategoriaService {

	private CategoriaDao dao;

	public CategoriaServiceImpl() {
		Logger.getLogger(CategoriaServiceImpl.class).debug("####################  construct  ####################");
		this.dao = new CategoriaDaoJpa(Persistence.createEntityManagerFactory("default").createEntityManager());
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
	public Categoria buscar(Integer id) {
		return this.dao.buscar(id);
	}

	@Override
	public List<Categoria> listar() {
		return this.dao.listar();
	}

	@Override
	public Categoria buscarPorNome(String nome) {
		Categoria categoria = null;
		try {
			categoria = this.dao.buscarPorNome(nome);
		} catch (NoResultException nre) {
			Logger.getLogger(CategoriaServiceImpl.class).debug("Categoria não encontrada (nome): " + nome);
		}
		return categoria;
	}

}
