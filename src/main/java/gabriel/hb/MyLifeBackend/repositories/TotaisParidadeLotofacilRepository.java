package gabriel.hb.MyLifeBackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gabriel.hb.MyLifeBackend.entities.TotaisParidadeLotofacil;

@Repository //Registra a classe como um componente/repository do spring e vai poder ser injetado no TotaisParidadeLotofacilService 
			//(não é obrigatório, pois essa interface já herda isso do JpaRepository)
public interface TotaisParidadeLotofacilRepository extends JpaRepository<TotaisParidadeLotofacil, Long>{
	Optional<TotaisParidadeLotofacil> findByParidade(String paridade);
}
