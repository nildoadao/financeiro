package br.com.javaparaweb.financeiro.usuario;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class UsuarioDaoHibernate implements UsuarioDao {

	private Session sessao;
	
	public void setSession(Session sessao) {
		this.sessao = sessao;
	}
	
	public void salvar(Usuario usuario) {
		sessao.save(usuario);
	}

	public void atualizar(Usuario usuario) {
		
		if(usuario.getPermissao() == null || usuario.getPermissao().size() == 0) {
			Usuario usuarioPermissao = this.carregar(usuario.getCodigo());
			usuario.setPermissao(usuarioPermissao.getPermissao());
			this.sessao.evict(usuarioPermissao);
		}
		sessao.update(usuario);
	}

	public void excluir(Usuario usuario) {
		sessao.delete(usuario);
	}

	public Usuario carregar(Integer codigo) {
		return (Usuario) sessao.get(Usuario.class, codigo);
	}

	public Usuario buscarPorLogin(String login) {
		String hql = "select u from Usuario u where u.login = :login";
		Query consulta = sessao.createQuery(hql);
		consulta.setString("login", login);
		return (Usuario) consulta.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> listar() {
		return sessao.createCriteria(Usuario.class).list();
	}

}
