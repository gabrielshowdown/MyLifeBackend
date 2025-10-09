package gabriel.hb.MyLifeBackend.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
		
    	/**
    	Cálculo que deve ser feito para verificar a compatibilidade do jogo gerado:
    	
    	Pegar o total desejado de repeticao e subtrair pelo menor do par ou impar
    	Exemplo: se o ultimo jogo tiver 11I / 4P , e for informado rep 9
    	Pegar o 9 e subtrair por 4 (que é o menor de par/impar, nesse caso o par)
		Esse resultado de 5 colocar como impar 
		Ou seja, o mínimo para um jogo é 5i/10p
    	*/
		
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
    	int impNextConc = 0;
    	int parNextConc = 0;
    	
    	int menorPrd;
    	String menorPrdString;
    	int rep = 0;
    	int qtdMinimaPrd;
    	int nextConcurso;
		List<Integer>listIntUltConc = new ArrayList<>();
		
		ConcursoLotofacil c = findById(concursoAnteriorId);
		List<NumeroConcursoLotofacil> listUltConc = c.getNumerosConcurso();
		listIntUltConc = listUltConc.stream().map(n -> n.getNumero()).collect(Collectors.toList());
		impLastConc = c.getQtdImpares();
		parLastConc = c.getQtdPares();
		
		System.out.println("listIntUltConc " + listIntUltConc);
		System.out.println("impLastConc " + impLastConc);
		System.out.println("parLastConc " + parLastConc);
		
		// Set do concurso que será gerado
    	Set<Integer> concGenerate = new HashSet<>();
    	
    	// Verifica se tem mais impar ou impar (Por exemplo: o último conc tem 9Ie6P , o 'menorPrd' fica 6 e o 'menorPrdString'
    	menorPrd = (impLastConc < parLastConc) ? impLastConc : parLastConc;
    	menorPrdString = (impLastConc < parLastConc) ? "Impar" : "Par";
    	System.out.println("Menor Paridade do último concurso : " + menorPrdString + " com " + menorPrd );
    	
    	// Lógica que verifica se é possível gerar um concurso com os atributos desejados
    	qtdMinimaPrd = qtdRepetidos - menorPrd;
    	
    	if(menorPrdString.equals("Impar")) {
    		impNextConc = 15 - qtdMinimaPrd;
    		parNextConc = qtdMinimaPrd;
    	}
    	else{
    		impNextConc = qtdMinimaPrd;
    		parNextConc = 15 - qtdMinimaPrd;
    	}
    	
    	System.out.println("Jogo mínino para o concurso é : " + impNextConc + "I/" + parNextConc + "P");
    	
		// Verifica se o valor digitado pelo usuário é menor que o jogo mínimo
		if (menorPrdString.equals("Par") && (qtdImpares < impNextConc && qtdPares > parNextConc) || (menorPrdString.equals("Impar") && (qtdImpares > impNextConc && qtdPares < parNextConc))) {
			throw new InvalidLParametersContestException ("Não é possível gerar o jogo! Jogo mínino para o concurso é : " + impNextConc + "I/" + parNextConc + "P");
		}
		else {
			System.out.println("É possível gerar jogo : " + impNextConc + "I/" + parNextConc + "P");
			int imparesConcGener = 0;
			int repetidosConcGener = 0;
    	
		}
		return null;
	}
	
}
