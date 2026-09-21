package gabriel.hb.MyLifeBackend.modules.lotteries.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gabriel.hb.MyLifeBackend.modules.lotteries.entitites.LotofacilTotalsRepetitions;

@Repository /* Não é obrigatório, pois essa interface já herda isso do JpaRepository */
public interface LotofacilTotalsRepetitionsRepository extends JpaRepository<LotofacilTotalsRepetitions, Long>{
	
	Optional<LotofacilTotalsRepetitions> findByRepeated(Integer repeated);

}
