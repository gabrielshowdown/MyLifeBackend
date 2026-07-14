package gabriel.hb.MyLifeBackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gabriel.hb.MyLifeBackend.entities.BookBible;
import gabriel.hb.MyLifeBackend.entities.enums.ReadingCategory;

@Repository //Registra a classe como um componente/repository do spring e vai poder ser injetado no BookBibleService 
			//(não é obrigatório, pois essa interface já herda isso do JpaRepository)
public interface BookBibleRepository extends JpaRepository<BookBible, Long>{
	
    List<BookBible> findByCategory(ReadingCategory category);
	
    BookBible findByAbbreviationIgnoreCase(String abbreviation);
    
    List<BookBible> findByName(String name);

}
