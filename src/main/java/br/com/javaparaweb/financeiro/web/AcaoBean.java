package br.com.javaparaweb.financeiro.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

import br.com.javaparaweb.financeiro.bolsa.acao.Acao;
import br.com.javaparaweb.financeiro.bolsa.acao.AcaoRn;
import br.com.javaparaweb.financeiro.bolsa.acao.AcaoVirtual;
import br.com.javaparaweb.financeiro.util.AlphaVantageUtil;

@ManagedBean(name = "acaoBean")
@RequestScoped
public class AcaoBean {
	
	private static final Logger log = Logger.getLogger(AcaoBean.class.getName());
	private AcaoVirtual selecionada = new AcaoVirtual();
	private List<AcaoVirtual> lista = null;
	private SortedMap<Date, String> valoresDiario = null;
	private SortedMap<Date, String> valoresTrimestral = null;
	private SortedMap<Date, String> valoresSemestral = null;
	private SortedMap<Date, String> valoresAnual = null;
	private String linkCodigoAcao = null;
	private PieChartModel percentualQuantidade = new PieChartModel();
	private PieChartModel percentualValor = new PieChartModel();
	private LineChartModel variacaoDiaria = new LineChartModel();
	private LineChartModel variacaoTrimestral = new LineChartModel();
	private LineChartModel variacaoSemestral = new LineChartModel();
	private LineChartModel variacaoAnual = new LineChartModel();
	
	@ManagedProperty(value = "#{contextoBean}")
	private ContextoBean contextoBean;
	
	public void salvar() {
		AcaoRn acaoRn = new AcaoRn();
		Acao acao = this.selecionada.getAcao();
		acao.setSigla(acao.getSigla().toUpperCase());
		acao.setUsuario(contextoBean.getUsuarioLogado());
		acaoRn.salvar(acao);
		this.selecionada = new AcaoVirtual();
		this.lista = null;
	}
	
	public void excluir() {
		AcaoRn acaoRn = new AcaoRn();
		acaoRn.excluir(this.selecionada.getAcao());
		this.selecionada = new AcaoVirtual();
		this.lista = null;
	}
	
	public List<AcaoVirtual> getLista(){
		FacesContext context = FacesContext.getCurrentInstance();
		AcaoRn acaoRn = new AcaoRn();
		try {
			if(this.lista == null)
				this.lista = acaoRn.listarAcaoVirtual(contextoBean.getUsuarioLogado());
		}catch(Exception e) {
			context.addMessage(null, new FacesMessage(e.getMessage()));
		}
		return this.lista;
	}
	
	public String getLinkCodigoAcao() {
		this.linkCodigoAcao = AlphaVantageUtil.getSiglaLink(this.selecionada.getAcao());
		return this.linkCodigoAcao;
	}
	
	public PieChartModel getPercentualQuantidade() {
		List<AcaoVirtual> acoes = this.getLista();
		if(acoes.size() > 0) {
			this.percentualQuantidade.setLegendPosition("e");
			this.percentualQuantidade.setShowDataLabels(true);
			this.percentualQuantidade.setDataFormat("percent");
			
			for(AcaoVirtual acaoVirtual : acoes) {
				Acao acao = acaoVirtual.getAcao();
				this.percentualQuantidade.set(acao.getSigla(), acao.getQuantidade());
			}
		}
		return this.percentualQuantidade;
	}
	
	public PieChartModel getPercentualValor() {
		List<AcaoVirtual> acoes = this.getLista();
		if(acoes.size() > 0) {
			this.percentualValor.setLegendPosition("e");
			this.percentualValor.setShowDataLabels(true);
			this.percentualValor.setDataFormat("percent");
			
			for(AcaoVirtual acaoVirtual : acoes) {
				Acao acao = acaoVirtual.getAcao();
				this.percentualValor.set(acao.getSigla(), acaoVirtual.getTotal());
			}
		}
		return this.percentualValor;
	}
	
