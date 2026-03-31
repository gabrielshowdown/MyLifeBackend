package gabriel.hb.MyLifeBackend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gabriel.hb.MyLifeBackend.entities.LotofacilBet;
import gabriel.hb.MyLifeBackend.entities.LotofacilBetNumber;
import gabriel.hb.MyLifeBackend.entities.LotofacilDraw;
import gabriel.hb.MyLifeBackend.entities.LotofacilDrawNumber;
import gabriel.hb.MyLifeBackend.repositories.LotofacilBetRepository;
import gabriel.hb.MyLifeBackend.repositories.LotofacilDrawRepository;
import gabriel.hb.MyLifeBackend.resources.dto.PlaceBetRequest;
import gabriel.hb.MyLifeBackend.services.exceptions.DatabaseException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;

@Service /* Registra a classe como um componente/service do spring e vai poder ser injetado no LotofacilBetResource */
public class LotofacilBetService {
	
	/* O Spring resolve essa injeção de dependencia e associar uma instancia de LotofacilBetRepository */
	@Autowired private LotofacilBetRepository repository;
	@Autowired private LotofacilDrawRepository drawRepository;
	
	/* Listar de todos os concursos */
	public List<LotofacilBet> findAll(){
		return repository.findAll();
	}
	
	/* Consultar concurso por Id */
	public LotofacilBet findById(Long id) {
		Optional<LotofacilBet> obj = repository.findById(id); // o findById retona um Optional
        return obj.orElseThrow(() -> new ResourceNotFoundException(id)); // Poderia ser um return obj.get(); para pegar o 'ConcursoLotofacil' do obj;
	}
	
	/* Inserir novo concurso */
	@Transactional
	public LotofacilBet insert(PlaceBetRequest dto) {
        // 1. Instanciar a entidade principal
		LotofacilBet bet = new LotofacilBet();
        bet.setBetDate(dto.getBetDate());
        bet.setTargetDrawId(dto.getTargetDrawId());
        bet.setOddCount(dto.getOddCount());
        bet.setEvenCount(dto.getEvenCount());
        bet.setRepeatedCount(dto.getRepeatedCount());
        bet.setCost(3.50); // Valor fixo de uma aposta simples
        bet.setChecked(false);
        bet.setHits(0);

        // 2. Buscar o concurso ANTERIOR para saber quais números são repetidos
        Optional<LotofacilDraw> previousDrawOpt = drawRepository.findById(dto.getTargetDrawId() - 1);
        List<Integer> previousNumbers = new ArrayList<>();
        if (previousDrawOpt.isPresent()) {
            previousNumbers = previousDrawOpt.get().getDrawNumbers().stream()
                    .map(LotofacilDrawNumber::getNumber)
                    .collect(Collectors.toList());
        }

        // 3. Buscar o concurso ALVO para saber se já podemos conferir a aposta
        Optional<LotofacilDraw> targetDrawOpt = drawRepository.findById(dto.getTargetDrawId());
        List<Integer> targetNumbers = new ArrayList<>();
        if (targetDrawOpt.isPresent()) {
            bet.setRealDraw(targetDrawOpt.get()); // Vincula o concurso real à aposta
            bet.setChecked(true); // Marca como conferido
            targetNumbers = targetDrawOpt.get().getDrawNumbers().stream()
                    .map(LotofacilDrawNumber::getNumber)
                    .collect(Collectors.toList());
        }

        int hitCount = 0;

        // 4. Transformar a lista de inteiros do DTO em entidades LotofacilBetNumber
        for (Integer num : dto.getBetNumbers()) {
            LotofacilBetNumber betNumber = new LotofacilBetNumber();
            betNumber.setNumber(num);
            betNumber.setBet(bet); // Vincula o número à aposta (necessário para o CascadeType.ALL funcionar)
            
            // Verifica se o número repetiu do concurso anterior
            betNumber.setIsRepeated(previousNumbers.contains(num));

            // Verifica se acertou o número (caso o concurso oficial já exista)
            if (bet.isChecked() && targetNumbers.contains(num)) {
                betNumber.setWasCorrectly(true);
                hitCount++;
            } else {
                betNumber.setWasCorrectly(false);
            }

            // Adiciona o número à lista da aposta
            bet.getBetNumbers().add(betNumber);
        }

        // 5. Salva a quantidade de acertos finais
        if (bet.isChecked()) {
            bet.setHits(hitCount);
        }

        // 6. Salvar no banco. O CascadeType.ALL do LotofacilBet salvará a lista de LotofacilBetNumber automaticamente
		return repository.save(bet);
	}
	
	/* Apagar concurso */
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
