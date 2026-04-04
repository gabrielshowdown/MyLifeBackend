package gabriel.hb.MyLifeBackend.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_aposta_lotofacil")
public class LotofacilBet implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// Atributos
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long targetDrawId;
    private int evenCount;
	private int oddCount;
    private int repeatedCount;
    private double cost;
    private int hits;
    private boolean isChecked;
    private Double prize;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate betDate;
    @OneToMany(mappedBy = "bet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LotofacilBetNumber> betNumbers = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "concurso_id")
    private LotofacilDraw realDraw;

	
	// Construtores
    public LotofacilBet() {
    }


	public LotofacilBet(Long id, Long targetDrawId, int evenCount, int oddCount, int repeatedCount, double cost,
			int hits, boolean isChecked, Double prize, LocalDate betDate, List<LotofacilBetNumber> betNumbers,
			LotofacilDraw realDraw) {
		super();
		this.id = id;
		this.targetDrawId = targetDrawId;
		this.evenCount = evenCount;
		this.oddCount = oddCount;
		this.repeatedCount = repeatedCount;
		this.cost = cost;
		this.hits = hits;
		this.isChecked = isChecked;
		this.prize = prize;
		this.betDate = betDate;
		this.betNumbers = betNumbers;
		this.realDraw = realDraw;
	}




	// Métodos Acessores
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Long getTargetDrawId() {
		return targetDrawId;
	}



	public void setTargetDrawId(Long targetDrawId) {
		this.targetDrawId = targetDrawId;
	}



	public int getEvenCount() {
		return evenCount;
	}



	public void setEvenCount(int evenCount) {
		this.evenCount = evenCount;
	}



	public int getOddCount() {
		return oddCount;
	}



	public void setOddCount(int oddCount) {
		this.oddCount = oddCount;
	}



	public int getRepeatedCount() {
		return repeatedCount;
	}



	public void setRepeatedCount(int repeatedCount) {
		this.repeatedCount = repeatedCount;
	}



	public double getCost() {
		return cost;
	}



	public void setCost(double cost) {
		this.cost = cost;
	}



	public int getHits() {
		return hits;
	}



	public void setHits(int hits) {
		this.hits = hits;
	}



	public boolean isChecked() {
		return isChecked;
	}



	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}



	public LocalDate getBetDate() {
		return betDate;
	}



	public void setBetDate(LocalDate betDate) {
		this.betDate = betDate;
	}



	public LotofacilDraw getRealDraw() {
		return realDraw;
	}



	public void setRealDraw(LotofacilDraw realDraw) {
		this.realDraw = realDraw;
	}

	public Double getPrize() {
		return prize;
	}


	public void setPrize(Double prize) {
		this.prize = prize;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
    public List<LotofacilBetNumber> getBetNumbers() {
		return betNumbers;
	}

	public void setBetNumbers(List<LotofacilBetNumber> betNumbers) {
		this.betNumbers = betNumbers;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LotofacilBet other = (LotofacilBet) obj;
		return Objects.equals(id, other.id);
	}

}
