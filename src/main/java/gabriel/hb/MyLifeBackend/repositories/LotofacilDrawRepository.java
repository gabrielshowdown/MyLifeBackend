package gabriel.hb.MyLifeBackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gabriel.hb.MyLifeBackend.entities.LotofacilDraw;

@Repository //Registra a classe como um componente/repository do spring e vai poder ser injetado no LotofacilDrawService 
			//(não é obrigatório, pois essa interface já herda isso do JpaRepository)
public interface LotofacilDrawRepository extends JpaRepository<LotofacilDraw, Long>{
	
	// Novo método: Busca o concurso com o ID mais alto (o último)
    Optional<LotofacilDraw> findTopByOrderByIdDesc();
	
}
