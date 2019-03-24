package br.com.javaparaweb.financeiro.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.javaparaweb.financeiro.categoria.Categoria;
import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.lancamento.Lancamento;
import br.com.javaparaweb.financeiro.lancamento.LancamentoRn;

@ManagedBean(name="lancamentoBean")
@ViewScoped
public class LancamentoBean implements Serializable {

	private static final long serialVersionUID = 3882855062422561543L;
	
	private List<Lancamento> lista;
	private Conta conta;
	private List<Double> saldos;
	private float saldoGeral;
	private Lancamento editado = new Lancamento();
	
	@ManagedProperty(value="#{contextoBean}")
	private ContextoBean contextoBean;
	
	public LancamentoBean() {
		novo();
	}
	
	public String novo() {
		this.editado = new Lancamento();
		this.editado.setData(new Date());
		return null;
	}
	
	public void editar() {}
	
	public void salvar() {
		editado.setUsuario(contextoBean.getUsuarioLogado());
		editado.setConta(contextoBean.getContaAtiva());
		
		LancamentoRn lancamentoRn = new LancamentoRn();
		lancamentoRn.salvar(editado);
		this.novo();
		this.lista = null;
	}
	
	public void excluir() {
		LancamentoRn lancamentoRn = new LancamentoRn();
		lancamentoRn.excluir(editado);
		this.lista = null;
	}
	
	public List<Lancamento> getLista(){
		if(lista == null || this.conta != contextoBean.getContaAtiva()) {
			conta = contextoBean.getContaAtiva();
			
			Calendar dataSaldo = new GregorianCalendar();
			dataSaldo.add(Calendar.MONTH, -1);
			dataSaldo.add(Calendar.DAY_OF_MONTH, -1);
			
			Calendar inicio = new GregorianCalendar();
			inicio.add(Calendar.MONTH, -1);
			
			LancamentoRn lancamentoRn = new LancamentoRn();
			saldoGeral = lancamentoRn.saldo(conta, dataSaldo.getTime());
			lista = lancamentoRn.listar(conta, inicio.getTime(), null);
			
			Categoria categoria = null;
			double saldo = saldoGeral;
			saldos = new ArrayList<Double>();
			for(Lancamento lancamento : lista) {
				categoria = lancamento.getCategoria();
				saldo = saldo + (lancamento.getValor().floatValue() * categoria.getFator());
				saldos.add(saldo);
			}
		}
		return lista;			
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public List<Double> getSaldos() {
		return saldos;
	}

	public void setSaldos(List<Double> saldos) {
		this.saldos = saldos;
	}

	public float getSaldoGeral() {
		return saldoGeral;
	}

	public void setSaldoGeral(float saldoGeral) {
		this.saldoGeral = saldoGeral;
	}

	public Lancamento getEditado() {
		return editado;
	}

	public void setEditado(Lancamento editado) {
		this.editado = editado;
	}

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}

	public void setLista(List<Lancamento> lista) {
		this.lista = lista;
	}

	
}
