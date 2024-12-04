package gabriel.hb.MyLifeBackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gabriel.hb.MyLifeBackend.entities.User;
import gabriel.hb.MyLifeBackend.repositories.UserRepository;

@Service // Registra a classe como um componente/service do spring e vai poder ser injetado no UserResource
public class UserService {
	
	@Autowired //O Spring resolve essa injeção de dependencia e associar uma instancia de UserRepository
	private UserRepository repository;
	
	public List<User> findAll(){
		return repository.findAll();
	}
	
	public User findById(Long id) {
		Optional<User> obj = repository.findById(id); // o findById retona um Optional
		return obj.get(); // Pega o 'User' do obj;
	}
	
	public User insert(User obj) {
		return repository.save(obj);
	}
	
    public void delete (long id) {
        repository.deleteById(id);
    }
	
    public User update(long id, User obj) {
        User entity = repository.getReferenceById(id); // Deixa um obj monitorado pelo JPA, não realizando operação com o banco igual o findById
        updateData(entity, obj);
        return repository.save(entity);
    }

	private void updateData(User entity, User obj) {
		entity.setUsername(obj.getUsername());
		entity.setSenha(obj.getSenha());
		entity.setGenero(obj.getGenero());
		entity.setLocalizacao(obj.getLocalizacao());
	}
}
