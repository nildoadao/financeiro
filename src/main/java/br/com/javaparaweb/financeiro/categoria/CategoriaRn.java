package br.com.javaparaweb.financeiro.categoria;

import java.util.List;

import br.com.javaparaweb.financeiro.usuario.Usuario;
import br.com.javaparaweb.financeiro.util.DaoFactory;

public class CategoriaRn {

	private CategoriaDao categoriaDao;
	
	public CategoriaRn() {
		this.categoriaDao = DaoFactory.criarCategoriaDao();
	}
	
	public List<Categoria> listar(Usuario usuario){
		return this.categoriaDao.listar(usuario);
	}
	
	public Categoria salvar(Categoria categoria) {
		Categoria pai = categoria.getPai();
		
		if(pai == null) {
			String msg = String.format("A Categoria {0} deve ter um pai definido", categoria.getDescricao());
			throw new IllegalArgumentException(msg);
		}
		
		boolean mudouFator = pai.getFator() != categoria.getFator();
		categoria.setFator(pai.getFator());
		categoria = this.categoriaDao.salvar(categoria);
		
		if(mudouFator) {
			categoria = this.carregar(categoria.getCodigo());
			this.replicarFator(categoria, categoria.getFator());
		}
		
		return categoria;
	}
	
	private void replicarFator(Categoria categoria, int fator) {
		if(categoria.getFilhos() != null) {
			for(Categoria filho : categoria.getFilhos()) {
				filho.setFator(fator);
				this.categoriaDao.salvar(filho);
				this.replicarFator(filho, fator);
			}
		}
	}
	
	public void excluir(Categoria categoria) {
		this.categoriaDao.excluir(categoria);
	}
	
	public void excluir(Usuario usuario) {
		List<Categoria> lista = this.listar(usuario);
		
		for(Categoria categoria : lista)
			this.categoriaDao.excluir(categoria);		
	}
	
	public Categoria carregar(Integer categoria) {
		return this.categoriaDao.carregar(categoria);
	}
	
	public void salvaEstruturaPadrao(Usuario usuario) {
		
		Categoria despesas = new Categoria(null, usuario, "DESPESAS", -1);
		despesas = this.categoriaDao.salvar(despesas);
		this.categoriaDao.salvar(new Categoria(despesas, usuario, "Moradia", -1));
		this.categoriaDao.salvar(new Categoria(despesas, usuario, "Alimentação", -1));
		this.categoriaDao.salvar(new Categoria(despesas, usuario, "Vestuário", -1));
		this.categoriaDao.salvar(new Categoria(despesas, usuario, "Deslocamento", -1));
		this.categoriaDao.salvar(new Categoria(despesas, usuario, "Cuidados Pessoais", -1));
		this.categoriaDao.salvar(new Categoria(despesas, usuario, "Educação", -1));
		this.categoriaDao.salvar(new Categoria(despesas, usuario, "Saúde", -1));
		this.categoriaDao.salvar(new Categoria(despesas, usuario, "Lazer", -1));
		this.categoriaDao.salvar(new Categoria(despesas, usuario, "Despesas Financeiras", -1));
		
		Categoria receitas = new Categoria(null, usuario, "RECEITAS", 1);
		receitas = this.categoriaDao.salvar(receitas);
		this.categoriaDao.salvar(new Categoria(receitas, usuario, "Salário", 1));
		this.categoriaDao.salvar(new Categoria(receitas, usuario, "Restituições", 1));
		this.categoriaDao.salvar(new Categoria(receitas, usuario, "Rendimento", 1));		
	}

}
