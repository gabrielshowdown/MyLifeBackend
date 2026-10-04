package gabriel.hb.MyLifeBackend.modules.community.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import gabriel.hb.MyLifeBackend.modules.community.entities.ThemeHistory;
import gabriel.hb.MyLifeBackend.modules.community.repositories.projection.ThemeSummaryProjection;

@Repository /* Não é obrigatório, pois essa interface já herda isso do JpaRepository */
public interface ThemeHistoryRepository extends JpaRepository<ThemeHistory, Long> {
	
	List<ThemeHistory> findByThemeName(String themeName);
	
	@Query("""
		    SELECT t.id as id, t.themeName as themeName, t.celebrationDate as celebrationDate
		    FROM ThemeHistory t
		    ORDER BY t.celebrationDate DESC, t.id DESC
		    """)
	/* Poderia ser: @Query("SELECT t.id as id, t.themeName as themeName, t.celebrationDate as celebrationDate FROM ThemeHistory t ORDER BY t.celebrationDate DESC, t.id DESC") */
	/* Os tres aspas são um textblock, a única diferença é a legibilidade quebrando as linhas */
	List<ThemeSummaryProjection> findAllSummaries();
	
}