package gabriel.hb.MyLifeBackend.entities;

import java.io.Serializable;
import java.util.Objects;

import gabriel.hb.MyLifeBackend.entities.enums.ReadingCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_livro_biblico")
public class BookBible implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// Atributos
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	// Caso quisesse mudar o nome da coluna -> @Column(name="nomecompleto")
	@Column(name = "sigla")
	private String abbreviation;
	@Column(name = "nome")
	private String name;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "categoria_leitura")
    private ReadingCategory category;
	
	// Construtores
	public BookBible() {}

    public BookBible(Long id, String abbreviation, String name, ReadingCategory category) {
        this.id = id;
        this.abbreviation = abbreviation;
        this.name = name;
        this.category = category;
    }
	
	// Métodos Acessores
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ReadingCategory getCategory() {
		return category;
	}

	public void setCategory(ReadingCategory category) {
		this.category = category;
	}
	
	// Métodos Acessores
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
		BookBible other = (BookBible) obj;
		return Objects.equals(id, other.id);
	}

}
