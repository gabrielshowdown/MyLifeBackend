package gabriel.hb.MyLifeBackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import gabriel.hb.MyLifeBackend.entities.LotofacilBet;
import gabriel.hb.MyLifeBackend.repositories.projection.BetSummaryProjection;

@Repository //Registra a classe como um componente/repository do spring e vai poder ser injetado no LotofacilBetService 
			//(não é obrigatório, pois essa interface já herda isso do JpaRepository)
public interface LotofacilBetRepository extends JpaRepository<LotofacilBet, Long>{
	
	// Novo método: Busca o concurso com o ID mais alto (o último)
    Optional<LotofacilBet> findTopByOrderByIdDesc();
    
    List<LotofacilBet> findByTargetDrawId(Long targetDrawId);
    
    List<LotofacilBet> findByTargetDrawIdAndIsCheckedFalse(Long targetDrawId);
    
    @Query("SELECT COUNT(b) as totalBets, COALESCE(SUM(b.cost), 0.0) as totalInvested, COALESCE(SUM(b.prize), 0.0) as totalReturn FROM LotofacilBet b")
    BetSummaryProjection getBetSummaryData();
	
}
