package gabriel.hb.MyLifeBackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import gabriel.hb.MyLifeBackend.entities.TotaisRepeticoesLotofacil;
import gabriel.hb.MyLifeBackend.repositories.TotaisRepeticoesLotofacilRepository;
import gabriel.hb.MyLifeBackend.services.exceptions.DatabaseException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;

@Service // Registra a classe como um componente/service do spring e vai poder ser injetado no TotaisRepeticoesLotofacilResource
public class TotaisRepeticoesLotofacilService {
	
	@Autowired //O Spring resolve essa injeção de dependencia e associar uma instancia de TotaisRepeticoesLotofacilRepository
	private TotaisRepeticoesLotofacilRepository repository;
	
	public List<TotaisRepeticoesLotofacil> findAll(){
		return repository.findAll();
	}
	
	public TotaisRepeticoesLotofacil findById(Long id) {
		Optional<TotaisRepeticoesLotofacil> obj = repository.findById(id); // o findById retona um Optional
        return obj.orElseThrow(() -> new ResourceNotFoundException(id)); // Poderia ser um return obj.get(); para pegar o 'TotaisRepeticoesLotofacil' do obj;
	}
	
	public TotaisRepeticoesLotofacil insert(TotaisRepeticoesLotofacil obj) {
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
