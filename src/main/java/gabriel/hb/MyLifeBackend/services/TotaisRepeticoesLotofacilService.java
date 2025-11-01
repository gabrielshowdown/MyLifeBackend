package gabriel.hb.MyLifeBackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
    public void atualizaTotais(int repetidos, Long idConcurso) {
		
        // 1. Buscar o registro correspondente ao número de repetidos
        TotaisRepeticoesLotofacil total = repository.findByRepetido(repetidos).orElseThrow(() -> new RuntimeException("Registro de repetição " + repetidos + " não encontrado."));

        // 2. Incrementar a quantidade
        total.setQtd(total.getQtd() + 1);
        repository.save(total);

        // 3. Recalcular porcentagem para todos os registros
        recalcularPorcentagens();
    }
	
	private void recalcularPorcentagens() {
        List<TotaisRepeticoesLotofacil> totais = repository.findAll();

        // Soma total de todas as quantidades
        int somaTotal = totais.stream().mapToInt(TotaisRepeticoesLotofacil::getQtd).sum();

        if (somaTotal == 0) return;

        for (TotaisRepeticoesLotofacil total : totais) {
            double porcentagem = (total.getQtd() * 100.0) / somaTotal;
            total.setPorcentagem(porcentagem);
        }

        repository.saveAll(totais);
    }
	
}
