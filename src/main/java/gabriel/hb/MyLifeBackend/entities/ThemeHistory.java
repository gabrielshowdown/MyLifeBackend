package gabriel.hb.MyLifeBackend.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name="tb_historico_tema")
public class ThemeHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nome_tema")
    private String themeName;
    
    @Column(name = "data_criacao")
    private LocalDate creationDate;

    // O @ElementCollection cria uma tabela separada só para guardar as strings dessa lista amarradas ao ID do tema
    @ElementCollection
    @CollectionTable(name="tb_tema_primeira_leitura", joinColumns=@JoinColumn(name="tema_id"))
    @Column(name="leitura")
    private List<String> primeiraLeitura = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="tb_tema_segunda_leitura", joinColumns=@JoinColumn(name="tema_id"))
    @Column(name="leitura")
    private List<String> segundaLeitura = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="tb_tema_terceira_leitura", joinColumns=@JoinColumn(name="tema_id"))
    @Column(name="leitura")
    private List<String> terceiraLeitura = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="tb_tema_evangelho", joinColumns=@JoinColumn(name="tema_id"))
    @Column(name="leitura")
    private List<String> evangelhos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="tb_tema_descartado", joinColumns=@JoinColumn(name="tema_id"))
    @Column(name="leitura")
    private List<String> descartados = new ArrayList<>();

    public ThemeHistory() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public List<String> getPrimeiraLeitura() {
		return primeiraLeitura;
	}

	public void setPrimeiraLeitura(List<String> primeiraLeitura) {
		this.primeiraLeitura = primeiraLeitura;
	}

	public List<String> getSegundaLeitura() {
		return segundaLeitura;
	}

	public void setSegundaLeitura(List<String> segundaLeitura) {
		this.segundaLeitura = segundaLeitura;
	}

	public List<String> getTerceiraLeitura() {
		return terceiraLeitura;
	}

	public void setTerceiraLeitura(List<String> terceiraLeitura) {
		this.terceiraLeitura = terceiraLeitura;
	}

	public List<String> getEvangelhos() {
		return evangelhos;
	}

	public void setEvangelhos(List<String> evangelhos) {
		this.evangelhos = evangelhos;
	}

	public List<String> getDescartados() {
		return descartados;
	}

	public void setDescartados(List<String> descartados) {
		this.descartados = descartados;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ThemeHistory other = (ThemeHistory) obj;
		return Objects.equals(id, other.id);
	}

}