package gabriel.hb.MyLifeBackend.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import gabriel.hb.MyLifeBackend.resources.dto.AddDrawRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gabriel.hb.MyLifeBackend.entities.LotofacilDraw;
import gabriel.hb.MyLifeBackend.entities.LotofacilDrawNumber;
import gabriel.hb.MyLifeBackend.repositories.LotofacilDrawRepository;
import gabriel.hb.MyLifeBackend.resources.dto.SynchronizeDrawResponse;
import gabriel.hb.MyLifeBackend.services.dto.CaixaDraw;
import gabriel.hb.MyLifeBackend.services.exceptions.DatabaseException;
import gabriel.hb.MyLifeBackend.services.exceptions.InvalidLParametersDrawException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;

@Service /* Registra a classe como um componente/service do spring e vai poder ser injetado no ConcursoLotofacilResource */
public class LotofacilDrawService {
	
	/* O Spring resolve essa injeção de dependencia e associar uma instancia de ConcursoLotofacilRepository */
	@Autowired private LotofacilDrawRepository repository;
	@Autowired private LotofacilTotalsRepetitionsService lotofacilTotalsRepetitionsService;
	@Autowired private LotofacilTotalsParitiesService lotofacilTotalsParitiesService;
	@Autowired private LotofacilTotalsNumbersService lotofacilTotalsNumbersService;
	
	private final String CAIXA_API_URL = "https://servicebus2.caixa.gov.br/portaldeloterias/api/lotofacil/";
	DateTimeFormatter fmt1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	/* Listar de todos os concursos */
	public List<LotofacilDraw> findAll(){
		return repository.findAll();
	}
	
	/* Consultar concurso por Id */
	public LotofacilDraw findById(Long id) {
		Optional<LotofacilDraw> obj = repository.findById(id); // o findById retona um Optional
        return obj.orElseThrow(() -> new ResourceNotFoundException(id)); // Poderia ser um return obj.get(); para pegar o 'ConcursoLotofacil' do obj;
	}
	
