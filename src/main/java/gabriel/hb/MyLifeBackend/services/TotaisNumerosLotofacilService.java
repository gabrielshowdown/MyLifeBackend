package gabriel.hb.MyLifeBackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import gabriel.hb.MyLifeBackend.entities.TotaisNumerosLotofacil;
import gabriel.hb.MyLifeBackend.repositories.TotaisNumerosLotofacilRepository;
import gabriel.hb.MyLifeBackend.services.exceptions.DatabaseException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;

@Service // Registra a classe como um componente/service do spring e vai poder ser injetado no TotaisNumerosLotofacilResource
public class TotaisNumerosLotofacilService {
	
	@Autowired //O Spring resolve essa injeção de dependencia e associar uma instancia de TotaisNumerosLotofacilRepository
	private TotaisNumerosLotofacilRepository repository;
	
	public List<TotaisNumerosLotofacil> findAll(){
		return repository.findAll();
	}
	
	public TotaisNumerosLotofacil findById(Long id) {
		Optional<TotaisNumerosLotofacil> obj = repository.findById(id); // o findById retona um Optional
        return obj.orElseThrow(() -> new ResourceNotFoundException(id)); // Poderia ser um return obj.get(); para pegar o 'TotaisNumerosLotofacil' do obj;
	}
	
	public TotaisNumerosLotofacil insert(TotaisNumerosLotofacil obj) {
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
	
}
