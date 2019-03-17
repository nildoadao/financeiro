package br.com.javaparaweb.financeiro.web;

import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.conta.ContaRn;
import br.com.javaparaweb.financeiro.usuario.Usuario;
import br.com.javaparaweb.financeiro.usuario.UsuarioRn;

@ManagedBean(name="usuarioBean")
@RequestScoped
public class UsuarioBean {

	private Usuario usuario = new Usuario();
	private String confirmarSenha;
	private List<Usuario> lista;
	private String destinoSalvar;
	private Conta conta = new Conta();
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String getConfirmarSenha() {
		return confirmarSenha;
	}
	
	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}
	
	public String novo() {
		
		destinoSalvar = "usuariosucesso";
		usuario = new Usuario();
		usuario.setAtivo(true);
		return "publico/usuario";
	}
	
	public String editar() {
		
		this.confirmarSenha = this.usuario.getSenha();
		return "/publico/usuario";
	}
	
	public String salvar() {
		
		FacesContext context = FacesContext.getCurrentInstance();		
		String senha = this.usuario.getSenha();
		
		if(!senha.equals(this.confirmarSenha)) {
			FacesMessage facesMessage = new FacesMessage("A senha não foi confirma corretamente");
			context.addMessage(null, facesMessage);
			return null;
		}
		
		UsuarioRn usuarioRn = new UsuarioRn();
		usuarioRn.salvar(this.usuario);
		
		if(this.conta.getDescricao() != null) {
			this.conta.setUsuario(this.usuario);
			this.conta.setFavorita(true);
			ContaRn contaRn = new ContaRn();
			contaRn.salvar(this.conta);
		}
		
		return destinoSalvar;			
	}

	public String excluir() {
		
		UsuarioRn usuarioRn = new UsuarioRn();
		usuarioRn.excluir(this.usuario);
		this.lista = null;
		return null;
	}
	
	public String ativar() {
		
		if(this.usuario.isAtivo())
			this.usuario.setAtivo(false);
		else
			this.usuario.setAtivo(true);
		
		UsuarioRn usuarioRn = new UsuarioRn();
		usuarioRn.salvar(this.usuario);
		return null;
	}
	
	public List<Usuario> getLista(){
		
		if(this.lista == null) {
			UsuarioRn usuarioRn = new UsuarioRn();
			this.lista = usuarioRn.listar();
		}
		return this.lista;
	}
		
	
	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}

	public void setLista(List<Usuario> lista) {
		this.lista = lista;
	}
	
	
	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public String atribuirPermissao(Usuario usuario, String permissao) {
		
		this.usuario = usuario;
		Set<String> permissoes = this.usuario.getPermissao();
		
		if(permissoes.contains(permissao))
			permissoes.remove(permissao);
		else
			permissoes.add(permissao);
		
		return null;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((confirmarSenha == null) ? 0 : confirmarSenha.hashCode());
		result = prime * result + ((conta == null) ? 0 : conta.hashCode());
		result = prime * result + ((destinoSalvar == null) ? 0 : destinoSalvar.hashCode());
		result = prime * result + ((lista == null) ? 0 : lista.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioBean other = (UsuarioBean) obj;
		if (confirmarSenha == null) {
			if (other.confirmarSenha != null)
				return false;
		} else if (!confirmarSenha.equals(other.confirmarSenha))
			return false;
		if (conta == null) {
			if (other.conta != null)
				return false;
		} else if (!conta.equals(other.conta))
			return false;
		if (destinoSalvar == null) {
			if (other.destinoSalvar != null)
				return false;
		} else if (!destinoSalvar.equals(other.destinoSalvar))
			return false;
		if (lista == null) {
			if (other.lista != null)
				return false;
		} else if (!lista.equals(other.lista))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
	
}
