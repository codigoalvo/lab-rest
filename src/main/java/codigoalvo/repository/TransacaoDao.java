package codigoalvo.repository;

import java.util.Date;
import java.util.List;

import codigoalvo.entity.Transacao;
import codigoalvo.entity.TransacaoTipo;
import codigoalvo.genericdao.GenericDao;

public interface TransacaoDao extends GenericDao<Transacao> {

	public List<Transacao> transacoesDoUsuarioNoPeriodo(int usuarioId, Date dataInicial, Date dataFinal, boolean considerarDataPagamento, TransacaoTipo tipo);
	public Transacao transacaoDoUsuario(Integer usuarioId, Integer transacaoId);

}
