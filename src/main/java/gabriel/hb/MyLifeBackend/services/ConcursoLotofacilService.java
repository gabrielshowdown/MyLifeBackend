package gabriel.hb.MyLifeBackend.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import gabriel.hb.MyLifeBackend.entities.ConcursoLotofacil;
import gabriel.hb.MyLifeBackend.entities.NumeroConcursoLotofacil;
import gabriel.hb.MyLifeBackend.repositories.ConcursoLotofacilRepository;
import gabriel.hb.MyLifeBackend.services.exceptions.DatabaseException;
import gabriel.hb.MyLifeBackend.services.exceptions.InvalidLParametersContestException;
import gabriel.hb.MyLifeBackend.services.exceptions.InvalidLParametersException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;

@Service // Registra a classe como um componente/service do spring e vai poder ser injetado no ConcursoLotofacilResource
public class ConcursoLotofacilService {
	
	@Autowired //O Spring resolve essa injeção de dependencia e associar uma instancia de ConcursoLotofacilRepository
	private ConcursoLotofacilRepository repository;
	
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
		
		if (qtdImpares + qtdPares != 15 && qtdImpares + qtdPares != 0) {
			throw new InvalidLParametersException("Pares:" + qtdPares + " Impares:" + qtdImpares);
        }
		
		if (qtdRepetidos > 12 || qtdRepetidos < 6 && qtdRepetidos != 0) {
			throw new InvalidLParametersException("Repetição tem que ser entre 6 e 12 ");
        }
		
		int impLastConc = 0;
    	int parLastConc = 0;
    	boolean semRepeticao = false;
    	int repetidosConcGener = 0;
    	int imparesConcGener = 0;
    	int paresConcGener = 0;
		Set<Integer> concGenerate = new HashSet<>();
    	
    	semRepeticao = (qtdRepetidos == 0) ?  true : false;
    	
		List<Integer>listIntUltConc = new ArrayList<>();
		
		ConcursoLotofacil c = findById(concursoAnteriorId);
		List<NumeroConcursoLotofacil> listUltConc = c.getNumerosConcurso();
		listIntUltConc = listUltConc.stream().map(n -> n.getNumero()).collect(Collectors.toList());
		impLastConc = c.getQtdImpares();
		parLastConc = c.getQtdPares();
		
		System.out.println("listIntUltConc " + listIntUltConc);
		System.out.println("impLastConc " + impLastConc);
		System.out.println("parLastConc " + parLastConc);
		int minimoImpar;
		int minimoPar;
		
		minimoImpar = qtdRepetidos - parLastConc;
		minimoPar = qtdRepetidos - impLastConc;
		
		System.out.println("minimoPar: " + minimoPar + " minimoImpar: " + minimoImpar);
		
		if (qtdImpares < minimoImpar || qtdPares < minimoPar && semRepeticao == false) {
			
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
					
					if (qtdImpares == imparesConcGener) {
						gameValidate = true;
					}
					else {
						concGenerate.clear();
						gameValidate = false;
						imparesConcGener = 0;
					}
					
					if (gameValidate = true) {
						
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
		}
		
		System.out.println("concGenerate: " + concGenerate);
    	
    	
		return null;
	}
	
}
