package gabriel.hb.MyLifeBackend.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gabriel.hb.MyLifeBackend.entities.ThemeHistory;
import gabriel.hb.MyLifeBackend.repositories.ThemeHistoryRepository;

@Service
public class ThemeHistoryService {

    @Autowired
    private ThemeHistoryRepository repository;

    public ThemeHistory insert(ThemeHistory obj) {
        // Define a data atual no momento em que está sendo salvo
        obj.setCreationDate(LocalDate.now());
        return repository.save(obj);
    }

    public List<ThemeHistory> findAll() {
        return repository.findAll();
    }
}