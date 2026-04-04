package gabriel.hb.MyLifeBackend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import gabriel.hb.MyLifeBackend.entities.LotofacilBet;
import gabriel.hb.MyLifeBackend.entities.LotofacilBetNumber;
import gabriel.hb.MyLifeBackend.entities.LotofacilDraw;
import gabriel.hb.MyLifeBackend.entities.LotofacilDrawNumber;
import gabriel.hb.MyLifeBackend.repositories.LotofacilBetRepository;
import gabriel.hb.MyLifeBackend.repositories.LotofacilDrawRepository;
import gabriel.hb.MyLifeBackend.resources.dto.PlaceBetRequest;
import gabriel.hb.MyLifeBackend.services.dto.CaixaDraw;
import gabriel.hb.MyLifeBackend.services.exceptions.DatabaseException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;

@Service /* Registra a classe como um componente/service do spring e vai poder ser injetado no LotofacilBetResource */
public class LotofacilBetService {
	
	/* O Spring resolve essa injeção de dependencia e associar uma instancia de LotofacilBetRepository */
	@Autowired private LotofacilBetRepository repository;
	@Autowired private LotofacilDrawRepository drawRepository;
	
	private final String CAIXA_API_URL = "https://servicebus2.caixa.gov.br/portaldeloterias/api/lotofacil/";
	
	/* Listar de todos os concursos */
	public List<LotofacilBet> findAll(){
		return repository.findAll();
	}
	
	/* Consultar concurso por Id */
	public LotofacilBet findById(Long id) {
		Optional<LotofacilBet> obj = repository.findById(id); // o findById retona um Optional
        return obj.orElseThrow(() -> new ResourceNotFoundException(id)); // Poderia ser um return obj.get(); para pegar o 'ConcursoLotofacil' do obj;
	}
	
	/* Consultar concurso por Id */
	public List<LotofacilBet> findByTargetDrawId(Long targetDrawId) {
		List<LotofacilBet> list = repository.findByTargetDrawId(targetDrawId);
	    
	    // Se a lista estiver vazia, lança a exceção passando o ID
	    if (list.isEmpty()) {
	        throw new ResourceNotFoundException(targetDrawId);
	    }
	    
	    return list;
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
        int calculatedRepeatedCount = 0;
        int calculatedOddCount = 0;
        int calculatedEvenCount = 0;

        // 4. Processar os números da aposta...
        for (Integer num : dto.getBetNumbers()) {
            LotofacilBetNumber betNumber = new LotofacilBetNumber();
            betNumber.setNumber(num);
            betNumber.setBet(bet); 
            
            // Calcula Paridade
            if (num % 2 == 0) {
                calculatedEvenCount++;
            } else {
                calculatedOddCount++;
            }
            
            // Verifica se o número repetiu do concurso anterior
            boolean isRepeated = previousNumbers.contains(num);
            betNumber.setIsRepeated(isRepeated);
            if (isRepeated) {
                calculatedRepeatedCount++;
            }

            // Verifica se acertou o número
            if (bet.isChecked() && targetNumbers.contains(num)) {
                betNumber.setWasCorrectly(true);
                hitCount++;
            } else {
                betNumber.setWasCorrectly(false);
            }

            bet.getBetNumbers().add(betNumber);
        }

        // 5. ATRIBUI OS VALORES REAIS CALCULADOS À ENTIDADE PRINCIPAL
        bet.setOddCount(calculatedOddCount);
        bet.setEvenCount(calculatedEvenCount);
        bet.setRepeatedCount(calculatedRepeatedCount);

        // 5. Salva a quantidade de acertos finais
        // Se o concurso já existir e foi conferido
        if (bet.isChecked()) {
            bet.setHits(hitCount);
            
            // NOVO: Se teve 11 acertos ou mais, busca o prêmio na API da Caixa
            if (hitCount >= 11) {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    CaixaDraw caixaDrawData = restTemplate.getForObject(CAIXA_API_URL + dto.getTargetDrawId(), CaixaDraw.class);
                    
                    if (caixaDrawData != null && caixaDrawData.getListaRateioPremio() != null) {
                        String targetDescription = hitCount + " acertos";
                        for (CaixaDraw.RateioPremio rateio : caixaDrawData.getListaRateioPremio()) {
                            if (rateio.getDescricaoFaixa().toLowerCase().contains(targetDescription)) {
                                bet.setPrize(rateio.getValorPremio());
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    // Se a API falhar, logamos o erro e mantemos o prêmio zerado para não travar a inserção
                    System.out.println("Erro ao buscar prêmio na API da Caixa para o concurso " + dto.getTargetDrawId() + ": " + e.getMessage());
                }
            }
        }

        // 6. Salvar no banco. O CascadeType.ALL do LotofacilBet salvará a lista de LotofacilBetNumber automaticamente
		return repository.save(bet);
	}
	
	@Transactional
	public void checkPendingBetsForDraw(LotofacilDraw savedDraw, CaixaDraw caixaDrawData) {
	    // 1. Busca todas as apostas pendentes para este concurso
	    List<LotofacilBet> pendingBets = repository.findByTargetDrawIdAndIsCheckedFalse(savedDraw.getId());
	    
	    if (pendingBets.isEmpty()) return; // Se não tem aposta, não faz nada
	    
	    // 2. Extrai os números oficiais sorteados
	    List<Integer> officialNumbers = savedDraw.getDrawNumbers().stream()
	            .map(LotofacilDrawNumber::getNumber)
	            .collect(Collectors.toList());

	    // 3. Verifica cada aposta
	    for (LotofacilBet bet : pendingBets) {
	        int hits = 0;
	        
	        for (LotofacilBetNumber betNumber : bet.getBetNumbers()) {
	            if (officialNumbers.contains(betNumber.getNumber())) {
	                betNumber.setWasCorrectly(true);
	                hits++;
	            } else {
	                betNumber.setWasCorrectly(false);
	            }
	        }
	        
	        bet.setHits(hits);
	        bet.setChecked(true);
	        bet.setRealDraw(savedDraw);
	        
	        // 4. Define o prêmio baseando-se no payload da Caixa
	        double prizeValue = 0.0;
	        if (hits >= 11 && caixaDrawData.getListaRateioPremio() != null) {
	            String targetDescription = hits + " acertos";
	            for (CaixaDraw.RateioPremio rateio : caixaDrawData.getListaRateioPremio()) {
	                if (rateio.getDescricaoFaixa().toLowerCase().contains(targetDescription)) {
	                    prizeValue = rateio.getValorPremio();
	                    break;
	                }
	            }
	        }
	        bet.setPrize(prizeValue);
	    }
	    
	    // 5. Salva as apostas atualizadas no banco
	    repository.saveAll(pendingBets);
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
