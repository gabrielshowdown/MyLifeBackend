package gabriel.hb.MyLifeBackend.modules.lotteries.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gabriel.hb.MyLifeBackend.modules.lotteries.entitites.LotofacilDraw;

@Repository /* Não é obrigatório, pois essa interface já herda isso do JpaRepository */
public interface LotofacilDrawRepository extends JpaRepository<LotofacilDraw, Long>{
	
	/* Busca o concurso com o ID mais alto (o último) */
    Optional<LotofacilDraw> findTopByOrderByIdDesc();
    
    /* Busca concursos inseridos manualmente que não foram confirmados com a API */
    List<LotofacilDraw> findByIsOfficialFalse();
	
}
