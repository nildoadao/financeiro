package br.com.javaparaweb.financeiro.util;

import br.com.javaparaweb.financeiro.conta.ContaDao;
import br.com.javaparaweb.financeiro.conta.ContaDaoHibernate;
import br.com.javaparaweb.financeiro.usuario.UsuarioDao;
import br.com.javaparaweb.financeiro.usuario.UsuarioDaoHibernate;

public class DaoFactory {

	public static UsuarioDao criarUsuarioDao() {
		UsuarioDaoHibernate usuarioDao = new UsuarioDaoHibernate();
		usuarioDao.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return usuarioDao;
	}

	public static ContaDao criarContaDao() {
		ContaDaoHibernate contaDao = new ContaDaoHibernate();
		contaDao.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return contaDao;
	}
}
