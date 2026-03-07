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
@Table(name="tb_usuario")
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// Atributos
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	// Caso quisesse mudar o nome da coluna -> @Column(name="nomecompleto")
	private String username;
	@Column(name = "senha")
	private String password;
	@Column(name = "genero")
	private String gender;
	@Column(name = "localizacao")
	private String location;
	@Column(name = "data_nascimento") 
	private LocalDate birthDate;
	
	// Construtores
	public User() {
		
	}
		
	public User(Long id, String username, String password, String gender, String location, LocalDate birthDate) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.gender = gender;
		this.location = location;
		this.birthDate = birthDate;
	}
	
	// Métodos Acessores
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setData(LocalDate birthDate) {
		this.birthDate = birthDate;
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
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}

}
