package br.com.javaparaweb.financeiro.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.com.javaparaweb.financeiro.categoria.Categoria;
import br.com.javaparaweb.financeiro.categoria.CategoriaRn;

import javax.faces.convert.*;

@FacesConverter(forClass = Categoria.class)
public class CategoriaConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if(value != null && value.trim().length() > 0) {
			Integer codigo = Integer.valueOf(value);
			try {
				CategoriaRn categoriaRn = new CategoriaRn();
				return categoriaRn.carregar(codigo);
			}
			catch(Exception e) {
				throw new ConverterException(String.format("Não foi possivel encontrar a categoria de código {0}. {1]",
						value, e.getMessage()));
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value != null) {
			Categoria categoria = (Categoria) value;
			return categoria.getCodigo().toString();
		}
		return "";
	}

}
