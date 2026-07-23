package gabriel.hb.MyLifeBackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import gabriel.hb.MyLifeBackend.entities.ThemeHistory;

@Repository
public interface ThemeHistoryRepository extends JpaRepository<ThemeHistory, Long> {
}