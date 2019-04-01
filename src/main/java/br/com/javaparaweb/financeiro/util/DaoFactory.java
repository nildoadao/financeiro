package br.com.javaparaweb.financeiro.util;

import br.com.javaparaweb.financeiro.categoria.CategoriaDao;
import br.com.javaparaweb.financeiro.categoria.CategoriaDaoHibernate;
import br.com.javaparaweb.financeiro.cheque.ChequeDao;
import br.com.javaparaweb.financeiro.cheque.ChequeDaoHibernate;
import br.com.javaparaweb.financeiro.conta.ContaDao;
import br.com.javaparaweb.financeiro.conta.ContaDaoHibernate;
import br.com.javaparaweb.financeiro.lancamento.LancamentoDao;
import br.com.javaparaweb.financeiro.lancamento.LancamentoDaoHibernate;
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
	
	public static CategoriaDao criarCategoriaDao() {
		CategoriaDaoHibernate categoriaDao = new CategoriaDaoHibernate();
		categoriaDao.SetSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return categoriaDao;
	}
	
	public static LancamentoDao criarLancamentoDao() {
		LancamentoDaoHibernate lancamentoDao = new LancamentoDaoHibernate();
		lancamentoDao.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return lancamentoDao;
	}
	
	public static ChequeDao criarChequeDao() {
		ChequeDaoHibernate chequeDao = new ChequeDaoHibernate();
		chequeDao.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return chequeDao;		
	}
}
