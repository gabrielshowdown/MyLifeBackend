package gabriel.hb.MyLifeBackend.modules.community.repositories;

import java.util.List;

import gabriel.hb.MyLifeBackend.modules.community.entities.ThemeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeHistoryRepository extends JpaRepository<ThemeHistory, Long> {
	
	List<ThemeHistory> findByThemeName(String themeName);
	
}