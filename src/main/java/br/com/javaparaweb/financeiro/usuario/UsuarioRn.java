package br.com.javaparaweb.financeiro.usuario;

import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.javaparaweb.financeiro.categoria.CategoriaRn;
import br.com.javaparaweb.financeiro.util.DaoFactory;
import br.com.javaparaweb.financeiro.util.EmailUtil;
import br.com.javaparaweb.financeiro.util.MensagemUtil;
import br.com.javaparaweb.financeiro.util.RNException;
import br.com.javaparaweb.financeiro.util.UtilException;

public class UsuarioRn {

	private UsuarioDao usuarioDao;
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public UsuarioRn() {
		usuarioDao = DaoFactory.criarUsuarioDao();
	}
	
	public Usuario carregar(Integer codigo) {
		return usuarioDao.carregar(codigo);
	}
	
	public Usuario buscarPorLogin(String login) {
		return usuarioDao.buscarPorLogin(login);
	}
	
	public void salvar(Usuario usuario) {
		
		Integer codigo = usuario.getCodigo();
		
		if(codigo == null || codigo == 0) {
			String hash = passwordEncoder.encode(usuario.getSenha());
			usuario.setSenha(hash);
			usuario.getPermissao().add("ROLE_USUARIO");
			usuarioDao.salvar(usuario);
			CategoriaRn categoriaRn = new CategoriaRn();
			categoriaRn.salvaEstruturaPadrao(usuario);
		}
			
		else
			usuarioDao.atualizar(usuario);
	}
	
	public void excluir(Usuario usuario) {
		CategoriaRn categoriaRn = new CategoriaRn();
		categoriaRn.excluir(usuario);
		usuarioDao.excluir(usuario);
	}
	
	public List<Usuario> listar(){
		return usuarioDao.listar();
	}
	
	public void enviarEmailPorCadastramento(Usuario usuario) throws RNException{
		
		//Enviando e-mail conforme idioma do usuario
		String[] info = usuario.getIdioma().split("_");
		Locale locale = new Locale(info[0], info[1]);
		String titulo = MensagemUtil.getMensagem(locale, "email_titulo");
		String mensagem = MensagemUtil.getMensagem(locale, "email_mensagem", usuario.getNome(),
				usuario.getLogin(), usuario.getSenha());
		try {
			EmailUtil emailUtil = new EmailUtil();
			emailUtil.enviarEmail(null, usuario.getEmail(), titulo, mensagem);
		}catch(UtilException e) {
			throw new RNException(e);
		}		
	}
}
