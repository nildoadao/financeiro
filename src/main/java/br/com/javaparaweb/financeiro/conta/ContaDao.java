package br.com.javaparaweb.financeiro.conta;

import java.util.List;

import br.com.javaparaweb.financeiro.usuario.Usuario;

public interface ContaDao {

	public void salvar(Conta conta);
	public void excluir(Conta conta);
	public Conta carregar(Integer id);
	public List<Conta> listar(Usuario usuario);
	public Conta buscarFavorita(Usuario usuario);
}
