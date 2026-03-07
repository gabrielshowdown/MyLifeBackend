package gabriel.hb.MyLifeBackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import gabriel.hb.MyLifeBackend.entities.Item;
import gabriel.hb.MyLifeBackend.repositories.ItemRepository;
import gabriel.hb.MyLifeBackend.services.exceptions.DatabaseException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@Service // Registra a classe como um componente/service do spring e vai poder ser injetado no UserResource
public class ItemService {
	
	@Autowired //O Spring resolve essa injeção de dependencia e associar uma instancia de UserRepository
	private ItemRepository repository;
	
	public List<Item> findAll(){
		return repository.findAll();
	}
	
	public Item findById(Long id) {
		Optional<Item> obj = repository.findById(id); // o findById retona um Optional
        return obj.orElseThrow(() -> new ResourceNotFoundException(id)); // Poderia ser um return obj.get(); para pegar o 'User' do obj;
	}
	
	public Item insert(Item obj) {
		return repository.save(obj);
	}
	
	public void delete(Long id) {
	    try {
	        if (repository.existsById(id)) {
	            repository.deleteById(id);			
	        } else {				
	            throw new ResourceNotFoundException(id); // Lança uma exceção através do 'ResourceExceptionHandler', que captura as excecões que ocorrem		
	        }		
	    } catch (DataIntegrityViolationException e) {			
	        throw new DatabaseException(e.getMessage());		
	    }	
	} 
	
	
	public Item update(long id, Item obj) {
		try {
			Item entity = repository.getReferenceById(id); // Deixa um obj monitorado pelo JPA, não realizando operação com o banco igual o findById
			updateData(entity, obj);
			return repository.save(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Item entity, Item obj) {
		entity.setName(obj.getName());
		entity.setCategory(obj.getCategory());
		entity.setObservation(obj.getObservation());
		entity.setImage(obj.getImage());
		entity.setValidity(obj.getValidity());
	}
}
