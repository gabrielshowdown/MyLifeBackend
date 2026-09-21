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

import gabriel.hb.MyLifeBackend.modules.lotteries.entitites.LotofacilDrawNumber;
import gabriel.hb.MyLifeBackend.modules.lotteries.services.LotofacilDrawNumberService;

@RestController
@RequestMapping(value = "/lotofacilDrawNumber")
public class LotofacilDrawNumberResource {
	
	@Autowired
	private LotofacilDrawNumberService service;
	
	/* Método para retorno de todos os numeros de concurso */
	@GetMapping
	public ResponseEntity<List<LotofacilDrawNumber>> findAll(){
		List<LotofacilDrawNumber> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	/* Método para retorno por ID */
	@GetMapping(value = "/{id}")
	public ResponseEntity<LotofacilDrawNumber> findById(@PathVariable Long id){
		LotofacilDrawNumber obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	/* Método para a inserção */
	@PostMapping 
	public ResponseEntity<LotofacilDrawNumber> insert(@RequestBody LotofacilDrawNumber obj){
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
				  buildAndExpand(obj.getId()).toUri();
		 /* Trecho para retornar o código 201 e não o 200, e mostrar o id do número criado */
		return ResponseEntity.created(uri).body(obj);
	}
	
	/* Método para delete */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete (@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build(); /* Retorna uma resposta vazia (código 204) */
	}
	
	/** Novo endpoint para buscar números por ID do concurso
	@GetMapping(value = "/draw/{drawId}")
	public ResponseEntity<List<LotofacilDrawNumber>> findByDrawId(@PathVariable Long drawId){
		List<LotofacilDrawNumber> list = service.findByDrawId(drawId);
		return ResponseEntity.ok().body(list);
	}	
	*/
}
