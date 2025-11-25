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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import gabriel.hb.MyLifeBackend.entities.ConcursoLotofacil;
import gabriel.hb.MyLifeBackend.entities.NumeroConcursoLotofacil;
import gabriel.hb.MyLifeBackend.repositories.ConcursoLotofacilRepository;
import gabriel.hb.MyLifeBackend.resources.dto.AddContestRequestDTO;
import gabriel.hb.MyLifeBackend.resources.dto.CaixaConcursoDTO;
import gabriel.hb.MyLifeBackend.resources.dto.SyncronizeContestResponseDTO;
import gabriel.hb.MyLifeBackend.services.exceptions.DatabaseException;
import gabriel.hb.MyLifeBackend.services.exceptions.InvalidLParametersContestException;
import gabriel.hb.MyLifeBackend.services.exceptions.InvalidLParametersException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;

@Service // Registra a classe como um componente/service do spring e vai poder ser injetado no ConcursoLotofacilResource
public class ConcursoLotofacilService {
	
	@Autowired //O Spring resolve essa injeção de dependencia e associar uma instancia de ConcursoLotofacilRepository
	private ConcursoLotofacilRepository repository;
	@Autowired
	private TotaisRepeticoesLotofacilService totaisRepeticoesLotofacilService;
	@Autowired
	private TotaisParidadeLotofacilService totaisParidadeLotofacilService;
	@Autowired
	private TotaisNumerosLotofacilService totaisNumerosLotofacilService;
	
	private final String CAIXA_API_URL = "https://servicebus2.caixa.gov.br/portaldeloterias/api/lotofacil/";
	DateTimeFormatter fmt1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public List<ConcursoLotofacil> findAll(){
		return repository.findAll();
	}
	
	public ConcursoLotofacil findById(Long id) {
		Optional<ConcursoLotofacil> obj = repository.findById(id); // o findById retona um Optional
        return obj.orElseThrow(() -> new ResourceNotFoundException(id)); // Poderia ser um return obj.get(); para pegar o 'ConcursoLotofacil' do obj;
	}
	
