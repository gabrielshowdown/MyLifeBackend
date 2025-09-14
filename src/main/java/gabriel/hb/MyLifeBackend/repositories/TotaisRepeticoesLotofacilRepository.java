package gabriel.hb.MyLifeBackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gabriel.hb.MyLifeBackend.entities.TotaisRepeticoesLotofacil;

@Repository //Registra a classe como um componente/repository do spring e vai poder ser injetado no TotaisRepeticoesLotofacilService 
			//(não é obrigatório, pois essa interface já herda isso do JpaRepository)
public interface TotaisRepeticoesLotofacilRepository extends JpaRepository<TotaisRepeticoesLotofacil, Long>{
	
}
