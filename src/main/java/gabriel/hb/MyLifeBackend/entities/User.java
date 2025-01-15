package gabriel.hb.MyLifeBackend.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

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
	private String senha;
	private String genero;
	private String localizacao;
	private LocalDate dataNascimento;
	
	// Construtores
	public User() {
		
	}
		
	public User(Long id, String username, String senha, String genero, String localizacao, LocalDate dataNascimento) {
		super();
		this.id = id;
		this.username = username;
		this.senha = senha;
		this.genero = genero;
		this.localizacao = localizacao;
		this.dataNascimento = dataNascimento;
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
	
	public LocalDate getDataNascimento() {
		return dataNascimento;
	}
	public void setData(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
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
