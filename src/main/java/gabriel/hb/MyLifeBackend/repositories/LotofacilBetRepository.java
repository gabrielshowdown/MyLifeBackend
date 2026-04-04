package gabriel.hb.MyLifeBackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gabriel.hb.MyLifeBackend.entities.LotofacilBet;

@Repository //Registra a classe como um componente/repository do spring e vai poder ser injetado no LotofacilBetService 
			//(não é obrigatório, pois essa interface já herda isso do JpaRepository)
public interface LotofacilBetRepository extends JpaRepository<LotofacilBet, Long>{
	
	// Novo método: Busca o concurso com o ID mais alto (o último)
    Optional<LotofacilBet> findTopByOrderByIdDesc();
    
    List<LotofacilBet> findByTargetDrawId(Long targetDrawId);
    
    List<LotofacilBet> findByTargetDrawIdAndIsCheckedFalse(Long targetDrawId);
	
}