	public LineChartModel getVariacaoDiaria() {		
		FacesContext context = FacesContext.getCurrentInstance();
		
		try {
			SortedMap<Date, String> valores = this.getValoresDiario();		
			LineChartSeries serieGrafico = new LineChartSeries("Variação Diaria");
			int contador = 0;
			
			for(Date data : valores.keySet()) {				
				contador++;
				
				// considera apenas as ultimas 15 medições
				if(contador > valores.size() - 15) {
					serieGrafico.set(new SimpleDateFormat("HH:mm").format(data), 
							new Float(valores.get(data)).floatValue());
				}			
			}
			
			this.variacaoDiaria.setTitle(this.getLinkCodigoAcao());
			this.variacaoDiaria.setLegendPosition("ne");
			this.variacaoDiaria.setStacked(true);
			this.variacaoDiaria.addSeries(serieGrafico);
			
	        Axis xAxis = new CategoryAxis("Hora");
	        xAxis.setTickAngle(60);
	        this.variacaoDiaria.getAxes().put(AxisType.X, xAxis);
	        Axis yAxis = this.variacaoDiaria.getAxis(AxisType.Y);
	        yAxis.setLabel("Valor");

			
		}catch(Exception e) {
			log.severe(e.getMessage());
			context.addMessage(null, new FacesMessage(e.getMessage()));
		}
		
		return variacaoDiaria;
	}
	
	
	public LineChartModel getVariacaoTrimestral() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		try {
			SortedMap<Date, String> valores = this.getValoresTrimestral();		
			LineChartSeries serieGrafico = new LineChartSeries("Variação Trimestral");
			int contador = 0;
			
			for(Date data : valores.keySet()) {				
				contador++;
				
				// considera apenas as ultimas 12 medições
				if(contador > valores.size() - 12) {
					serieGrafico.set(new SimpleDateFormat("dd/MM").format(data), 
							new Float(valores.get(data)).floatValue());
				}			
			}
			
			this.variacaoTrimestral.setTitle(this.getLinkCodigoAcao());
			this.variacaoTrimestral.setLegendPosition("ne");
			this.variacaoTrimestral.setStacked(true);
			this.variacaoTrimestral.addSeries(serieGrafico);
			
	        Axis xAxis = new CategoryAxis("Dia");
	        xAxis.setTickAngle(60);
	        this.variacaoTrimestral.getAxes().put(AxisType.X, xAxis);
	        Axis yAxis = this.variacaoTrimestral.getAxis(AxisType.Y);
	        yAxis.setLabel("Valor");

			
		}catch(Exception e) {
			log.severe(e.getMessage());
			context.addMessage(null, new FacesMessage(e.getMessage()));
		}
		
