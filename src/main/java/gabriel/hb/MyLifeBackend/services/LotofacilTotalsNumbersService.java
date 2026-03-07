package gabriel.hb.MyLifeBackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import gabriel.hb.MyLifeBackend.entities.LotofacilTotalsNumbers;
import gabriel.hb.MyLifeBackend.repositories.LotofacilTotalsNumbersRepository;
import gabriel.hb.MyLifeBackend.services.exceptions.DatabaseException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;

@Service // Registra a classe como um componente/service do spring e vai poder ser injetado no TotaisNumerosLotofacilResource
public class LotofacilTotalsNumbersService {
	
	@Autowired //O Spring resolve essa injeção de dependencia e associar uma instancia de TotaisNumerosLotofacilRepository
	private LotofacilTotalsNumbersRepository repository;
	
	public List<LotofacilTotalsNumbers> findAll(){
		return repository.findAll();
	}
	
	public LotofacilTotalsNumbers findById(Long id) {
		Optional<LotofacilTotalsNumbers> obj = repository.findById(id); // o findById retona um Optional
        return obj.orElseThrow(() -> new ResourceNotFoundException(id)); // Poderia ser um return obj.get(); para pegar o 'TotaisNumerosLotofacil' do obj;
	}
	
	public LotofacilTotalsNumbers insert(LotofacilTotalsNumbers obj) {
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

	public void updateTotals(List<Integer> currentDozens, Long lastDrawId) {
		
		for(long n : currentDozens) {
			LotofacilTotalsNumbers total = repository.findById(n).orElseThrow(() -> new RuntimeException("Registro de número " + n + " não encontrado."));
			total.setQuantity(total.getQuantity() + 1);
	        repository.save(total);
		}
		
	}

	public void recalculatePercentages(Long lastDrawId) {
		
		List<LotofacilTotalsNumbers> totalsNumbersList = repository.findAll();

        if (lastDrawId == 0) return;

        for (LotofacilTotalsNumbers total : totalsNumbersList) {
            double porcentagem = (total.getQuantity() * 100.0) / lastDrawId;
            total.setPercentage(porcentagem);
        }

        repository.saveAll(totalsNumbersList);
		
	} 
	
}
