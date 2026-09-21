package gabriel.hb.MyLifeBackend.modules.lotteries.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import gabriel.hb.MyLifeBackend.modules.lotteries.entitites.LotofacilDrawNumber;
import gabriel.hb.MyLifeBackend.modules.lotteries.repositories.LotofacilDrawNumberRepository;
import gabriel.hb.MyLifeBackend.shared.DatabaseException;
import gabriel.hb.MyLifeBackend.shared.ResourceNotFoundException;

@Service
public class LotofacilDrawNumberService {
	
	@Autowired
	private LotofacilDrawNumberRepository repository;
	
	public List<LotofacilDrawNumber> findAll(){
		return repository.findAll();
	}
	
	public LotofacilDrawNumber findById(Long id) {
		Optional<LotofacilDrawNumber> obj = repository.findById(id); /* O findById retona um Optional */
        return obj.orElseThrow(() -> new ResourceNotFoundException(id)); /* Poderia ser um return obj.get(); para pegar o 'LotofacilDrawNumber' do obj; */
	}
	
	public LotofacilDrawNumber insert(LotofacilDrawNumber obj) {
		return repository.save(obj);
	}
	
	public void delete(Long id) {
	    try {
	        if (repository.existsById(id)) {
	            repository.deleteById(id);			
	        } else {				
	            throw new ResourceNotFoundException(id); /* Lança uma exceção através do 'ResourceExceptionHandler', que captura as excecões que ocorrem */
	        }		
	    } catch (DataIntegrityViolationException e) {			
	        throw new DatabaseException(e.getMessage());		
	    }	
	}
	
	/*
	public List<LotofacilDrawNumber> findByConcursoId(Long drawId) {
		return repository.findByConcursoId(drawId);
	}
	*/
}
