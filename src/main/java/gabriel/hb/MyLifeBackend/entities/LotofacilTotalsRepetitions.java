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
@Table(name = "tb_totais_repeticoes_lotofacil")
public class LotofacilTotalsRepetitions implements Serializable {
	private static final long serialVersionUID = 1L;

	// Atributos
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "repetido")
	private Integer repeated;
	@Column(name = "qtd")
	private Integer quantity;
	@Column(name = "porcentagem")
	private Double percentage;

	// Construtores
	public LotofacilTotalsRepetitions() {

	}

	public LotofacilTotalsRepetitions(Integer repeated, Integer quantity, Double percentage) {
		super();
		this.repeated = repeated;
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

	public Integer getRepeated() {
		return repeated;
	}

	public void setRepeated(Integer repeated) {
		this.repeated = repeated;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQtd(Integer quantity) {
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
		LotofacilTotalsRepetitions other = (LotofacilTotalsRepetitions) obj;
		return Objects.equals(id, other.id);
	}

}
