package gabriel.hb.MyLifeBackend.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_item")
public class Item implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// Atributos
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	// Caso quisesse mudar o nome da coluna -> @Column(name="nomecompleto")
	@Column(name = "nome")
	private String name;
	@Column(name = "observacao")
	private String observation;
	@Column(name = "categoria")
	private String category;
	@Column(name = "imagem")
	private String image;
	@Column(name = "validade")
	private LocalDate validity;
	
	// Construtores
	public Item() {
		
	}
		
	

	public Item(Long id, String name, String observation, String category, String image, LocalDate validity) {
		super();
		this.id = id;
		this.name = name;
		this.observation = observation;
		this.category = category;
		this.image = image;
		this.validity = validity;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getObservation() {
		return observation;
	}



	public void setObservation(String observation) {
		this.observation = observation;
	}



	public String getCategory() {
		return category;
	}



	public void setCategory(String category) {
		this.category = category;
	}



	public String getImage() {
		return image;
	}



	public void setImage(String image) {
		this.image = image;
	}



	public LocalDate getValidity() {
		return validity;
	}



	public void setValidity(LocalDate validity) {
		this.validity = validity;
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
		Item other = (Item) obj;
		return Objects.equals(id, other.id);
	}
}
