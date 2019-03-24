package br.com.javaparaweb.financeiro.lancamento;

import java.util.Date;
import java.util.List;

import br.com.javaparaweb.financeiro.conta.Conta;

public interface LancamentoDao {
	public void salvar(Lancamento lancamento);
	public void excluir(Lancamento lancamento);
	public Lancamento carregar(Integer id);
	public List<Lancamento> listar(Conta conta, Date dataInicio, Date dataFim);
	public float saldo(Conta conta, Date data);
}