	/* Inserir novo concurso */
	public LotofacilDraw insert(LotofacilDraw obj) {
		return repository.save(obj);
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
	
	/* Geração de concurso */
	public LotofacilDraw generateDraw(Long lastDrawId, int repeatedCount, int oddCount, int evenCount) {
		
		System.out.println("lastDrawId " + lastDrawId);
		System.out.println("repeatedCount " + repeatedCount);
		System.out.println("oddCount " + oddCount);
		System.out.println("evenCount " + evenCount);
		
		/* Variáveis que serão usadas no método */
		int lastDrawOddCount = 0; // Quantidade de ímpares do último concurso
    	int lastDrawEvenCount = 0; // Quantidade de pares do último concurso
    	int generatedRepeatedCount = 0; //Quantidade de repetidos do concurso que o algoritimo irá tentar gerar
    	int generatedOddCount = 0; // Quantidade de impares do concurso que o algoritimo irá tentar gerar
		int minOddCount; // Quantidade mínima de impares disponível para o concurso a ser gerado
		int minEvenCount; // Quantidade mínima de pares disponível para o concurso a ser gerado
    	boolean ignoreRepeats = false; // Se o que repeticao que veio é nula (pode ser qualquer uma)
    	boolean ignoreParity = false; // Se o que veio de paridade é nulo (pode ser qualquer uma)
		Set<Integer> generatedNumbers = new HashSet<>(); // Set do concurso que será gerado
		List<Integer>lastDrawNumbersList = new ArrayList<>(); // Lista dos números do último concurso
		
		/* Tratamento para ver os valores passados na chamada do método */
		if (oddCount + evenCount != 15 && oddCount + evenCount != 0) {
			throw new InvalidLParametersDrawException("Pares:" + evenCount + " Impares:" + oddCount);
        }
		
		if (repeatedCount > 12 || repeatedCount < 6 && repeatedCount != 0) {
			throw new InvalidLParametersDrawException("Repetição tem que ser entre 6 e 12 ");
        }
		
		/* Verifica se o jogo a ser gerado possui ignora as paridades e repeticões */
		ignoreRepeats = (repeatedCount == 0);
		ignoreParity = (oddCount == 0);
		// ignoreRepeats = (repeatedCount == 0) ?  true : false;
		// ignoreParity = (oddCount == 0) ? true : false;
    	System.out.println("ignoreRepeats " + ignoreRepeats);
    	System.out.println("ignoreParity " + ignoreParity);
    	
		/* Obtem as informações do concurso anterior que será usado como comparativo na geração */
    	LotofacilDraw lastDraw = findById(lastDrawId);
		List<LotofacilDrawNumber> lastDrawNumberEntities = lastDraw.getDrawNumbers();
		/* Obtem apenas a lista de números, ignorando o isRepeated e o id do concurso */
		lastDrawNumbersList = lastDrawNumberEntities.stream().map(n -> n.getNumber()).collect(Collectors.toList());
		lastDrawOddCount = lastDraw.getOddCount();
		lastDrawEvenCount = lastDraw.getEvenCount();
		
		System.out.println("lastDrawNumbersList " + lastDrawNumbersList);
		System.out.println("lastDrawOddCount " + lastDrawOddCount);
		System.out.println("lastDrawEvenCount " + lastDrawEvenCount);
		
		/* Verifica o mínimo de pares e impares que podem ser usados no concurso a ser gerado */
		/* (Por exemplo, o mínimo de impares é: Qtd de Repetidos a ser gerado menos o oposto (par) do último concurso */
		minOddCount = repeatedCount - lastDrawEvenCount;
		minEvenCount = repeatedCount - lastDrawOddCount;
		
		System.out.println("minimoPar: " + minEvenCount + " minimoImpar: " + minOddCount);
		
		/* Verifica se com os parametros passados, se é possível gerar o concurso ou não */
		/* Se possui (repetição e paridade) e se as quantidades forem menor que o mínimo estabelecido , cai na exceção */
		if ((ignoreParity == false && (oddCount < minOddCount || evenCount < minEvenCount)) && ignoreRepeats == false) {
			
			System.out.println("Impossível gerar jogo");
			if (oddCount < minOddCount) {
				throw new InvalidLParametersDrawException ("Não é possível gerar o jogo! Jogo mínino para o concurso é : " + minOddCount + "I/" + (15 - minOddCount) + "P");
			}
			else {
				throw new InvalidLParametersDrawException ("Não é possível gerar o jogo! Jogo mínino para o concurso é : " + (15 - minEvenCount) + "I/" + minEvenCount + "P");
			}
		}
		else {
			
			System.out.println("É possível gerar");
			boolean isValidGame = false;
			
			do {
				int number = new Random().nextInt(25) + 1;
				
				if (generatedNumbers.size() < 15) {
					generatedNumbers.add(number);
				}
				
				/* Indica que já preencheu tudo*/
				if (generatedNumbers.size() == 15) { 
					
					generatedOddCount = 0;
					
					/* Armezena quantos impares foram gerados no concurso */
					for (Integer n : generatedNumbers) {
						if (n %2 == 1) {
							generatedOddCount ++;
						}
					}
					
					/* Verifica se o número de impares gerado está de acordo com o solicitado */
					if (oddCount == generatedOddCount || ignoreParity == true) {
						isValidGame = true;
					}
					else { // Se a relação de paridade não estiver de acordo, limpa a lista de concursos
						generatedNumbers.clear();
						isValidGame = false;
						generatedOddCount = 0;
					}
					
					/* Se a relação de paridade bate com o solicitado, verifica a repetição */
					if (isValidGame == true) {
						
						generatedRepeatedCount = 0;
						
						/* Armazena quantos repetidos foram gerados em relação com o concurso anterior */
						for (int i = 0; i < 15; i++) {
							if (generatedNumbers.contains(lastDrawNumbersList.get(i))) {
								generatedRepeatedCount ++;								
							}
						}
						
						/* Verifica se o número de repetições gerado está de acordo com o solicitado */
						if (repeatedCount == generatedRepeatedCount || ignoreRepeats == true) {
							isValidGame = true;
							// System.out.println("Os números repetidos do jogo anterior " + repetidosConcGener + ", batem com o selecionado pelo usuário " + qtdRepetidos);
						}
						else { // Se não não estiver de acordo, limpa a lista de concursos
							isValidGame = false;
							generatedNumbers.clear();
							// System.out.println("Os números repetidos do jogo anterior " + repetidosConcGener + ", não batem com o selecionado pelo usuário " + qtdRepetidos);
							generatedRepeatedCount = 0;
						}
						
					}
					
				}
				
				
			} while(generatedNumbers.size() < 15 && isValidGame == false);
			
			/* Se caiu aqui, conseguiu gerar o concurso */
			System.out.println("generatedNumbers: " + generatedNumbers);
			System.out.println("generatedOddCount: " + generatedOddCount);
			System.out.println("generatedEvenCount: " + (15 - generatedOddCount));
			System.out.println("generatedRepeatedCount: " + generatedRepeatedCount);
			
			/* Lista dos números gerados */
			List<LotofacilDrawNumber> generatedDrawNumberList = new ArrayList<>();	 	
			
			LotofacilDraw generateDraw = new LotofacilDraw();
			
			generateDraw.setOddCount(generatedOddCount);
			generateDraw.setEvenCount(15 - generatedOddCount);
			generateDraw.setRepeatedCount(generatedRepeatedCount);
			generateDraw.setId(lastDrawId + 1);
			
			/* Armazena na lista de NumeroConcursoLotofacil o sorteio realizado*/
			for (Integer num : generatedNumbers) {
				generatedDrawNumberList.add(new LotofacilDrawNumber(num, (lastDrawNumbersList.contains(num) ? true : false), generateDraw));
		 	}
			
			generateDraw.setDrawNumbers(generatedDrawNumberList);
			
			return generateDraw;
			
		}

	}
	
	/* Sincronização de Concursos */
	public SynchronizeDrawResponse synchronizeWithCaixaApi() {
		
		/* RestTemplate é uma classe da biblioteca Spring usada para fazer chamadas HTTP de forma simples */
        RestTemplate restTemplate = new RestTemplate();
        CaixaDraw latestCaixaDraw = restTemplate.getForObject(CAIXA_API_URL, CaixaDraw.class); 
        
        /* Variáveis que serão usadas no método */
        long latestRemoteDrawId = latestCaixaDraw.getNumero(); // Id do último concurso no portal da caixa
        long nextDrawId = latestCaixaDraw.getNumeroConcursoProximo(); // Id no próximo concurso no portal da Caixa
        
        long lastSavedDrawId = 0; // Variável para controlar o último id de concurso salvo
        long maxDrawIdToSync = 0; // Variável para verificar o próximo concurso a ser sincronizado
        int addedDrawsCount = 0; // Quantidade de concursos salvos

        LocalDate nextDrawDate = LocalDate.parse(latestCaixaDraw.getDataProximoConcurso(), fmt1);
        String syncMessage = "";
        
        System.out.println("latestRemoteDrawId: " + latestRemoteDrawId);
        System.out.println("data: " + latestCaixaDraw.getDataProximoConcurso());
        
        /* Descobrir o último concurso no DB local */
        Optional<LotofacilDraw> latestLocalDrawOpt = repository.findTopByOrderByIdDesc();
        long latestLocalDrawId = latestLocalDrawOpt.isPresent() ? latestLocalDrawOpt.get().getId() : 0; 
        System.out.println("latestLocalDrawId: " + latestLocalDrawId);
             
        /* Com base no último local, coloca como teto + 100 concurso se tiver tudo isso 
         * Ex: se o último na caixa for 3000 e o último local for 2700, o teto é 2800 */
        maxDrawIdToSync = (latestRemoteDrawId - latestLocalDrawId >= 100) ? latestLocalDrawId + 100 : latestRemoteDrawId;
        System.out.println("maxDrawIdToSync: " + maxDrawIdToSync);
        
        /* Armazena os valores do último concurso cadastrado no banco local*/
        List<Integer> previousDrawNumbers = 
        		latestLocalDrawOpt.isPresent() ? latestLocalDrawOpt.get().getDrawNumbers().stream().map(LotofacilDrawNumber::getNumber).collect(Collectors.toList()) : new ArrayList<>();
        
        /* Loop: Do nosso último + 1 até o último da Caixa */
        for (long id = latestLocalDrawId + 1; id <= maxDrawIdToSync; id++) {
            
            /* Buscar concurso 'id' da Caixa */
        	CaixaDraw caixaDraw = restTemplate.getForObject(CAIXA_API_URL + id, CaixaDraw.class);
            
            /* Transformar DTO da Caixa -> LotofacilDraw */
        	LotofacilDraw newDraw = new LotofacilDraw();
            newDraw.setId(caixaDraw.getNumero()); 
            
            if (caixaDraw.getDataApuracao() != null) {
                LocalDate formattedDrawDate = LocalDate.parse(caixaDraw.getDataApuracao(), fmt1);
                newDraw.setDrawDate(formattedDrawDate);
            }
            
            int evenCount = 0;
            int oddCount = 0;
            int repeatedCount = 0;
            
            /* Lista para armazenar a dezena do concurso sincronizado na caixa*/
            List<Integer> currentDrawNumbers = new ArrayList<>();
            
            for (String numberStr : caixaDraw.getListaDezenas()) {
                int number = Integer.parseInt(numberStr);
                currentDrawNumbers.add(number);

                /* Calcula paridade */
                if (number % 2 == 0) evenCount++;
                else oddCount++;

                /* Calcula repetidos (comparando com 'previousDrawNumbers', que é buscado do último concurso local ou no final do for) */
                if (previousDrawNumbers.contains(number)) {
                    repeatedCount++;
                }
            }
            
            /* Popula o objeto newDraw com as informações do concurso sincronizado */
            newDraw.setEvenCount(evenCount);
            newDraw.setOddCount(oddCount);
            newDraw.setRepeatedCount(repeatedCount);
            
            System.out.println("aki");

            /* Criar os números filhos */
            for (int num : currentDrawNumbers) {
                boolean isRepeated = previousDrawNumbers.contains(num);
                /* Nota: Se você renomeou o método na entidade, mude para addDrawnNumber */
                newDraw.addDrawNumber(new LotofacilDrawNumber(num, isRepeated, newDraw));
            }
            
            System.out.println("aki2");
            
            /* Salvar no banco e atualiza os totais */
            repository.save(newDraw);
            if(newDraw.getId() != 1) lotofacilTotalsRepetitionsService.updateTotals(repeatedCount, newDraw.getId());
            lotofacilTotalsParitiesService.updateTotals(evenCount, oddCount, newDraw.getId());
            lotofacilTotalsNumbersService.updateTotals(currentDrawNumbers, newDraw.getId());
            
            System.out.println("aki3");
            lastSavedDrawId = newDraw.getId();
            
            /* Atualizar 'previousDrawNumbers' para o próximo loop */
            previousDrawNumbers = currentDrawNumbers;
            addedDrawsCount++;
        }
        
        /* Recalcula as porcentagens após todas as sincronizações */
        if (addedDrawsCount > 0) {
            System.out.println("Recalculando todas as porcentagens...");
            lotofacilTotalsRepetitionsService.recalculatePercentages(); 
            lotofacilTotalsParitiesService.recalculatePercentages();
            lotofacilTotalsNumbersService.recalculatePercentages(lastSavedDrawId);
            System.out.println("Recálculo concluído.");
        }
        
        /* Coloca mensagem de sincronização */
        syncMessage = "Sincronização concluída. " + addedDrawsCount + " novos concursos adicionados.";
        return new SynchronizeDrawResponse(lastSavedDrawId, addedDrawsCount, nextDrawDate, syncMessage, nextDrawId);
    }

	/* Inserir concurso manualmente */
	public LotofacilDraw insertManually(AddDrawRequest dto) {
        Long newDrawId = dto.getDrawId();

        /* Validar se o concurso já existe */
        if (repository.existsById(newDrawId)) {
            throw new DatabaseException("Draw " + newDrawId + " is already registered.");
        }

        /* Buscar dezenas do concurso anterior (para calcular repetidos) */
        List<Integer> previousDrawNumbers = new ArrayList<>();
        if (newDrawId > 1) {
            try {
            	LotofacilDraw previousDraw = findById(newDrawId - 1);
                previousDrawNumbers = previousDraw.getDrawNumbers().stream()
                                    .map(LotofacilDrawNumber::getNumber)
                                    .collect(Collectors.toList());
            } catch (ResourceNotFoundException e) {
                /* Se o anterior não existir (ex: cadastrando o 2 sem ter o 1), apenas não calcula repetidos */
                System.out.println("Warning: Previous draw " + (newDrawId - 1) + " not found. Repeated count will be 0.");
            }
        }

        /* Criar a nova entidade LotofacilDraw */
        LotofacilDraw newDraw = new LotofacilDraw();
        newDraw.setId(newDrawId); // Define o ID manualmente
        
        System.out.println("dto.getDrawDate() " + dto.getDrawDate());
        
        /* Verifica o formato da Data */
        if (dto.getDrawDate() != null && !dto.getDrawDate().isEmpty()) {
            LocalDate formattedDrawDate = LocalDate.parse(dto.getDrawDate());
            newDraw.setDrawDate(formattedDrawDate);
        }

        int evenCount = 0;
        int oddCount = 0;
        int repeatedCount = 0;
        
        /* Converte a List<String> do DTO passado na requisiçao para List<Integer> */
        List<Integer> currentDrawNumbers = dto.getDozens().stream()
                                          .map(Integer::parseInt)
                                          .collect(Collectors.toList());

        /* Calcular estatísticas e criar número do concurso */
        for (int number : currentDrawNumbers) {
            /* Calcula paridade */
            if (number % 2 == 0) evenCount++;
            else oddCount++;

            /* Calcula repetidos */
            boolean isRepeated = previousDrawNumbers.contains(number);
            if (isRepeated) {
                repeatedCount++;
            }
            
            /* Cria a entidade filha e associa ao "pai" */
            newDraw.addDrawNumber(new LotofacilDrawNumber(number, isRepeated, newDraw));
        }
        
        newDraw.setEvenCount(evenCount);
        newDraw.setOddCount(oddCount);
        newDraw.setRepeatedCount(repeatedCount);

        /* Salvar no banco */
        LotofacilDraw savedDraw;
        try {
             savedDraw = repository.save(newDraw);
        } catch (DataIntegrityViolationException e) {
             throw new DatabaseException("Failed to save draw: " + e.getMessage());
        }

        /* Atualizar os totais */
        if(savedDraw.getId() != 1) {
        	lotofacilTotalsRepetitionsService.updateTotals(repeatedCount, savedDraw.getId());
        }
        lotofacilTotalsParitiesService.updateTotals(evenCount, oddCount, savedDraw.getId());
        lotofacilTotalsNumbersService.updateTotals(currentDrawNumbers, savedDraw.getId());
        
        /* Recalcular as porcentagens */
        System.out.println("Recalculating all percentages after manual insertion...");
        lotofacilTotalsRepetitionsService.recalculatePercentages();
        lotofacilTotalsParitiesService.recalculatePercentages();
        lotofacilTotalsNumbersService.recalculatePercentages(savedDraw.getId());
        System.out.println("Recalculation finished.");

        /* Retornar o concurso salvo */
        return savedDraw;
    }
	
	/* Buscar por paginação */
	public Page<LotofacilDraw> findAllPaginated(Pageable pageable){
		/* Internamente, o Spring Data JPA traduz o Pageable em uma query SQL com LIMIT, OFFSET e ORDER BY */
	    return repository.findAll(pageable);
	}
	
}
