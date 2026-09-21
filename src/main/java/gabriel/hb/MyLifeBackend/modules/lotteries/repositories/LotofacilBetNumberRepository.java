package gabriel.hb.MyLifeBackend.modules.lotteries.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gabriel.hb.MyLifeBackend.modules.lotteries.entitites.LotofacilBetNumber;

@Repository /* Não é obrigatório, pois essa interface já herda isso do JpaRepository */
public interface LotofacilBetNumberRepository extends JpaRepository<LotofacilBetNumber, Long>{
	
	/* Mesmo no banco sendo concurso_id , o Spring Data JPA Hibernate ja realiza essa conversão */
	List<LotofacilBetNumber> findByBetId(Long betId);
}
