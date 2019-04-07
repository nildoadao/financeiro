package br.com.javaparaweb.financeiro.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import br.com.javaparaweb.financeiro.categoria.Categoria;
import br.com.javaparaweb.financeiro.cheque.Cheque;
import br.com.javaparaweb.financeiro.cheque.ChequeId;
import br.com.javaparaweb.financeiro.cheque.ChequeRn;
import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.lancamento.Lancamento;
import br.com.javaparaweb.financeiro.lancamento.LancamentoRn;
import br.com.javaparaweb.financeiro.util.RNException;

@ManagedBean(name="lancamentoBean")
@ViewScoped
public class LancamentoBean implements Serializable {

	private static final long serialVersionUID = 3882855062422561543L;
	
	private List<Lancamento> lista;
	private Conta conta;
	private List<Double> saldos;
	private float saldoGeral;
	private Lancamento editado = new Lancamento();
	private Integer numeroCheque;
	
	@ManagedProperty(value="#{contextoBean}")
	private ContextoBean contextoBean;
	
	public LancamentoBean() {
		novo();
	}
	
	public void novo() {
		this.editado = new Lancamento();
		this.editado.setData(new Date());
		numeroCheque = null;
	}
	
	public void editar() {
		Cheque cheque = editado.getCheque();
		
		if(cheque != null)
			numeroCheque = cheque.getChequeId().getCheque();
	}
	
	public void salvar() {
		editado.setUsuario(contextoBean.getUsuarioLogado());
		editado.setConta(contextoBean.getContaAtiva());
		
		ChequeRn chequeRn = new ChequeRn();
		ChequeId chequeId = null;
		
		if(numeroCheque != null) {
			chequeId = new ChequeId();
			chequeId.setConta(this.contextoBean.getContaAtiva().getConta());
			chequeId.setCheque(numeroCheque);
			Cheque cheque = chequeRn.carregar(chequeId);
			FacesContext context = FacesContext.getCurrentInstance();
			
			if(cheque == null) {
				context.addMessage(null, new FacesMessage("Cheque não cadastrado"));
				return;
			}
			else if(cheque.getSituacao() == Cheque.SITUACAO_CHEQUE_CANCELADO) {
				context.addMessage(null, new FacesMessage("Cheque já cancelado"));
				return;
			}
			else {
				editado.setCheque(cheque);
				chequeRn.baixarCheque(chequeId, editado);
			}
		}
		
		LancamentoRn lancamentoRn = new LancamentoRn();
		lancamentoRn.salvar(editado);
		this.novo();
		this.lista = null;
	}
	
	public void mudouCheque(ValueChangeEvent event) {
		Integer chequeAnterior = (Integer) event.getOldValue();
		if(chequeAnterior != null) {
			ChequeRn chequeRn = new ChequeRn();
			try {
				chequeRn.desvincularLancamento(contextoBean.getContaAtiva(), chequeAnterior);
			}
			catch(RNException e) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(e.getMessage()));
				return;
			}
		}
	}
	
	public Integer getNumeroCheque() {
		return numeroCheque;
	}

	public void setNumeroCheque(Integer numeroCheque) {
		this.numeroCheque = numeroCheque;
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
