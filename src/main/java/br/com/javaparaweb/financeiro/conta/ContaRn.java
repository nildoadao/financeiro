package br.com.javaparaweb.financeiro.conta;

import java.util.Date;
import java.util.List;

import br.com.javaparaweb.financeiro.usuario.Usuario;
import br.com.javaparaweb.financeiro.util.DaoFactory;

public class ContaRn {
	
	private ContaDao contaDao;
	
	public ContaRn() {
		contaDao = DaoFactory.criarContaDao();
	}
	
	public Conta carregar(Integer id) {
		return contaDao.carregar(id);
	}
	
	public List<Conta> listar(Usuario usuario){
		return contaDao.listar(usuario);
	}
	
	public void excluir(Conta conta) {
		contaDao.excluir(conta);
	}
	
	public void salvar(Conta conta) {
		conta.setDataCadastro(new Date());
		contaDao.salvar(conta);
	}
	
	public Conta buscarFavorita(Usuario usuario) {
		return contaDao.buscarFavorita(usuario);
	}
	
	public void tornarFavorita(Conta contaFavorita) {
		
		Conta conta = buscarFavorita(contaFavorita.getUsuario());
		
		if(conta != null) {
			conta.setFavorita(false);
			contaDao.salvar(conta);
		}
		
		contaFavorita.setFavorita(true);
		contaDao.salvar(contaFavorita);
	}

}
