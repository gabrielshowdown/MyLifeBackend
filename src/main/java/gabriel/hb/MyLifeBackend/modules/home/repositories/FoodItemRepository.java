package gabriel.hb.MyLifeBackend.modules.home.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gabriel.hb.MyLifeBackend.modules.home.entities.FoodItem;

@Repository /* Não é obrigatório, pois essa interface já herda isso do JpaRepository */
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

}
