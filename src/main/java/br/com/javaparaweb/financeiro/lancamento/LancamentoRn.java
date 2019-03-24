package br.com.javaparaweb.financeiro.lancamento;

import java.util.Date;
import java.util.List;

import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.util.DaoFactory;

public class LancamentoRn {

	private LancamentoDao lancamentoDao;
	
	public LancamentoRn() {
		lancamentoDao = DaoFactory.criarLancamentoDao();
	}
	
	public void salvar(Lancamento lancamento) {
		lancamentoDao.salvar(lancamento);
	}
	
	public void excluir(Lancamento lancamento) {
		lancamentoDao.excluir(lancamento);
	}
	
	public Lancamento carregar(Integer lancamento) {
		return lancamentoDao.carregar(lancamento);
	}
	
	public float saldo(Conta conta, Date data) {
		float saldoInicial = conta.getSaldoInicial();
		float saldoNaData = lancamentoDao.saldo(conta, data);
		return saldoNaData + saldoInicial;
	}

	public List<Lancamento> listar(Conta conta, Date dataInicio, Date dataFim){
		return lancamentoDao.listar(conta, dataInicio, dataFim);
	}
}
