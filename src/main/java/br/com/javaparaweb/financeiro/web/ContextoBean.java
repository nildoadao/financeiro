package br.com.javaparaweb.financeiro.web;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.conta.ContaRn;
import br.com.javaparaweb.financeiro.usuario.Usuario;
import br.com.javaparaweb.financeiro.usuario.UsuarioRn;

@ManagedBean
@SessionScoped
public class ContextoBean implements Serializable {

	private static final long serialVersionUID = -1412984382434612871L;
	private int codigoContaAtiva = 0;
	
	public Usuario getUsuarioLogado() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext external = context.getExternalContext();
		String login = external.getRemoteUser();
		
		if(login != null) {
			UsuarioRn usuarioRn = new UsuarioRn();
			return usuarioRn.buscarPorLogin(login);
		}
		
		return null;
	}
	
	public Conta getContaAtiva() {
		
		Conta contaAtiva = null;
		
		if(this.codigoContaAtiva == 0) 
			contaAtiva = this.getContaAtivaPadrao();
		else {
			ContaRn contaRn = new ContaRn();
			contaAtiva = contaRn.carregar(this.codigoContaAtiva);
		}
		
		if(contaAtiva != null) {
			this.codigoContaAtiva = contaAtiva.getConta();
			return contaAtiva;
		}
		
		return null;	
	}
	
	public Conta getContaAtivaPadrao() {
		
		ContaRn contaRn = new ContaRn();
		Conta contaAtiva = null;
		Usuario usuario = getUsuarioLogado();
		contaAtiva = contaRn.buscarFavorita(usuario);
		
		if(contaAtiva == null) {
			List<Conta> contas = contaRn.listar(usuario);
			
			if(contas != null && contas.size() > 0)
				contaAtiva = contas.get(0);
		}
		
		return contaAtiva;
	}
	
	public void changeContaAtiva(ValueChangeEvent event) {
		this.codigoContaAtiva = (Integer) event.getNewValue();
	}
}
