package gabriel.hb.MyLifeBackend.entities;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_numero_aposta_lotofacil")
public class LotofacilBetNumber implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// Atributos
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name = "numero")
    private int number;
	@Column(name = "repetido")
    private boolean isRepeated;
	@Column(name = "acertado")
    private boolean wasCorrectly;

    @ManyToOne
    @JoinColumn(name = "aposta_id")
    private LotofacilBet bet;
	
	// Construtores
    public LotofacilBetNumber() {
    }

    public LotofacilBetNumber(int number, boolean isRepeated, LotofacilBet bet) {
        this.number = number;
        this.isRepeated = isRepeated;
        this.bet = bet;
    }

	// Métodos Acessores
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    public boolean getIsRepeated() {
        return isRepeated;
    }

    public void setIsRepeated(boolean isRepeated) {
        this.isRepeated = isRepeated;
    }

    // o Jackson usa os métodos getters para serializar
    @JsonIgnore
    public LotofacilBet getBet() {
        return bet;
    }

    public void setBet(LotofacilBet bet) {
        this.bet = bet;
    }

    
	public boolean isWasCorrectly() {
		return wasCorrectly;
	}

	public void setWasCorrectly(boolean wasCorrectly) {
		this.wasCorrectly = wasCorrectly;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LotofacilBetNumber other = (LotofacilBetNumber) obj;
		return Objects.equals(id, other.id);
	}

}
