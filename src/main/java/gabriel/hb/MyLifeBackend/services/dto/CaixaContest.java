package gabriel.hb.MyLifeBackend.services.dto;

import java.util.List;

/* Classe para tratar os dados recebidos da API da Caixa na busca de concurso (usado na sincronização) */
public class CaixaContest {

	/* Atributos */
	private long numero;
	private List<String> listaDezenas;
	private String dataProximoConcurso;
	private Long numeroConcursoProximo;

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
}
