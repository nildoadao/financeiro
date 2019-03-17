package br.com.javaparaweb.financeiro.conta;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import br.com.javaparaweb.financeiro.usuario.Usuario;


public class ContaDaoHibernate implements ContaDao {

	private Session session;
	
	public void setSession(Session session) {
		this.session = session;
	}
	
	@Override
	public void salvar(Conta conta) {
		this.session.saveOrUpdate(conta);
	}

	@Override
	public void excluir(Conta conta) {
		this.session.delete(conta);
	}

	@Override
	public Conta carregar(Integer id) {
		return (Conta) this.session.get(Conta.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Conta> listar(Usuario usuario) {
		Criteria criteria = session.createCriteria(Conta.class);
		criteria.add(Restrictions.eq("usuario", usuario));
		return criteria.list();
	}

	@Override
	public Conta buscarFavorita(Usuario usuario) {
		Criteria criteria = session.createCriteria(Conta.class);
		criteria.add(Restrictions.eq("usuario", usuario));
		criteria.add(Restrictions.eq("favorita", true));
		return (Conta) criteria.uniqueResult();
	}


}
