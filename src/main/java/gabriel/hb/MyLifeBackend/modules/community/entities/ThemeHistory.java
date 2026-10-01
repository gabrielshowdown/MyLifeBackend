package gabriel.hb.MyLifeBackend.modules.community.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name="tb_cm_historico_tema")
public class ThemeHistory implements Serializable {
    private static final long serialVersionUID = 1L;

	/* Atributos */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome_tema")
    private String themeName;
    @Column(name = "data_criacao")
    private LocalDate creationDate;
    @Column(name = "data_celebracao")
    private LocalDate celebrationDate;

    /* O @ElementCollection cria uma tabela separada só para guardar as strings dessa lista amarradas ao ID do tema */
    @ElementCollection
    @CollectionTable(name="tb_cm_tema_primeira_leitura", joinColumns=@JoinColumn(name="tema_id"))
    @Column(name="leitura")
    private List<String> firstReading = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="tb_cm_tema_segunda_leitura", joinColumns=@JoinColumn(name="tema_id"))
    @Column(name="leitura")
    private List<String> secondReading = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="tb_cm_tema_terceira_leitura", joinColumns=@JoinColumn(name="tema_id"))
    @Column(name="leitura")
    private List<String> thirdReading = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="tb_cm_tema_evangelho", joinColumns=@JoinColumn(name="tema_id"))
    @Column(name="leitura")
    private List<String> gospel = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="tb_cm_tema_descartado", joinColumns=@JoinColumn(name="tema_id"))
    @Column(name="leitura")
    private List<String> discarded = new ArrayList<>();

	/* Construtor */
    public ThemeHistory() {}

	/* Métodos Acessores */
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

	public LocalDate getCelebrationDate() {
		return celebrationDate;
	}

	public void setCelebrationDate(LocalDate celebrationDate) {
		this.celebrationDate = celebrationDate;
	}
	
	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public List<String> getFirstReading() {
		return firstReading;
	}

	public void setFirstReading(List<String> firstReading) {
		this.firstReading = firstReading;
	}

	public List<String> getSecondReading() {
		return secondReading;
	}

	public void setSecondReading(List<String> secondReading) {
		this.secondReading = secondReading;
	}

	public List<String> getThirdReading() {
		return thirdReading;
	}

	public void setThirdReading(List<String> thirdReading) {
		this.thirdReading = thirdReading;
	}

	public List<String> getGospel() {
		return gospel;
	}

	public void setGospel(List<String> gospel) {
		this.gospel = gospel;
	}

	public List<String> getDiscarded() {
		return discarded;
	}

	public void setDiscarded(List<String> discarded) {
		this.discarded = discarded;
	}

	/* Métodos Comparativos */
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