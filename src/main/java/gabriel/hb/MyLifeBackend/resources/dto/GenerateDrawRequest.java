package gabriel.hb.MyLifeBackend.resources.dto;

/* Classe para tratar a requisiçao que vem do frontend para gerar concurso */
public class GenerateDrawRequest {
	
	/* Atributos */
	private Long lastDrawId;
    private int evenCount;
    private int oddCount;
    private int repeatedCount;
    
    /* Construtores */
    public GenerateDrawRequest() {
    	
    }
    
	public GenerateDrawRequest(Long lastDrawId, int evenCount, int oddCount, int repeatedCount) {
		super();
		this.lastDrawId = lastDrawId;
		this.evenCount = evenCount;
		this.oddCount = oddCount;
		this.repeatedCount = repeatedCount;
	}

	/* Getters e Setters */
	public Long getLastDrawId() {
		return lastDrawId;
	}

	public void setLastDrawId(Long lastDrawId) {
		this.lastDrawId = lastDrawId;
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

}
