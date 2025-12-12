package gabriel.hb.MyLifeBackend.entities;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_numero_concurso_lotofacil")
public class NumeroConcursoLotofacil implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// Atributos
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numero;
    private boolean repetido;

    @ManyToOne
    private ConcursoLotofacil concurso;
	
	// Construtores
    public NumeroConcursoLotofacil() {
    }

    public NumeroConcursoLotofacil(int numero, boolean repetido, ConcursoLotofacil sorteio) {
        this.numero = numero;
        this.repetido = repetido;
        this.concurso = sorteio;
    }

	// Métodos Acessores
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
    
    public boolean getRepetido() {
        return repetido;
    }

    public void setRepetido(boolean repetido) {
        this.repetido = repetido;
    }

    // o Jackson usa os métodos getters para serializar
    @JsonIgnore
    public ConcursoLotofacil getSorteio() {
        return concurso;
    }

    public void setSorteio(ConcursoLotofacil concurso) {
        this.concurso = concurso;
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NumeroConcursoLotofacil other = (NumeroConcursoLotofacil) obj;
		return Objects.equals(id, other.id);
	}

}
