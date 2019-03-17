package br.com.javaparaweb.financeiro.util;

public class DaoException extends Exception {

	private static final long serialVersionUID = -7220499276256043381L;

	public DaoException() {
	}

	public DaoException(String arg0) {
		super(arg0);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
