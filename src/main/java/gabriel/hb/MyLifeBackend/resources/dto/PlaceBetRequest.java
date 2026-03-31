package gabriel.hb.MyLifeBackend.resources.dto;

import java.time.LocalDate;
import java.util.List;

/* Classe para tratar a requisiçao que vem do frontend para adicionar concurso manualmente */
public class PlaceBetRequest {
	
	/* Atributos */
	private LocalDate betDate;
    private Long targetDrawId;
    private int oddCount;
    private int evenCount;
    private int repeatedCount;
    private List<Integer> betNumbers;

    /* Construtor padrão */
    public PlaceBetRequest() {
    }

    /* Getters e Setters */
	public LocalDate getBetDate() {
		return betDate;
	}

	public void setBetDate(LocalDate betDate) {
		this.betDate = betDate;
	}

	public Long getTargetDrawId() {
		return targetDrawId;
	}

	public void setTargetDrawId(Long targetDrawId) {
		this.targetDrawId = targetDrawId;
	}

	public int getOddCount() {
		return oddCount;
	}

	public void setOddCount(int oddCount) {
		this.oddCount = oddCount;
	}

	public int getEvenCount() {
		return evenCount;
	}

	public void setEvenCount(int evenCount) {
		this.evenCount = evenCount;
	}

	public int getRepeatedCount() {
		return repeatedCount;
	}

	public void setRepeatedCount(int repeatedCount) {
		this.repeatedCount = repeatedCount;
	}

	public List<Integer> getBetNumbers() {
		return betNumbers;
	}

	public void setBetNumbers(List<Integer> betNumbers) {
		this.betNumbers = betNumbers;
	}

}
