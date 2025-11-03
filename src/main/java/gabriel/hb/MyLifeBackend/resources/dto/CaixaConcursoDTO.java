package gabriel.hb.MyLifeBackend.resources.dto;

import java.util.List;

public class CaixaConcursoDTO {

	private long numero;
	private List<String> listaDezenas;

	// Getters e Setters
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
}
