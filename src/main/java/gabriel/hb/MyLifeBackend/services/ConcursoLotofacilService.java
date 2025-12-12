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
import gabriel.hb.MyLifeBackend.resources.dto.AddContestRequest;
import gabriel.hb.MyLifeBackend.resources.dto.SyncronizeContestResponse;
import gabriel.hb.MyLifeBackend.services.dto.CaixaContest;
import gabriel.hb.MyLifeBackend.services.exceptions.DatabaseException;
import gabriel.hb.MyLifeBackend.services.exceptions.InvalidLParametersContestException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;

@Service /* Registra a classe como um componente/service do spring e vai poder ser injetado no ConcursoLotofacilResource */
public class ConcursoLotofacilService {
	
	/* O Spring resolve essa injeção de dependencia e associar uma instancia de ConcursoLotofacilRepository */
	@Autowired private ConcursoLotofacilRepository repository;
	@Autowired private TotaisRepeticoesLotofacilService totaisRepeticoesLotofacilService;
	@Autowired private TotaisParidadeLotofacilService totaisParidadeLotofacilService;
	@Autowired private TotaisNumerosLotofacilService totaisNumerosLotofacilService;
	private final String CAIXA_API_URL = "https://servicebus2.caixa.gov.br/portaldeloterias/api/lotofacil/";
	DateTimeFormatter fmt1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	/* Listar de todos os concursos */
	public List<ConcursoLotofacil> findAll(){
		return repository.findAll();
	}
	
	/* Consultar concurso por Id */
	public ConcursoLotofacil findById(Long id) {
		Optional<ConcursoLotofacil> obj = repository.findById(id); // o findById retona um Optional
        return obj.orElseThrow(() -> new ResourceNotFoundException(id)); // Poderia ser um return obj.get(); para pegar o 'ConcursoLotofacil' do obj;
	}
	
