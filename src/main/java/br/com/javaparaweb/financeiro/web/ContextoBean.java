package br.com.javaparaweb.financeiro.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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
	private List<Locale> idiomas;
	
	
	public Usuario getUsuarioLogado() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext external = context.getExternalContext();
		String login = external.getRemoteUser();
		
		if(login != null) {
			UsuarioRn usuarioRn = new UsuarioRn();
			Usuario usuario = usuarioRn.buscarPorLogin(login);
			String[] info = usuario.getIdioma().split("_");
			Locale locale = new Locale(info[0], info[1]);
			context.getViewRoot().setLocale(locale);
			return usuario;
		}
		
		return null;
	}
	
	public List<Locale> getIdiomas(){
		FacesContext context = FacesContext.getCurrentInstance();
		Iterator<Locale> locales = context.getApplication().getSupportedLocales();
		idiomas = new ArrayList<Locale>();
		
		while(locales.hasNext()) 
			idiomas.add(locales.next());
		
		return idiomas;
	}
	
	public void setIdiomaUsuario(String idioma) {
		Usuario usuario = this.getUsuarioLogado();
		usuario.setIdioma(idioma);
		UsuarioRn usuarioRn = new UsuarioRn();
		usuarioRn.salvar(usuario);
		
		String[] info = idioma.split("_");
		Locale locale = new Locale(info[0], info[1]);
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.getViewRoot().setLocale(locale);
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
