package gabriel.hb.MyLifeBackend.repositories.projection;

public interface BetSummaryProjection {
	
    Long getTotalBets();
    Double getTotalInvested();
    Double getTotalReturn();
    
}