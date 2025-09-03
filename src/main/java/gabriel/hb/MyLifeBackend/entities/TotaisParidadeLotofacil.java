package gabriel.hb.MyLifeBackend.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_totais_paridade_lotofacil")
public class TotaisParidadeLotofacil implements Serializable {
	private static final long serialVersionUID = 1L;

	// Atributos
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String paridade;
	private Integer qtd;
	private Double porcentagem;

	// Construtores
	public TotaisParidadeLotofacil() {

	}

	public TotaisParidadeLotofacil(String paridade, Integer qtd, Double porcentagem) {
		super();
		this.paridade = paridade;
		this.qtd = qtd;
		this.porcentagem = porcentagem;
	}

	// Métodos Acessores
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getParidade() {
		return paridade;
	}

	public void setParidade(String paridade) {
		this.paridade = paridade;
	}

	public Integer getQtd() {
		return qtd;
	}

	public void setQtd(Integer qtd) {
		this.qtd = qtd;
	}

	public Double getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Double porcentagem) {
		this.porcentagem = porcentagem;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TotaisParidadeLotofacil other = (TotaisParidadeLotofacil) obj;
		return Objects.equals(id, other.id);
	}

}
