package br.com.javaparaweb.financeiro.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

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

	private static HttpClient client = HttpClients.createDefault();
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
					.addParameter("function", "GLOBAL_QUOTE")
					.addParameter("symbol", sigla)
					.addParameter("apikey", KEY)
					.build();
		
		HttpGet httpGet = new HttpGet(uri);
		
		try {
			HttpResponse response = client.execute(httpGet);
			
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) 
				throw new UtilException("Falha ao se comunicar com a API Alpha Vantage.");
			
			String content = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(content);
			
			cotacao = json.getJSONObject("Global Quote")
					.getString("05. price");
			
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
	
	public static Map<Date, String> getVariacaoDiaria(Acao acao) throws URISyntaxException, UtilException{
		
		String sigla = getSiglaLink(acao);		
		Map<Date, String> valores = new LinkedHashMap<Date, String>();
		
		URI uri = new URIBuilder()
					.setScheme("https")
					.setHost("www.alphavantage.co/query")
					.addParameter("function", "TIME_SERIES_INTRADAY")
					.addParameter("symbol", sigla)
					.addParameter("interval", "5min")
					.addParameter("apikey", KEY)
					.build();
		
		HttpGet httpGet = new HttpGet(uri);
		
		try {
			HttpResponse response = client.execute(httpGet);
			
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) 
				throw new UtilException("Falha ao se comunicar com a API Alpha Vantage.");
			
			String content = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(content);
			JSONObject cotacoes = (JSONObject) json.get("Time Series (5min)");
			Iterator<String> iterator = cotacoes.keys();
			String key = null;
			int contador = 0;
			
			while(contador < 10) {
				key = (String) iterator.next();
				Date data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(key);		
				
				valores.put(data, json.getJSONObject("Time Series (5min)").
						getJSONObject(key).getString("4. close"));
				
				contador++;
			}

			
		}catch(Exception e) {
			throw new UtilException(e.getMessage());
		}finally {
			httpGet.releaseConnection();
		}		
		return valores;			
	}
}
