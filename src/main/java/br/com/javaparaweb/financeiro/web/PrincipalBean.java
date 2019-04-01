package br.com.javaparaweb.financeiro.web;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.lancamento.Lancamento;
import br.com.javaparaweb.financeiro.lancamento.LancamentoRn;

@ManagedBean(name="principalBean")
public class PrincipalBean {

	private List<Lancamento> listaAteHoje;
	private List<Lancamento> listaFuturos;
	
	@ManagedProperty(value = "#{contextoBean}")
	private ContextoBean contextoBean;
	
	public List<Lancamento> getListaAteHoje(){
		if(this.listaAteHoje == null) {
			Conta conta = contextoBean.getContaAtiva();
			Calendar hoje = new GregorianCalendar();
			LancamentoRn lancamentoRn = new LancamentoRn();
			listaAteHoje = lancamentoRn.listar(conta, null, hoje.getTime());
		}
		return listaAteHoje;
	}
	
	public List<Lancamento> getListaFuturos(){
		if(this.listaFuturos == null) {
			Conta conta = contextoBean.getContaAtiva();
			Calendar amanha = new GregorianCalendar();
			amanha.add(Calendar.DAY_OF_MONTH, 1);
			LancamentoRn lancamentoRn = new LancamentoRn();
			listaFuturos = lancamentoRn.listar(conta, amanha.getTime(), null);
		}
		return listaFuturos;
	}

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}

	public void setListaAteHoje(List<Lancamento> listaAteHoje) {
		this.listaAteHoje = listaAteHoje;
	}

	public void setListaFuturos(List<Lancamento> listaFuturos) {
		this.listaFuturos = listaFuturos;
	}
	
}