	public ConcursoLotofacil insert(ConcursoLotofacil obj) {
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
	
	public ConcursoLotofacil generateContest(Long concursoAnteriorId, int qtdRepetidos, int qtdImpares, int qtdPares) {
		
		
		System.out.println("concursoAnteriorId " + concursoAnteriorId);
		System.out.println("qtdRepetidos " + qtdRepetidos);
		System.out.println("qtdImpares " + qtdImpares);
		System.out.println("qtdPares " + qtdPares);
		
		int impLastConc = 0;
    	int parLastConc = 0;
    	boolean semRepeticao = false;
    	boolean semParidade = false;
    	int repetidosConcGener = 0;
    	int imparesConcGener = 0;
		int minimoImpar;
		int minimoPar;
		Set<Integer> concGenerate = new HashSet<>();
		List<Integer>listIntUltConc = new ArrayList<>();
		
		if (qtdImpares + qtdPares != 15 && qtdImpares + qtdPares != 0) {
			throw new InvalidLParametersException("Pares:" + qtdPares + " Impares:" + qtdImpares);
        }
		
		if (qtdRepetidos > 12 || qtdRepetidos < 6 && qtdRepetidos != 0) {
			throw new InvalidLParametersException("Repetição tem que ser entre 6 e 12 ");
        }
		
    	semRepeticao = (qtdRepetidos == 0) ?  true : false;
    	semParidade = (qtdImpares == 0) ? true : false;
    	System.out.println("semRepeticao " + semRepeticao);
    	System.out.println("semParidade " + semParidade);
		
		ConcursoLotofacil lastConc = findById(concursoAnteriorId);
		List<NumeroConcursoLotofacil> listUltConc = lastConc.getNumerosConcurso();
		listIntUltConc = listUltConc.stream().map(n -> n.getNumero()).collect(Collectors.toList());
		impLastConc = lastConc.getQtdImpares();
		parLastConc = lastConc.getQtdPares();
		
		System.out.println("listIntUltConc " + listIntUltConc);
		System.out.println("impLastConc " + impLastConc);
		System.out.println("parLastConc " + parLastConc);
		
		minimoImpar = qtdRepetidos - parLastConc;
		minimoPar = qtdRepetidos - impLastConc;
		
		System.out.println("minimoPar: " + minimoPar + " minimoImpar: " + minimoImpar);
		
		if ((semParidade == false && (qtdImpares < minimoImpar || qtdPares < minimoPar)) && semRepeticao == false) {
			
			System.out.println("Impossível gerar jogo");
			if (qtdImpares < minimoImpar) {
				throw new InvalidLParametersContestException ("Não é possível gerar o jogo! Jogo mínino para o concurso é : " + minimoImpar + "I/" + (15 - minimoImpar) + "P");
			}
			else {
				throw new InvalidLParametersContestException ("Não é possível gerar o jogo! Jogo mínino para o concurso é : " + (15 - minimoPar) + "I/" + minimoPar + "P");
			}
		}
		else {
			
			System.out.println("É possível gerar");
			boolean gameValidate = false;
			
			do {
				int numero = new Random().nextInt(25) + 1;
				
				if (concGenerate.size() < 15) {
					concGenerate.add(numero);
				}
				
				// Indica que já preencheu tudo
				if (concGenerate.size() == 15) { 
					
					imparesConcGener = 0;
					
					// Armezena quantos impares foram gerados no concurso
					for (Integer n : concGenerate) {
						if (n %2 == 1) {
							imparesConcGener ++;
						}
						
					}
					
					if (qtdImpares == imparesConcGener || semParidade == true) {
						gameValidate = true;
					}
					else {
						concGenerate.clear();
						gameValidate = false;
						imparesConcGener = 0;
					}
					
					if (gameValidate == true) {
						
						repetidosConcGener = 0;
						
						for (int i = 0; i < 15; i++) {
							if (concGenerate.contains(listIntUltConc.get(i))) {
								repetidosConcGener ++;								
							}
						}
						
						if (qtdRepetidos == repetidosConcGener || semRepeticao == true) {
							gameValidate = true;
							// System.out.println("Os números repetidos do jogo anterior " + repetidosConcGener + ", batem com o selecionado pelo usuário " + qtdRepetidos);
						}
						else {
							gameValidate = false;
							concGenerate.clear();
							// System.out.println("Os números repetidos do jogo anterior " + repetidosConcGener + ", não batem com o selecionado pelo usuário " + qtdRepetidos);
							repetidosConcGener = 0;
						}
						
					}
					
				}
				
				
			} while(concGenerate.size() < 15 && gameValidate == false);
			
			System.out.println("concGenerate: " + concGenerate);
			
			System.out.println("qtdImparesGerados: " + imparesConcGener);
			System.out.println("qtdParesGerados: " + (15 - imparesConcGener));
			System.out.println("qtdRepetidosGerados: " + repetidosConcGener);
			
			List<NumeroConcursoLotofacil> listConcGenerate = new ArrayList<>();	 	
			 
			ConcursoLotofacil concLotoGenerate = new ConcursoLotofacil();
			
			concLotoGenerate.setQtdImpares(imparesConcGener);
			concLotoGenerate.setQtdPares(15 - imparesConcGener);
			concLotoGenerate.setQtdRepetidos(repetidosConcGener);
			concLotoGenerate.setId(concursoAnteriorId + 1);
			
			
			for (Integer num : concGenerate) {
				listConcGenerate.add(new NumeroConcursoLotofacil(num, (listIntUltConc.contains(num) ? true : false), concLotoGenerate));
		 	}
			
			concLotoGenerate.setNumerosConcurso(listConcGenerate);
			
			return concLotoGenerate;
			
		}

	}
	
	public SyncronizeContestResponseDTO synchronizeWithCaixaApi() {
		
        RestTemplate restTemplate = new RestTemplate();
        CaixaConcursoDTO ultimoConcursoCaixa = restTemplate.getForObject(CAIXA_API_URL, CaixaConcursoDTO.class);
        
        long ultimoConcursoRemotoId = ultimoConcursoCaixa.getNumero();
        long numeroConcursoProximo = ultimoConcursoCaixa.getNumeroConcursoProximo();
        LocalDate dateNextContest = LocalDate.parse(ultimoConcursoCaixa.getDataProximoConcurso(), fmt1);
        String textReturnedSyoncronized = "";
        long lastConcCadastrado = 0;
        long idConcASerSincronizado = 0;
        
        System.out.println("ultimoConcursoRemotoId: " + ultimoConcursoRemotoId);
        System.out.println("data" + ultimoConcursoCaixa.getDataProximoConcurso());
        
        // 2. Descobrir o último concurso no nosso DB local
        Optional<ConcursoLotofacil> ultimoLocalOpt = repository.findTopByOrderByIdDesc();
        long ultimoConcursoLocalId = ultimoLocalOpt.isPresent() ? ultimoLocalOpt.get().getId() : 0;
        
        System.out.println("ultimoConcursoLocalId: " + ultimoConcursoLocalId);
        /*
        if (ultimoConcursoLocalId >= ultimoConcursoRemotoId) {
            return "Banco de dados já está atualizado. (Último: " + ultimoConcursoLocalId + ")";
        }
        */
        idConcASerSincronizado = (ultimoConcursoRemotoId - ultimoConcursoLocalId >= 100) ? ultimoConcursoLocalId + 100 : ultimoConcursoRemotoId;
        /*
        if (ultimoConcursoRemotoId - lastConcCadastrado >= 100) {
        	idConcASerSincronizado = lastConcCadastrado + 100;
        }
        else {
        	idConcASerSincronizado = ultimoConcursoRemotoId;
        }
        */
        System.out.println("idConcASerSincronizado: " + idConcASerSincronizado);
        int concursosAdicionados = 0;
        List<Integer> dezenasAnteriores = 
        		ultimoLocalOpt.isPresent() ? ultimoLocalOpt.get().getNumerosConcurso().stream().map(NumeroConcursoLotofacil::getNumero).collect(Collectors.toList()) : new ArrayList<>();
        
        // 3. Loop: Do nosso último + 1 até o último da Caixa
        for (long id = ultimoConcursoLocalId + 1; id <= idConcASerSincronizado; id++) { // ultimoConcursoRemotoId
            
            // 4. Buscar concurso 'id' da Caixa
            CaixaConcursoDTO concursoCaixa = restTemplate.getForObject(CAIXA_API_URL + id, CaixaConcursoDTO.class);
            
            // 5. Transformar DTO da Caixa -> Nossas Entidades
            ConcursoLotofacil novoConcurso = new ConcursoLotofacil();
            novoConcurso.setId(concursoCaixa.getNumero()); // Define o ID manualmente

            int pares = 0;
            int impares = 0;
            int repetidos = 0;
            
            List<Integer> dezenasAtuais = new ArrayList<>();
            for (String dezenaStr : concursoCaixa.getListaDezenas()) {
                int dezena = Integer.parseInt(dezenaStr);
                dezenasAtuais.add(dezena);

                // Calcula paridade
                if (dezena % 2 == 0) pares++;
                else impares++;

                // Calcula repetidos (comparando com 'dezenasAnteriores')
                if (dezenasAnteriores.contains(dezena)) {
                    repetidos++;
                }
                
                // (O NumeroConcursoLotofacil será criado abaixo)
            }
            
            novoConcurso.setQtdPares(pares);
            novoConcurso.setQtdImpares(impares);
            novoConcurso.setQtdRepetidos(repetidos);

            // 6. Criar os números filhos
            for (int dezena : dezenasAtuais) {
                boolean isRepetido = dezenasAnteriores.contains(dezena);
                // O construtor de NumeroConcursoLotofacil já associa ao 'pai'
                novoConcurso.adicionarNumeroSorteio(new NumeroConcursoLotofacil(dezena, isRepetido, novoConcurso));
            }

            // 7. Salvar no banco
            repository.save(novoConcurso);
            if(novoConcurso.getId() != 1) totaisRepeticoesLotofacilService.atualizaTotais(repetidos, novoConcurso.getId());
            totaisParidadeLotofacilService.atualizaTotais(pares, impares, novoConcurso.getId());
            totaisNumerosLotofacilService.atualizaTotais(dezenasAtuais, novoConcurso.getId());
            lastConcCadastrado = novoConcurso.getId();
            // 8. Atualizar 'dezenasAnteriores' para o próximo loop
            dezenasAnteriores = dezenasAtuais;
            concursosAdicionados++;
        }
        
        if (concursosAdicionados > 0) {
            System.out.println("Recalculando todas as porcentagens...");
            // (Assumindo que o ID do último concurso é 'ultimoConcursoRemotoId')
            totaisRepeticoesLotofacilService.recalcularPorcentagens(); // Ajustar o service para ter o método público
            totaisParidadeLotofacilService.recalcularPorcentagens();
            totaisNumerosLotofacilService.recalcularPorcentagens(lastConcCadastrado);
            System.out.println("Recálculo concluído.");
        }
        
        textReturnedSyoncronized = "Sincronização concluída. " + concursosAdicionados + " novos concursos adicionados.";
        return new SyncronizeContestResponseDTO(lastConcCadastrado, concursosAdicionados, dateNextContest, textReturnedSyoncronized, numeroConcursoProximo);
        // return "Sincronização concluída. " + concursosAdicionados + " novos concursos adicionados.";
    }

	public ConcursoLotofacil insertManually(AddContestRequestDTO dto) {
        Long novoConcursoId = dto.getConcursoId();

        // 1. Validar se o concurso já existe
        if (repository.existsById(novoConcursoId)) {
            throw new DatabaseException("Concurso " + novoConcursoId + " já cadastrado.");
        }

        // 2. Buscar dezenas do concurso anterior (para calcular repetidos)
        List<Integer> dezenasAnteriores = new ArrayList<>();
        if (novoConcursoId > 1) {
            try {
                ConcursoLotofacil concursoAnterior = findById(novoConcursoId - 1);
                dezenasAnteriores = concursoAnterior.getNumerosConcurso().stream()
                                    .map(NumeroConcursoLotofacil::getNumero)
                                    .collect(Collectors.toList());
            } catch (ResourceNotFoundException e) {
                // Se o anterior não existir (ex: cadastrando o 2 sem ter o 1), apenas não calcula repetidos
                System.out.println("Aviso: Concurso anterior " + (novoConcursoId - 1) + " não encontrado. Repetidos serão 0.");
            }
        }

        // 3. Criar a nova entidade ConcursoLotofacil
        ConcursoLotofacil novoConcurso = new ConcursoLotofacil();
        novoConcurso.setId(novoConcursoId); // Define o ID manualmente (igual na sincronização)

        int pares = 0;
        int impares = 0;
        int repetidos = 0;
        
        // Converte a List<String> do DTO para List<Integer>
        List<Integer> dezenasAtuais = dto.getDezenas().stream()
                                          .map(Integer::parseInt)
                                          .collect(Collectors.toList());

        // 4. Calcular estatísticas e criar números filhos
        for (int dezena : dezenasAtuais) {
            // Calcula paridade
            if (dezena % 2 == 0) pares++;
            else impares++;

            // Calcula repetidos
            boolean isRepetido = dezenasAnteriores.contains(dezena);
            if (isRepetido) {
                repetidos++;
            }
            
            // Cria a entidade filha e associa ao "pai"
            novoConcurso.adicionarNumeroSorteio(new NumeroConcursoLotofacil(dezena, isRepetido, novoConcurso));
        }
        
        novoConcurso.setQtdPares(pares);
        novoConcurso.setQtdImpares(impares);
        novoConcurso.setQtdRepetidos(repetidos);

        // 5. Salvar no banco
        ConcursoLotofacil concursoSalvo;
        try {
             concursoSalvo = repository.save(novoConcurso);
        } catch (DataIntegrityViolationException e) {
             throw new DatabaseException("Falha ao salvar concurso: " + e.getMessage());
        }


        // 6. ATUALIZAR AS ESTATÍSTICAS (CRUCIAL!)
        // (Copiado do método synchronizeWithCaixaApi)
        if(concursoSalvo.getId() != 1) {
            totaisRepeticoesLotofacilService.atualizaTotais(repetidos, concursoSalvo.getId());
        }
        totaisParidadeLotofacilService.atualizaTotais(pares, impares, concursoSalvo.getId());
        totaisNumerosLotofacilService.atualizaTotais(dezenasAtuais, concursoSalvo.getId());
        
        // 7. RECALCULAR PORCENTAGENS (CRUCIAL!)
        // (Copiado do método synchronizeWithCaixaApi)
        System.out.println("Recalculando todas as porcentagens após inserção manual...");
        totaisRepeticoesLotofacilService.recalcularPorcentagens();
        totaisParidadeLotofacilService.recalcularPorcentagens();
        totaisNumerosLotofacilService.recalcularPorcentagens(concursoSalvo.getId());
        System.out.println("Recálculo concluído.");

        // 8. Retornar o concurso salvo
        // O frontend espera um ConcursoDetalhado (que é serializado a partir de ConcursoLotofacil)
        return concursoSalvo;
    }
	
	public Page<ConcursoLotofacil> findAllPaginated(Pageable pageable){
	    return repository.findAll(pageable);
	}
	
}
