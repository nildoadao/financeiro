package br.com.javaparaweb.financeiro.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class MensagemUtil {
	
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
}
