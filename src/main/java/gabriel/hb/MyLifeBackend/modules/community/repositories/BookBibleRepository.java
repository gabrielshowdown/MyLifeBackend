package gabriel.hb.MyLifeBackend.modules.community.repositories;

import java.util.List;

import gabriel.hb.MyLifeBackend.modules.community.entities.enums.ReadingCategory;
import gabriel.hb.MyLifeBackend.modules.community.entities.BookBible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository /* Não é obrigatório, pois essa interface já herda isso do JpaRepository */
public interface BookBibleRepository extends JpaRepository<BookBible, Long>{
	
    List<BookBible> findByCategory(ReadingCategory category);
	
    BookBible findByAbbreviationIgnoreCase(String abbreviation);
    
    List<BookBible> findByName(String name);

}
