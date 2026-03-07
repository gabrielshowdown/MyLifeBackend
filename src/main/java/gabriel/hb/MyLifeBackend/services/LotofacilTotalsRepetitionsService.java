package gabriel.hb.MyLifeBackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gabriel.hb.MyLifeBackend.entities.LotofacilTotalsRepetitions;
import gabriel.hb.MyLifeBackend.repositories.LotofacilTotalsRepetitionsRepository;
import gabriel.hb.MyLifeBackend.services.exceptions.DatabaseException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;

@Service // Registra a classe como um componente/service do spring e vai poder ser injetado no TotaisRepeticoesLotofacilResource
public class LotofacilTotalsRepetitionsService {
	
	@Autowired //O Spring resolve essa injeção de dependencia e associar uma instancia de TotaisRepeticoesLotofacilRepository
	private LotofacilTotalsRepetitionsRepository repository;
	
	public List<LotofacilTotalsRepetitions> findAll(){
		return repository.findAll();
	}
	
	public LotofacilTotalsRepetitions findById(Long id) {
		Optional<LotofacilTotalsRepetitions> obj = repository.findById(id); // o findById retona um Optional
        return obj.orElseThrow(() -> new ResourceNotFoundException(id)); // Poderia ser um return obj.get(); para pegar o 'TotaisRepeticoesLotofacil' do obj;
	}
	
	public LotofacilTotalsRepetitions insert(LotofacilTotalsRepetitions obj) {
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

	@Transactional
    public void updateTotals(int repeated, Long drawId) {
		
        // 1. Buscar o registro correspondente ao número de repetidos
		LotofacilTotalsRepetitions total = repository.findByRepeated(repeated).orElseThrow(() -> new RuntimeException("Registro de repetição " + repeated + " não encontrado."));

        // 2. Incrementar a quantidade
        total.setQtd(total.getQuantity() + 1);
        repository.save(total);

        // 3. Recalcular porcentagem para todos os registros
        // recalcularPorcentagens();
    }
	
	public void recalculatePercentages() {
        List<LotofacilTotalsRepetitions> totalsRepetitionsList = repository.findAll();

        // Soma total de todas as quantidades
        int totalSum = totalsRepetitionsList.stream().mapToInt(LotofacilTotalsRepetitions::getQuantity).sum();

        if (totalSum == 0) return;

        for (LotofacilTotalsRepetitions total : totalsRepetitionsList) {
            double percentage = (total.getQuantity() * 100.0) / totalSum;
            total.setPercentage(percentage);
        }

        repository.saveAll(totalsRepetitionsList);
    }
	
}
