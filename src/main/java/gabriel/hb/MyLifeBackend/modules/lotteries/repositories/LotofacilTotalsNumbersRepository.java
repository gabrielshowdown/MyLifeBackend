package gabriel.hb.MyLifeBackend.modules.lotteries.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gabriel.hb.MyLifeBackend.modules.lotteries.entitites.LotofacilTotalsNumbers;

@Repository /* Não é obrigatório, pois essa interface já herda isso do JpaRepository */
public interface LotofacilTotalsNumbersRepository extends JpaRepository<LotofacilTotalsNumbers, Long>{
	
}
