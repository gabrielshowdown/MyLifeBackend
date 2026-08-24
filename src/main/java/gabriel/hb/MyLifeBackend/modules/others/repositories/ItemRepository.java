package gabriel.hb.MyLifeBackend.modules.others.repositories;

import gabriel.hb.MyLifeBackend.modules.others.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Registra a classe como um componente/repository do spring e vai poder ser injetado no UserService 
			//(não é obrigatório, pois essa interface já herda isso do JpaRepository)
public interface ItemRepository extends JpaRepository<Item, Long>{
	
}
