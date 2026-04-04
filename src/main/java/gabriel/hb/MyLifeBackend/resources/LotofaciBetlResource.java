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

import gabriel.hb.MyLifeBackend.entities.LotofacilBet;
import gabriel.hb.MyLifeBackend.resources.dto.PlaceBetRequest;
import gabriel.hb.MyLifeBackend.services.LotofacilBetService;

@RestController
@RequestMapping(value = "/lotofacilBet")
public class LotofaciBetlResource {

	/* O Spring resolve essa injeção de dependencia e associar uma instancia de LotofacilDrawService */
	@Autowired
	private LotofacilBetService service;

	/* Método para retorno de todos os concursos */
	@GetMapping
	public ResponseEntity<List<LotofacilBet>> findAll() {
		List<LotofacilBet> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

	/* Método para retorno por ID */
	@GetMapping(value = "/{id}")
	public ResponseEntity<LotofacilBet> findById(@PathVariable Long id) { // Pega o valor passado de parâmetro da URL
		LotofacilBet obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	/* Buscar apostas por concurso*/
	@GetMapping(value = "/draw/{id}")
	public ResponseEntity<List<LotofacilBet>>findByTargetDrawId(@PathVariable Long id) { // Pega o valor passado de parâmetro da URL
		List<LotofacilBet> list = service.findByTargetDrawId(id);
		return ResponseEntity.ok().body(list);
	}


	/* Método para delete */
	@DeleteMapping(value = "/{id}") // 
	public ResponseEntity<Void> delete(@PathVariable Long id) { // Parâmetro passado na URL
		service.delete(id);
		return ResponseEntity.noContent().build(); // Retorna uma resposta vazia (código 204)
	}

	/* Método para a inserção */
	@PostMapping("/insert") 
	public ResponseEntity<LotofacilBet> insertManually(@RequestBody PlaceBetRequest dto) {
		LotofacilBet newBet = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newBet.getId()).toUri();
		return ResponseEntity.created(uri).body(newBet); // Retorna 201 Created (igual ao 'insert' padrão)
	}

}
