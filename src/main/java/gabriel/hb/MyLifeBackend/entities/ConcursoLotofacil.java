package gabriel.hb.MyLifeBackend.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_concurso_lotofacil")
public class ConcursoLotofacil implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// Atributos
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int qtd_pares;
    private int qtd_Impares;
    private int qtd_Repetidos;
    
    @JsonIgnore
    @OneToMany(mappedBy = "concurso", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<NumeroConcursoLotofacil> numerosConcurso = new ArrayList<>();

	
	// Construtores
    public ConcursoLotofacil() {
    }

    public ConcursoLotofacil(int qtd_pares, int qtd_Impares) {
        this.qtd_pares = qtd_pares;
        this.qtd_Impares = qtd_Impares;
    }

	// Métodos Acessores
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getqtd_pares() {
		return qtd_pares;
	}

	public void setqtd_pares(int qtd_pares) {
		this.qtd_pares = qtd_pares;
	}

	public int getQtdImpares() {
		return qtd_Impares;
	}

	public void setQtdImpares(int qtdImpares) {
		this.qtd_Impares = qtdImpares;
	}

	public int getQtdRepetidos() {
		return qtd_Repetidos;
	}

	public void setQtdRepetidos(int qtdRepetidos) {
		this.qtd_Repetidos = qtdRepetidos;
	}

	public List<NumeroConcursoLotofacil> getNumerosConcurso() {
		return numerosConcurso;
	}

	public void setNumerosConcurso(List<NumeroConcursoLotofacil> numerosConcurso) {
		this.numerosConcurso = numerosConcurso;
	}

	public void adicionarNumeroSorteio(NumeroConcursoLotofacil numeroSorteio) {
        numeroSorteio.setSorteio(this);
        numerosConcurso.add(numeroSorteio);
    }

    public void removerNumeroSorteio(NumeroConcursoLotofacil numeroSorteio) {
    	numerosConcurso.remove(numeroSorteio);
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConcursoLotofacil other = (ConcursoLotofacil) obj;
		return Objects.equals(id, other.id);
	}

}
