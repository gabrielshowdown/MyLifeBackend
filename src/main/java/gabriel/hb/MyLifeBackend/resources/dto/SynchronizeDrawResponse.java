package gabriel.hb.MyLifeBackend.resources.dto;

import java.time.LocalDate;

/* Classe para tratar a resposta para o frontend de quando for sincronizado os concursos */
public class SynchronizeDrawResponse {
	
	/* Atributos */
	private Long lastSavedDrawId;
    private int synchronizedDrawsCount; // Total de concursos sincronizados
    private LocalDate nextDrawDate;
    private String syncMessage;
    private Long nextDrawId;
    
    /* Construtores */
    public SynchronizeDrawResponse() {
    	
    }

	public SynchronizeDrawResponse(Long lastSavedDrawId, int synchronizedDrawsCount, LocalDate nextDrawDate, String syncMessage, Long nextDrawId) {
		super();
		this.lastSavedDrawId = lastSavedDrawId;
		this.synchronizedDrawsCount = synchronizedDrawsCount;
		this.nextDrawDate = nextDrawDate;
		this.syncMessage = syncMessage;
		this.nextDrawId = nextDrawId;
	}

	/* Getters e Setters */
	public Long getLastSavedDrawIdo() {
		return lastSavedDrawId;
	}

	public void setLastSavedDrawId(Long lastSavedDrawId) {
		this.lastSavedDrawId = lastSavedDrawId;
	}

	public int getSynchronizedDrawsCount() {
		return synchronizedDrawsCount;
	}

	public void setSynchronizedDrawsCount(int synchronizedDrawsCount) {
		this.synchronizedDrawsCount = synchronizedDrawsCount;
	}

	public LocalDate getNextDrawDate() {
		return nextDrawDate;
	}

	public void setNextDrawDate(LocalDate nextDrawDate) {
		this.nextDrawDate = nextDrawDate;
	}

	public String getSyncMessaged() {
		return syncMessage;
	}

	public void setSyncMessage(String syncMessage) {
		this.syncMessage = syncMessage;
	}

	public Long getNextDrawId() {
		return nextDrawId;
	}

	public void setNextDrawId(Long nextDrawId) {
		this.nextDrawId = nextDrawId;
	}

    
}
