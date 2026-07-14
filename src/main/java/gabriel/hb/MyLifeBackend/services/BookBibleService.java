package gabriel.hb.MyLifeBackend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gabriel.hb.MyLifeBackend.entities.BookBible;
import gabriel.hb.MyLifeBackend.entities.enums.ReadingCategory;
import gabriel.hb.MyLifeBackend.repositories.BookBibleRepository;
import gabriel.hb.MyLifeBackend.services.exceptions.BookBibleAlreadyRegisteredException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;

@Service
public class BookBibleService {

    @Autowired
    private BookBibleRepository repository;

    public List<BookBible> findAll() {
        return repository.findAll();
    }

    public List<BookBible> findByCategory(ReadingCategory category) {
        return repository.findByCategory(category);
    }

    // Método principal para o Frontend mudar um livro de lista
    public BookBible updateCategory(Long id, ReadingCategory newCategory) {
    	BookBible entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        
        entity.setCategory(newCategory);
        return repository.save(entity);
    }
    
    public BookBible insert(BookBible obj) {
		if(findByName(obj.getName()).isEmpty()) {
			return repository.save(obj);
		}
		else {
			throw new BookBibleAlreadyRegisteredException(" com o nome: " + obj.getName());
		}
	}
    
    public List<BookBible> findByName(String name) {
        return repository.findByName(name);
    }
}