package gabriel.hb.MyLifeBackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gabriel.hb.MyLifeBackend.entities.LotofacilDrawNumber;

@Repository //Registra a classe como um componente/repository do spring e vai poder ser injetado no NumeroConcursoLotofacilService 
			//(não é obrigatório, pois essa interface já herda isso do JpaRepository)
public interface LotofacilDrawNumberRepository extends JpaRepository<LotofacilDrawNumber, Long>{
	
	// Mesmo no banco sendo concurso_id , o Spring Data JPA Hibernate ja realiza essa conversão
	List<LotofacilDrawNumber> findByDrawId(Long drawId);
}
