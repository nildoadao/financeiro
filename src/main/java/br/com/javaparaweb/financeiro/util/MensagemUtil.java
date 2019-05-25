package br.com.javaparaweb.financeiro.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class MensagemUtil {
	
	private static final String PACOTE_MENSAGENS_IDIOMAS = "br.com.javaparaweb"
			+ ".financeiro.idioma.mensagens";
	
	public static String getMensagem(String propiedade) {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
		return bundle.getString(propiedade);
	}

	public static String getMensagem(String propiedade, Object...parametros) {
		String mensagem = getMensagem(propiedade);
		MessageFormat formatter = new MessageFormat(mensagem);
		return formatter.format(parametros);
	}
	
	public static String getMensagem(Locale locale, String propiedade) {
		ResourceBundle bundle = ResourceBundle.getBundle(PACOTE_MENSAGENS_IDIOMAS, locale);
		return bundle.getString(propiedade);
	}
	
	public static String getMensagem(Locale locale, String propiedade, Object...parametros) {
		String mensagem = getMensagem(locale, propiedade);
		MessageFormat formatter = new MessageFormat(mensagem);
		return formatter.format(parametros);
	}
}
