package br.com.javaparaweb.financeiro.bolsa.acao;

import java.util.ArrayList;
import java.util.List;

import br.com.javaparaweb.financeiro.usuario.Usuario;
import br.com.javaparaweb.financeiro.util.DaoFactory;
import br.com.javaparaweb.financeiro.util.UtilException;

public class AcaoRn {
	
	private AcaoDao acaoDao;
	
	public AcaoRn() {
		acaoDao = DaoFactory.criarAcaoDao();
	}
	
	public void salvar(Acao acao) {
		acaoDao.salvar(acao);
	}
	
	public void excluir(Acao acao) {
		acaoDao.excluir(acao);
	}
	
	public List<Acao> listar(Usuario usuario) {
		return acaoDao.listar(usuario);
	}
	
	public List<AcaoVirtual> listarAcaoVirtual(Usuario usuario) throws UtilException{
		List<AcaoVirtual> listaAcaoVirtual = new ArrayList<AcaoVirtual>();
		AcaoVirtual acaoVirtual = null;
		String cotacao = null;
		float ultimoPreco = 0.0f;
		
		for(Acao acao : this.listar(usuario)) {
			acaoVirtual = new AcaoVirtual();
			acaoVirtual.setAcao(acao);
			cotacao = AlphaVantageUtil.getInfoCotacao(
					AlphaVantageUtil.INDICE_ULTIMO_PRECO_DIA_ACAO, acao);
			if(cotacao != null) {
				ultimoPreco = new Float(cotacao).floatValue();
				acaoVirtual.setUltimoPreco(ultimoPreco);
				acaoVirtual.setTotal(ultimoPreco * acao.getQuantidade());
				listaAcaoVirtual.add(acaoVirtual);
			}
		}
		
		return listaAcaoVirtual;
	}
}
