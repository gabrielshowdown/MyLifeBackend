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
@Table(name="tb_totais_numeros_lotofacil")
public class LotofacilTotalsNumbers implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// Atributos
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	@Column(name = "qtd")
	private Integer quantity;
	@Column(name = "porcentagem")
	private Double percentage;
	
	// Construtores
    public LotofacilTotalsNumbers() {
    	
    }
    
    public LotofacilTotalsNumbers(int quantity, double percentage) {
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
		LotofacilTotalsNumbers other = (LotofacilTotalsNumbers) obj;
		return Objects.equals(id, other.id);
	}

}
