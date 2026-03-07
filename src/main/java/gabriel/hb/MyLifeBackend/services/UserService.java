package gabriel.hb.MyLifeBackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import gabriel.hb.MyLifeBackend.entities.User;
import gabriel.hb.MyLifeBackend.repositories.UserRepository;
import gabriel.hb.MyLifeBackend.services.exceptions.DatabaseException;
import gabriel.hb.MyLifeBackend.services.exceptions.InvalidLoginException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;
import gabriel.hb.MyLifeBackend.services.exceptions.UserAlreadyRegisteredException;
import jakarta.persistence.EntityNotFoundException;

@Service // Registra a classe como um componente/service do spring e vai poder ser injetado no UserResource
public class UserService {
	
	@Autowired //O Spring resolve essa injeção de dependencia e associar uma instancia de UserRepository
	private UserRepository repository;
	
	public List<User> findAll(){
		return repository.findAll();
	}
	
	public User findById(Long id) {
		Optional<User> obj = repository.findById(id); // o findById retona um Optional
        return obj.orElseThrow(() -> new ResourceNotFoundException(id)); // Poderia ser um return obj.get(); para pegar o 'User' do obj;
	}
	
	public List<User> findByUsername(String name) {
        return repository.findByUsername(name);
    }
	
	public User validateUser(String username, String password) {
		Optional<User> obj = repository.findByUsernameAndPassword(username, password);
		return obj.orElseThrow(() -> new InvalidLoginException(""));
		// Caso quisesse o booleano, poderia colocar repository.findByUsernameAndSenha(username, senha).isPresent();
    }
	
	public User insert(User obj) {
		if(findByUsername(obj.getUsername()).isEmpty()) {
			return repository.save(obj);
		}
		else {
			throw new UserAlreadyRegisteredException(" com o username: " + obj.getUsername());
		}
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
	
	public User update(long id, User obj) {
		try {
			User entity = repository.getReferenceById(id); // Deixa um obj monitorado pelo JPA, não realizando operação com o banco igual o findById
			updateData(entity, obj);
			return repository.save(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(User entity, User obj) {
		entity.setUsername(obj.getUsername());
		entity.setPassword(obj.getPassword());
		entity.setGender(obj.getGender());
		entity.setLocation(obj.getLocation());
	}
}
