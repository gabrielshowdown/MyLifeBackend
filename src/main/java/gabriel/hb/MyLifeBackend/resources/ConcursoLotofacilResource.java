package gabriel.hb.MyLifeBackend.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import gabriel.hb.MyLifeBackend.entities.ConcursoLotofacil;
import gabriel.hb.MyLifeBackend.resources.dto.GenerateContestRequestDTO;
import gabriel.hb.MyLifeBackend.resources.dto.SyncronizeContestResponseDTO;
import gabriel.hb.MyLifeBackend.services.ConcursoLotofacilService;

@RestController
@RequestMapping(value = "/concursoLotofacil")
public class ConcursoLotofacilResource {
	
	@Autowired //O Spring resolve essa injeção de dependencia e associar uma instancia de ConcursoLotofacilService
	private ConcursoLotofacilService service;
	
	@GetMapping
	public ResponseEntity<List<ConcursoLotofacil>> findAll(){
		List<ConcursoLotofacil> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ConcursoLotofacil> findById(@PathVariable Long id){ // Pega o valor passado de parâmetro da URL
		ConcursoLotofacil obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	@PostMapping // Método POST para insercao
	public ResponseEntity<ConcursoLotofacil> insert(@RequestBody ConcursoLotofacil obj){ // Objeto chega como JSON e é deserializado para um obj ConcursoLotofacil
		obj = service.insert(obj);
		// Trecho abaixo para retorna o código 201 e não o 200, e mostrar o id do user criado
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
				  buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj); 
	}
	
	@PostMapping("/generate") // Método POST para insercao
	public ResponseEntity<ConcursoLotofacil> generateContest (@RequestBody GenerateContestRequestDTO obj){ // Objeto chega como JSON e é deserializado para um obj ConcursoLotofacil
		ConcursoLotofacil conc = service.generateContest(obj.getConcursoAnteriorId(), obj.getQtdRepetidos(), obj.getQtdImpares(), obj.getQtdPares());
		return ResponseEntity.ok().body(conc);
	}
	
	@DeleteMapping(value = "/{id}") // Método DELETE do HTTP
	public ResponseEntity<Void> delete (@PathVariable Long id){ // Parâmetro passado na URL
		service.delete(id);
		return ResponseEntity.noContent().build(); // Retorna uma resposta vazia (código 204)
	}
	
	@PostMapping("/synchronize")
    public ResponseEntity<SyncronizeContestResponseDTO> synchronizeContests() {
        //try {
            // Delega toda a lógica para o Service
        	SyncronizeContestResponseDTO resultado = service.synchronizeWithCaixaApi(); 
            return ResponseEntity.ok().body(resultado);
        //} catch (Exception e) {
        //    return ResponseEntity.status(500).body("Erro durante a sincronização: " + e.getMessage());
        //}
    }
	
}
