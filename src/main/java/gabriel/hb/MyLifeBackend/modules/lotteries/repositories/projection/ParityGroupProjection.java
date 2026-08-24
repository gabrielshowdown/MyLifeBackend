package gabriel.hb.MyLifeBackend.modules.lotteries.repositories.projection;

public interface ParityGroupProjection {
	
    Integer getOddCount();
    Integer getEvenCount();
    Long getTotal();
    
}