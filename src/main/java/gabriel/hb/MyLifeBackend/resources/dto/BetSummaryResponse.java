package gabriel.hb.MyLifeBackend.resources.dto;

public class BetSummaryResponse {
	
    private Long totalBets;
    private Double totalInvested;
    private Double totalReturn;
    private Double balance;

    public BetSummaryResponse(Long totalBets, Double totalInvested, Double totalReturn, Double balance) {
        this.totalBets = totalBets;
        this.totalInvested = totalInvested;
        this.totalReturn = totalReturn;
        this.balance = balance;
    }

    // Getters e Setters
    public Long getTotalBets() {
    	return totalBets; 
    }
    public void setTotalBets(Long totalBets) { this.totalBets = totalBets; }
    public Double getTotalInvested() { return totalInvested; }
    public void setTotalInvested(Double totalInvested) { this.totalInvested = totalInvested; }
    public Double getTotalReturn() { return totalReturn; }
    public void setTotalReturn(Double totalReturn) { this.totalReturn = totalReturn; }
    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
}