	/* Inserir novo concurso */
	public ConcursoLotofacil insert(ConcursoLotofacil obj) {
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
	public ConcursoLotofacil generateContest(Long concursoAnteriorId, int qtdRepetidos, int qtdImpares, int qtdPares) {
		
		System.out.println("concursoAnteriorId " + concursoAnteriorId);
		System.out.println("qtdRepetidos " + qtdRepetidos);
		System.out.println("qtdImpares " + qtdImpares);
		System.out.println("qtdPares " + qtdPares);
		
		/* Variáveis que serão usadas no método */
		int impLastConc = 0;
    	int parLastConc = 0;
    	int repetidosConcGener = 0;
    	int imparesConcGener = 0;
		int minimoImpar;
		int minimoPar;
    	boolean semRepeticao = false;
    	boolean semParidade = false;
		Set<Integer> concGenerate = new HashSet<>();
		List<Integer>listIntUltConc = new ArrayList<>();
		
		/* Tratamento para ver os valores passados na chamada do método */
		if (qtdImpares + qtdPares != 15 && qtdImpares + qtdPares != 0) {
			throw new InvalidLParametersContestException("Pares:" + qtdPares + " Impares:" + qtdImpares);
        }
		
		if (qtdRepetidos > 12 || qtdRepetidos < 6 && qtdRepetidos != 0) {
			throw new InvalidLParametersContestException("Repetição tem que ser entre 6 e 12 ");
        }
		
    	semRepeticao = (qtdRepetidos == 0) ?  true : false;
    	semParidade = (qtdImpares == 0) ? true : false;
    	System.out.println("semRepeticao " + semRepeticao);
    	System.out.println("semParidade " + semParidade);
    	
		/* Obtem as informações do concurso anterior que será usado como comparativo na geração */
		ConcursoLotofacil lastConc = findById(concursoAnteriorId);
		List<NumeroConcursoLotofacil> listUltConc = lastConc.getNumerosConcurso();
		listIntUltConc = listUltConc.stream().map(n -> n.getNumero()).collect(Collectors.toList());
		impLastConc = lastConc.getQtdImpares();
		parLastConc = lastConc.getQtdPares();
		
		System.out.println("listIntUltConc " + listIntUltConc);
		System.out.println("impLastConc " + impLastConc);
		System.out.println("parLastConc " + parLastConc);
		
		/* Mínimo de pares e impares que podem ser usados no concurso a ser gerado */
		minimoImpar = qtdRepetidos - parLastConc;
		minimoPar = qtdRepetidos - impLastConc;
		
		System.out.println("minimoPar: " + minimoPar + " minimoImpar: " + minimoImpar);
		
		/* Verifica se com os parametros passados, se é possível gerar o concurso ou não */
		/* Se possui repetição e paridade e se as quantidades forem menor que o mínimo estabelecido*/
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
				
				/* Indica que já preencheu tudo*/
				if (concGenerate.size() == 15) { 
					
					imparesConcGener = 0;
					
					/* Armezena quantos impares foram gerados no concurso */
					for (Integer n : concGenerate) {
						if (n %2 == 1) {
							imparesConcGener ++;
						}
					}
					
					/* Verifica se o número de impares gerado está de acordo com o solicitado */
					if (qtdImpares == imparesConcGener || semParidade == true) {
						gameValidate = true;
					}
					else { // Se a relação de paridade não estiver de acordo, limpa a lista de concursos
						concGenerate.clear();
						gameValidate = false;
						imparesConcGener = 0;
					}
					
					/* Se a relação de paridade bate com o solicitado, verifica a repetição */
					if (gameValidate == true) {
						
						repetidosConcGener = 0;
						
						/* Armazena quantos repetidos foram gerados em relação com o concurso anterior */
						for (int i = 0; i < 15; i++) {
							if (concGenerate.contains(listIntUltConc.get(i))) {
								repetidosConcGener ++;								
							}
						}
						
						/* Verifica se o número de repetições gerado está de acordo com o solicitado */
						if (qtdRepetidos == repetidosConcGener || semRepeticao == true) {
							gameValidate = true;
							// System.out.println("Os números repetidos do jogo anterior " + repetidosConcGener + ", batem com o selecionado pelo usuário " + qtdRepetidos);
						}
						else { // Se não não estiver de acordo, limpa a lista de concursos
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
			
			/* Armazena na lista de NumeroConcursoLotofacil o sorteio realizado*/
			for (Integer num : concGenerate) {
				listConcGenerate.add(new NumeroConcursoLotofacil(num, (listIntUltConc.contains(num) ? true : false), concLotoGenerate));
		 	}
			
			concLotoGenerate.setNumerosConcurso(listConcGenerate);
			
			return concLotoGenerate;
			
		}

	}
	
	/* Sincronização de Concursos */
	public SyncronizeContestResponse synchronizeWithCaixaApi() {
		
		/* RestTemplate é uma classe da biblioteca Spring usada para fazer chamadas HTTP de forma simples */
        RestTemplate restTemplate = new RestTemplate();
        CaixaContest ultimoConcursoCaixa = restTemplate.getForObject(CAIXA_API_URL, CaixaContest.class); // O getForObject converte o retorno da API para um objeto do tipo CaixaContest
        
        /* Variáveis que serão usadas no método */
        long ultimoConcursoRemotoId = ultimoConcursoCaixa.getNumero();
        long numeroConcursoProximo = ultimoConcursoCaixa.getNumeroConcursoProximo();
        long lastConcCadastrado = 0;
        long idConcASerSincronizado = 0;
        int concursosAdicionados = 0;
        LocalDate dateNextContest = LocalDate.parse(ultimoConcursoCaixa.getDataProximoConcurso(), fmt1);
        String textReturnedSyoncronized = "";
        
        System.out.println("ultimoConcursoRemotoId: " + ultimoConcursoRemotoId);
        System.out.println("data" + ultimoConcursoCaixa.getDataProximoConcurso());
        
        /* Descobrir o último concurso no DB local */
        /* Optional serve para representar a presença ou ausência de um valor de forma explícita, evitando o uso de null e reduzindo o risco de NullPointerException */
        Optional<ConcursoLotofacil> ultimoLocalOpt = repository.findTopByOrderByIdDesc();
        long ultimoConcursoLocalId = ultimoLocalOpt.isPresent() ? ultimoLocalOpt.get().getId() : 0; 
        System.out.println("ultimoConcursoLocalId: " + ultimoConcursoLocalId);
             
        idConcASerSincronizado = (ultimoConcursoRemotoId - ultimoConcursoLocalId >= 100) ? ultimoConcursoLocalId + 100 : ultimoConcursoRemotoId;
        System.out.println("idConcASerSincronizado: " + idConcASerSincronizado);
        
        /* Armazena os valores do último concurso cadastrado no banco local*/
        List<Integer> dezenasAnteriores = 
        		ultimoLocalOpt.isPresent() ? ultimoLocalOpt.get().getNumerosConcurso().stream().map(NumeroConcursoLotofacil::getNumero).collect(Collectors.toList()) : new ArrayList<>();
        
        /* Loop: Do nosso último + 1 até o último da Caixa */
        for (long id = ultimoConcursoLocalId + 1; id <= idConcASerSincronizado; id++) {
            
            /* Buscar concurso 'id' da Caixa */
        	CaixaContest concursoCaixa = restTemplate.getForObject(CAIXA_API_URL + id, CaixaContest.class);
            
            /* Transformar DTO da Caixa -> ConcursoLotofacil */
            ConcursoLotofacil novoConcurso = new ConcursoLotofacil();
            novoConcurso.setId(concursoCaixa.getNumero()); // Define o ID manualmente

            int pares = 0;
            int impares = 0;
            int repetidos = 0;
            
            /* Lista para armazenar a dezena do concurso sincronizado na caixa*/
            List<Integer> dezenasAtuais = new ArrayList<>();
            
            for (String dezenaStr : concursoCaixa.getListaDezenas()) {
                int dezena = Integer.parseInt(dezenaStr);
                dezenasAtuais.add(dezena);

                /* Calcula paridade */
                if (dezena % 2 == 0) pares++;
                else impares++;

                /* Calcula repetidos (comparando com 'dezenasAnteriores', que é buscado do último concurso local ou no final do for) */
                if (dezenasAnteriores.contains(dezena)) {
                    repetidos++;
                }
                
            }
            
            /* Popula o objeto novoConcurso com as informações do concurso sincronizado */
            novoConcurso.setQtdPares(pares);
            novoConcurso.setQtdImpares(impares);
            novoConcurso.setQtdRepetidos(repetidos);

            /* Criar os números filhos */
            for (int n : dezenasAtuais) {
                boolean isRepetido = dezenasAnteriores.contains(n);
                /* O construtor de NumeroConcursoLotofacil já associa ao 'pai'*/
                novoConcurso.adicionarNumeroSorteio(new NumeroConcursoLotofacil(n, isRepetido, novoConcurso));
            }

            /* Salvar no banco e atualiza os totais */
            repository.save(novoConcurso);
            if(novoConcurso.getId() != 1) totaisRepeticoesLotofacilService.atualizaTotais(repetidos, novoConcurso.getId());
            totaisParidadeLotofacilService.atualizaTotais(pares, impares, novoConcurso.getId());
            totaisNumerosLotofacilService.atualizaTotais(dezenasAtuais, novoConcurso.getId());
            
            lastConcCadastrado = novoConcurso.getId();
            
            /* Atualizar 'dezenasAnteriores' para o próximo loop */
            dezenasAnteriores = dezenasAtuais;
            concursosAdicionados++;
            
        }
        
        /* Recalcula as porcentagens após todas as sincronizaçõess*/
        if (concursosAdicionados > 0) {
            System.out.println("Recalculando todas as porcentagens...");
            // (Assumindo que o ID do último concurso é 'ultimoConcursoRemotoId')
            totaisRepeticoesLotofacilService.recalcularPorcentagens(); // Ajustar o service para ter o método público
            totaisParidadeLotofacilService.recalcularPorcentagens();
            totaisNumerosLotofacilService.recalcularPorcentagens(lastConcCadastrado);
            System.out.println("Recálculo concluído.");
        }
        
        textReturnedSyoncronized = "Sincronização concluída. " + concursosAdicionados + " novos concursos adicionados.";
        return new SyncronizeContestResponse(lastConcCadastrado, concursosAdicionados, dateNextContest, textReturnedSyoncronized, numeroConcursoProximo);
        
    }

	/* Inserir concurso manualmente */
	public ConcursoLotofacil insertManually(AddContestRequest dto) {
        Long novoConcursoId = dto.getConcursoId();

        /* Validar se o concurso já existe */
        if (repository.existsById(novoConcursoId)) {
            throw new DatabaseException("Concurso " + novoConcursoId + " já cadastrado.");
        }

        /* Buscar dezenas do concurso anterior (para calcular repetidos) */
        List<Integer> dezenasAnteriores = new ArrayList<>();
        if (novoConcursoId > 1) {
            try {
                ConcursoLotofacil concursoAnterior = findById(novoConcursoId - 1);
                dezenasAnteriores = concursoAnterior.getNumerosConcurso().stream()
                                    .map(NumeroConcursoLotofacil::getNumero)
                                    .collect(Collectors.toList());
            } catch (ResourceNotFoundException e) {
                /* Se o anterior não existir (ex: cadastrando o 2 sem ter o 1), apenas não calcula repetidos */
                System.out.println("Aviso: Concurso anterior " + (novoConcursoId - 1) + " não encontrado. Repetidos serão 0.");
            }
        }

        /* Criar a nova entidade ConcursoLotofacil */
        ConcursoLotofacil novoConcurso = new ConcursoLotofacil();
        novoConcurso.setId(novoConcursoId); // Define o ID manualmente (igual na sincronização)

        int pares = 0;
        int impares = 0;
        int repetidos = 0;
        
        /* Converte a List<String> do DTO passado na requisiçao para List<Integer> */
        List<Integer> dezenasAtuais = dto.getDezenas().stream()
                                          .map(Integer::parseInt)
                                          .collect(Collectors.toList());

        /* Calcular estatísticas e criar número do concurso */
        for (int dezena : dezenasAtuais) {
            /* Calcula paridade */
            if (dezena % 2 == 0) pares++;
            else impares++;

            /* Calcula repetidos */
            boolean isRepetido = dezenasAnteriores.contains(dezena);
            if (isRepetido) {
                repetidos++;
            }
            
            /* Cria a entidade filha e associa ao "pai" */
            novoConcurso.adicionarNumeroSorteio(new NumeroConcursoLotofacil(dezena, isRepetido, novoConcurso));
        }
        
        novoConcurso.setQtdPares(pares);
        novoConcurso.setQtdImpares(impares);
        novoConcurso.setQtdRepetidos(repetidos);

        /* Salvar no banco */
        ConcursoLotofacil concursoSalvo;
        try {
             concursoSalvo = repository.save(novoConcurso);
        } catch (DataIntegrityViolationException e) {
             throw new DatabaseException("Falha ao salvar concurso: " + e.getMessage());
        }


        /* ATUALIZAR AS ESTATÍSTICAS (CRUCIAL!) */
        if(concursoSalvo.getId() != 1) {
            totaisRepeticoesLotofacilService.atualizaTotais(repetidos, concursoSalvo.getId());
        }
        totaisParidadeLotofacilService.atualizaTotais(pares, impares, concursoSalvo.getId());
        totaisNumerosLotofacilService.atualizaTotais(dezenasAtuais, concursoSalvo.getId());
        
        /* Recalcular as porcentagens */
        System.out.println("Recalculando todas as porcentagens após inserção manual...");
        totaisRepeticoesLotofacilService.recalcularPorcentagens();
        totaisParidadeLotofacilService.recalcularPorcentagens();
        totaisNumerosLotofacilService.recalcularPorcentagens(concursoSalvo.getId());
        System.out.println("Recálculo concluído.");

        /* Retornar o concurso salvo */
        /* O frontend espera um ConcursoDetalhado (que é serializado a partir de ConcursoLotofacil) */
        return concursoSalvo;
    }
	
	/* Buscar por paginação */
	public Page<ConcursoLotofacil> findAllPaginated(Pageable pageable){
		/* Internamente, o Spring Data JPA traduz o Pageable em uma query SQL com LIMIT, OFFSET e ORDER BY */
	    return repository.findAll(pageable);
	}
	
}
