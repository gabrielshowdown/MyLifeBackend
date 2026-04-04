package gabriel.hb.MyLifeBackend.services.dto;

import java.util.List;

/* Classe para tratar os dados recebidos da API da Caixa na busca de concurso (usado na sincronização) 
 * Ou seja, o que vem aqui (os atributos) são oriundos de lá */
public class CaixaDraw {

	/* Atributos */
	private long numero;
	private List<String> listaDezenas;
	private String dataProximoConcurso;
	private Long numeroConcursoProximo;
	private String dataApuracao;
	private List<RateioPremio> listaRateioPremio;

	/* Getters e Setters */
	public long getNumero() {
		return numero;
	}

	public void setNumero(long numero) {
		this.numero = numero;
	}

	public List<String> getListaDezenas() {
		return listaDezenas;
	}

	public void setListaDezenas(List<String> listaDezenas) {
		this.listaDezenas = listaDezenas;
	}

	public String getDataProximoConcurso() {
		return dataProximoConcurso;
	}

	public void setDataProximoConcurso(String dataProximoConcurso) {
		this.dataProximoConcurso = dataProximoConcurso;
	}

	public Long getNumeroConcursoProximo() {
		return numeroConcursoProximo;
	}

	public void setNumeroConcursoProximo(Long numeroConcursoProximo) {
		this.numeroConcursoProximo = numeroConcursoProximo;
	}
	
	public String getDataApuracao() {
		return dataApuracao;
	}

	public void setDataApuracao(String dataApuracao) {
		this.dataApuracao = dataApuracao;
	}
	
	public List<RateioPremio> getListaRateioPremio() {
        return listaRateioPremio;
    }

    public void setListaRateioPremio(List<RateioPremio> listaRateioPremio) {
        this.listaRateioPremio = listaRateioPremio;
    }
    
    public static class RateioPremio {
        private String descricaoFaixa; // Ex: "15 acertos", "14 acertos"
        private int numeroDeGanhadores;
        private double valorPremio;

        public String getDescricaoFaixa() { return descricaoFaixa; }
        public void setDescricaoFaixa(String descricaoFaixa) { this.descricaoFaixa = descricaoFaixa; }
        public int getNumeroDeGanhadores() { return numeroDeGanhadores; }
        public void setNumeroDeGanhadores(int numeroDeGanhadores) { this.numeroDeGanhadores = numeroDeGanhadores; }
        public double getValorPremio() { return valorPremio; }
        public void setValorPremio(double valorPremio) { this.valorPremio = valorPremio; }
    }
}
