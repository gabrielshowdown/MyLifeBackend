package gabriel.hb.MyLifeBackend.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_totais_paridade_lotofacil")
public class LotofacilTotalsParities implements Serializable {
	private static final long serialVersionUID = 1L;

	// Atributos
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "paridade")
	private String parity;
	@Column(name = "qtd")
	private Integer quantity;
	@Column(name = "porcentagem")
	private Double percentage;

	// Construtores
	public LotofacilTotalsParities() {

	}

	public LotofacilTotalsParities(String parity, Integer quantity, Double percentage) {
		super();
		this.parity = parity;
		this.quantity = quantity;
		this.percentage = percentage;
	}

	// Métodos Acessores
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getParity() {
		return parity;
	}

	public void setParity(String parity) {
		this.parity = parity;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LotofacilTotalsParities other = (LotofacilTotalsParities) obj;
		return Objects.equals(id, other.id);
	}

}
