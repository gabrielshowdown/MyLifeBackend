package gabriel.hb.MyLifeBackend.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import gabriel.hb.MyLifeBackend.resources.dto.AddContestRequest;
import gabriel.hb.MyLifeBackend.resources.dto.GenerateContestRequest;
import gabriel.hb.MyLifeBackend.resources.dto.SyncronizeContestResponse;
import gabriel.hb.MyLifeBackend.services.ConcursoLotofacilService;

@RestController
@RequestMapping(value = "/concursoLotofacil")
public class ConcursoLotofacilResource {

	/* O Spring resolve essa injeção de dependencia e associar uma instancia de ConcursoLotofacilService */
	@Autowired
	private ConcursoLotofacilService service;

	/* Método para retorno de todos os concursos */
	@GetMapping
	public ResponseEntity<List<ConcursoLotofacil>> findAll() {
		List<ConcursoLotofacil> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

	/* Método para retorno por ID */
	@GetMapping(value = "/{id}")
	public ResponseEntity<ConcursoLotofacil> findById(@PathVariable Long id) { // Pega o valor passado de parâmetro da URL
		ConcursoLotofacil obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	/* Método para geração de concurso */
	@PostMapping("/generate")
	public ResponseEntity<ConcursoLotofacil> generateContest(@RequestBody GenerateContestRequest obj) { // Objeto chega como JSON e é deserializado para um obj ConcursoLotofacil
		ConcursoLotofacil conc = service.generateContest(obj.getConcursoAnteriorId(), obj.getQtdRepetidos(),
				obj.getQtdImpares(), obj.getQtdPares());
		return ResponseEntity.ok().body(conc);
	}

	/* Método para delete */
	@DeleteMapping(value = "/{id}") // 
	public ResponseEntity<Void> delete(@PathVariable Long id) { // Parâmetro passado na URL
		service.delete(id);
		return ResponseEntity.noContent().build(); // Retorna uma resposta vazia (código 204)
	}

	/* Método para sincronização */
	@PostMapping("/synchronize")
	public ResponseEntity<SyncronizeContestResponse> synchronizeContests() {
		SyncronizeContestResponse resultado = service.synchronizeWithCaixaApi();
		return ResponseEntity.ok().body(resultado);
	}

	/* Método para a inserção */
	@PostMapping("/insert") 
	public ResponseEntity<ConcursoLotofacil> insertManually(@RequestBody AddContestRequest dto) {
		ConcursoLotofacil novoConcurso = service.insertManually(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novoConcurso.getId()).toUri();
		return ResponseEntity.created(uri).body(novoConcurso); // Retorna 201 Created (igual ao 'insert' padrão)
	}

	/* Método para a paginação */
	/* @PageableDefault define valores padrão caso o cliente não envie parâmetros
	 * 'Pageable pageable': o Spring injeta automaticamente um objeto Pageable com base nos parâmetros da requisição (page, size, sort).
	 * Exemplo: /paginated?page=2&size=10&sort=nome,asc */
	@GetMapping(value = "/paginated")
	public ResponseEntity<Page<ConcursoLotofacil>> findAllPaginated(
			@PageableDefault(sort = "id", direction = Sort.Direction.DESC, page = 0, size = 4) Pageable pageable) {
		Page<ConcursoLotofacil> list = service.findAllPaginated(pageable);
		return ResponseEntity.ok().body(list); // Objeto do tipo 'Page' retorna também informações de paginação (número da página, total de páginas, total de elementos, etc.)
	}

}
