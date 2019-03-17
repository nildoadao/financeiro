package br.com.javaparaweb.financeiro.web;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.conta.ContaRn;

@ManagedBean
@RequestScoped
public class ContaBean {

	private Conta selecionada = new Conta();
	private List<Conta> lista = null;
	
	@ManagedProperty(value = "#{contextoBean}")
	private ContextoBean contextoBean;

	public String salvar() {
		this.selecionada.setUsuario(this.contextoBean.getUsuarioLogado());
		ContaRn contaRn = new ContaRn();
		contaRn.salvar(this.selecionada);
		this.selecionada = new Conta();
		this.lista = null;
		return null;
	}
	
	public String excluir() {
		ContaRn contaRn = new ContaRn();
		contaRn.excluir(this.selecionada);
		this.selecionada = new Conta();
		this.lista = null;
		return null;
	}
	
	public String tornarFavorita() {
		ContaRn contaRn = new ContaRn();
		contaRn.tornarFavorita(this.selecionada);
		this.selecionada = new Conta();
		return null;
	}

	public Conta getSelecionada() {
		if(this.selecionada.getConta() == null && contextoBean.getContaAtiva() != null) {
			this.selecionada = contextoBean.getContaAtiva();
		}
		return this.selecionada;
	}

	public void setSelecionada(Conta selecionada) {
		this.selecionada = selecionada;
	}

	public List<Conta> getLista() {
		if(this.lista == null) {
			ContaRn contaRn = new ContaRn();
			this.lista = contaRn.listar(this.contextoBean.getUsuarioLogado());
		}
		return this.lista;
	}

	public void setLista(List<Conta> lista) {
		this.lista = lista;
	}

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}
	
	
}
