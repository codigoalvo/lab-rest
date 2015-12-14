package codigoalvo.repository;

import java.util.List;

import codigoalvo.entity.Conta;
import codigoalvo.genericdao.GenericDao;

public interface ContaDao extends GenericDao<Conta> {

	public Conta buscarPorNome(String nome);
	public List<Conta> contasDoUsuario(int usuarioId);
	public Conta contaDoUsuario(Integer usuarioId, Integer contaId);

}
