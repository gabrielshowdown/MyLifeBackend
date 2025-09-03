package gabriel.hb.MyLifeBackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gabriel.hb.MyLifeBackend.entities.NumeroConcursoLotofacil;

@Repository //Registra a classe como um componente/repository do spring e vai poder ser injetado no NumeroConcursoLotofacilService 
			//(não é obrigatório, pois essa interface já herda isso do JpaRepository)
public interface NumeroConcursoLotofacilRepository extends JpaRepository<NumeroConcursoLotofacil, Long>{
	
	// Mesmo no banco sendo concurso_id , o Spring Data JPA Hibernate ja realiza essa conversão
	List<NumeroConcursoLotofacil> findByConcursoId(Long concursoId);
}
