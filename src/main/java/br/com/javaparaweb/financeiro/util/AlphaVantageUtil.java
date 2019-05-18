package br.com.javaparaweb.financeiro.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import br.com.javaparaweb.financeiro.bolsa.acao.Acao;

public class AlphaVantageUtil {

	private static final String KEY = "XC5MVLREL74KNLOR"; 
	public static final Character ORIGEM_BOVESPA = 'B';
	public static final String SIGLA_BOVESPA = "^BVSP";
	public static final String SUFIXO_ACAO_BOVESPA = ".SA";
	
	public static String getInfoCotacao(Acao acao) throws UtilException, URISyntaxException{
		
		String sigla = getSiglaLink(acao);		
		String cotacao = null;
		URI uri = new URIBuilder()
					.setScheme("https")
					.setHost("www.alphavantage.co/query")
					.addParameter("function", "TIME_SERIES_INTRADAY")
					.addParameter("symbol", sigla)
					.addParameter("interval", "60min")
					.addParameter("apikey", KEY)
					.build();
		
		HttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(uri);
		
		try {
			HttpResponse response = client.execute(httpGet);
			
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) 
				throw new UtilException("Falha ao se comunicar com a API Alpha Vantage.");
			
			String content = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(content);
			
			String ultimaAtualizacao = json.getJSONObject("Meta Data")
					.getString("3. Last Refreshed");
			
			cotacao = json.getJSONObject("Time Series (60min)")
					.getJSONObject(ultimaAtualizacao)
					.getString("4. close");
			
		}catch(Exception e) {
			throw new UtilException(e.getMessage());
		}finally {
			httpGet.releaseConnection();
		}		
		return cotacao;			
	}
	
	public static String getSiglaLink(Acao acao) {
		
		if(acao == null || acao.getSigla() == null)
			return SIGLA_BOVESPA;
		else if(acao.getOrigem() == ORIGEM_BOVESPA)
			return acao.getSigla() + SUFIXO_ACAO_BOVESPA;
		
		return acao.getSigla();
	}
}
