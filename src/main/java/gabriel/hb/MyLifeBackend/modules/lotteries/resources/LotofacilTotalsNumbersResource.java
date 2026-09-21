package gabriel.hb.MyLifeBackend.modules.lotteries.resources;

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

import gabriel.hb.MyLifeBackend.modules.lotteries.entitites.LotofacilTotalsNumbers;
import gabriel.hb.MyLifeBackend.modules.lotteries.services.LotofacilTotalsNumbersService;

@RestController
@RequestMapping(value = "/lotofacilTotalsNumbers")
public class LotofacilTotalsNumbersResource {
	
	@Autowired
	private LotofacilTotalsNumbersService service;
	
	/* Método para retorno de todos totais dos números */
	@GetMapping
	public ResponseEntity<List<LotofacilTotalsNumbers>> findAll(){
		List<LotofacilTotalsNumbers> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	/* Método para retorno por ID */
	@GetMapping(value = "/{id}")
	public ResponseEntity<LotofacilTotalsNumbers> findById(@PathVariable Long id){
		LotofacilTotalsNumbers obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	/* Método para a inserção */
	@PostMapping
	public ResponseEntity<LotofacilTotalsNumbers> insert(@RequestBody LotofacilTotalsNumbers obj){
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj); /* Trecho para retornar o código 201 e não o 200, e mostrar o id do registro criado */
	}
	
	/* Método para delete */
	@DeleteMapping(value = "/{id}") // Método DELETE do HTTP
	public ResponseEntity<Void> delete (@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build(); /* Retorna uma resposta vazia (código 204) */
	}
	
}