		return variacaoTrimestral;
	}

	public LineChartModel getVariacaoSemestral() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		try {
			SortedMap<Date, String> valores = this.getValoresSemestral();		
			LineChartSeries serieGrafico = new LineChartSeries("Variação Semestral");
			int contador = 0;
			
			for(Date data : valores.keySet()) {				
				contador++;
				
				// considera apenas as ultimas 6 medições
				if(contador > valores.size() - 6) {
					serieGrafico.set(new SimpleDateFormat("MM/yy").format(data), 
							new Float(valores.get(data)).floatValue());
				}
				
			}
			
			this.variacaoSemestral.setTitle(this.getLinkCodigoAcao());
			this.variacaoSemestral.setLegendPosition("ne");
			this.variacaoSemestral.setStacked(true);
			this.variacaoSemestral.addSeries(serieGrafico);
			
	        Axis xAxis = new CategoryAxis("Mês");
	        xAxis.setTickAngle(60);
	        this.variacaoSemestral.getAxes().put(AxisType.X, xAxis);
	        Axis yAxis = this.variacaoSemestral.getAxis(AxisType.Y);
	        yAxis.setLabel("Valor");

			
		}catch(Exception e) {
			log.severe(e.getMessage());
			context.addMessage(null, new FacesMessage(e.getMessage()));
		}
		
		return variacaoSemestral;
	}

	public LineChartModel getVariacaoAnual() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		try {
			SortedMap<Date, String> valores = this.getValoresAnual();		
			LineChartSeries serieGrafico = new LineChartSeries("Variação Anual");
			int contador = 0;
			
			for(Date data : valores.keySet()) {				
				contador++;
				
				// considera apenas as ultimas 12 medições
				if(contador > valores.size() - 12) {
					serieGrafico.set(new SimpleDateFormat("MM/yy").format(data), 
							new Float(valores.get(data)).floatValue());
				}			
			}
			
			this.variacaoAnual.setTitle(this.getLinkCodigoAcao());
			this.variacaoAnual.setLegendPosition("ne");
			this.variacaoAnual.setStacked(true);
			this.variacaoAnual.addSeries(serieGrafico);
			
	        Axis xAxis = new CategoryAxis("Mês");
	        xAxis.setTickAngle(60);
	        this.variacaoAnual.getAxes().put(AxisType.X, xAxis);
	        Axis yAxis = this.variacaoAnual.getAxis(AxisType.Y);
	        yAxis.setLabel("Valor");

			
		}catch(Exception e) {
			log.severe(e.getMessage());
			context.addMessage(null, new FacesMessage(e.getMessage()));
		}
		
		return variacaoAnual;
	}

	public SortedMap<Date, String> getValoresDiario() {
		
		if(this.valoresDiario == null) {
			log.log(Level.INFO, "Obtendo dados da API AlphaVantage");
			try {
				valoresDiario = new TreeMap<Date, String>(AlphaVantageUtil.getVariacaoDiaria(this.selecionada.getAcao()));
			}catch(Exception e) {}
		}
		return this.valoresDiario;
	}
	
	
	public SortedMap<Date, String> getValoresTrimestral() {
		if(this.valoresTrimestral == null) {
			log.log(Level.INFO, "Obtendo dados da API AlphaVantage");
			try {
				valoresTrimestral = new TreeMap<Date, String>(AlphaVantageUtil.getVariacaoSemanal(this.selecionada.getAcao()));
			}catch(Exception e) {}
		}
		return this.valoresTrimestral;
	}

	public SortedMap<Date, String> getValoresSemestral() {
		if(this.valoresSemestral == null) {
			log.log(Level.INFO, "Obtendo dados da API AlphaVantage");
			try {
				valoresSemestral = new TreeMap<Date, String>(AlphaVantageUtil.getVariacaoMensal(this.selecionada.getAcao()));
			}catch(Exception e) {}
		}
		return this.valoresSemestral;
	}

	public SortedMap<Date, String> getValoresAnual() {
		if(this.valoresAnual == null) {
			log.log(Level.INFO, "Obtendo dados da API AlphaVantage");
			try {
				valoresAnual = new TreeMap<Date, String>(AlphaVantageUtil.getVariacaoMensal(this.selecionada.getAcao()));
			}catch(Exception e) {}
		}
		return this.valoresAnual;
	}

	public void setValoresDiario(SortedMap<Date, String> valoresDiario) {
		this.valoresDiario = valoresDiario;
	}

	public void setVariacaoDiaria(LineChartModel variacaoDiaria) {
		this.variacaoDiaria = variacaoDiaria;
	}

	public AcaoVirtual getSelecionada() {
		return selecionada;
	}

	public void setSelecionada(AcaoVirtual selecionada) {
		this.selecionada = selecionada;
		this.valoresDiario = null;
	}

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}

	public void setLista(List<AcaoVirtual> lista) {
		this.lista = lista;
	}

	public void setLinkCodigoAcao(String linkCodigoAcao) {
		this.linkCodigoAcao = linkCodigoAcao;
	}

	public void setPercentualQuantidade(PieChartModel percentualQuantidade) {
		this.percentualQuantidade = percentualQuantidade;
	}

	public void setPercentualValor(PieChartModel percentualValor) {
		this.percentualValor = percentualValor;
	}
	
}
