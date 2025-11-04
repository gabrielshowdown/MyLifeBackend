package gabriel.hb.MyLifeBackend.resources.dto;

import java.time.LocalDate;

public class SyncronizeContestResponseDTO {
	
	private Long lastConcCadastrado;
    private int totContestSyncronized;
    private LocalDate dateNextContest;
    private String textReturnedSyoncronized;
    private Long nextContest;
    
    public SyncronizeContestResponseDTO() {
    	
    }

	public SyncronizeContestResponseDTO(Long lastConcCadastrado, int totContestSyncronized, LocalDate dateNextContest, String textReturnedSyoncronized, Long nextContest) {
		super();
		this.lastConcCadastrado = lastConcCadastrado;
		this.totContestSyncronized = totContestSyncronized;
		this.dateNextContest = dateNextContest;
		this.textReturnedSyoncronized = textReturnedSyoncronized;
		this.nextContest = nextContest;
	}

	public Long getLastConcCadastrado() {
		return lastConcCadastrado;
	}

	public void setLastConcCadastrado(Long lastConcCadastrado) {
		this.lastConcCadastrado = lastConcCadastrado;
	}

	public int getTotContestSyncronized() {
		return totContestSyncronized;
	}

	public void setTotContestSyncronized(int totContestSyncronized) {
		this.totContestSyncronized = totContestSyncronized;
	}

	public LocalDate getDateNextContest() {
		return dateNextContest;
	}

	public void setDateNextContest(LocalDate dateNextContest) {
		this.dateNextContest = dateNextContest;
	}

	public String getTextReturnedSyoncronized() {
		return textReturnedSyoncronized;
	}

	public void setTextReturnedSyoncronized(String textReturnedSyoncronized) {
		this.textReturnedSyoncronized = textReturnedSyoncronized;
	}

	public Long getNextContest() {
		return nextContest;
	}

	public void setNextContest(Long nextContest) {
		this.nextContest = nextContest;
	}

    
}
