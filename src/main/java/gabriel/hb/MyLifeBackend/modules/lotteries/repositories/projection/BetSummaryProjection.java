package gabriel.hb.MyLifeBackend.modules.lotteries.repositories.projection;

public interface BetSummaryProjection {
	
    Long getTotalBets();
    Double getTotalInvested();
    Double getTotalReturn();
    
}