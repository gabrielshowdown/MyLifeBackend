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

import gabriel.hb.MyLifeBackend.entities.LotofacilTotalsParities;
import gabriel.hb.MyLifeBackend.services.LotofacilTotalsParitiesService;

@RestController
@RequestMapping(value = "/lotofacilTotalsParities")
public class LotofacilTotalsParitiesResource {
	
	@Autowired /* O Spring resolve essa injeção de dependencia e associar uma instancia de TotaisParidadeLotofacilService */
	private LotofacilTotalsParitiesService service;
	
	/* Método para retorno de todos totais de paridade */
	@GetMapping
	public ResponseEntity<List<LotofacilTotalsParities>> findAll(){
		List<LotofacilTotalsParities> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	/* Método para retorno por ID */
	@GetMapping(value = "/{id}")
	public ResponseEntity<LotofacilTotalsParities> findById(@PathVariable Long id){ // Pega o valor passado de parâmetro da URL
		LotofacilTotalsParities obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	/* Método para a inserção */
	@PostMapping
	public ResponseEntity<LotofacilTotalsParities> insert(@RequestBody LotofacilTotalsParities obj){ // Objeto chega como JSON e é deserializado para um obj TotaisParidadeLotofacil
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj); // Trecho para retornar o código 201 e não o 200, e mostrar o id do user criado
	}
	
	/* Método para delete */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete (@PathVariable Long id){ // Parâmetro passado na URL
		service.delete(id);
		return ResponseEntity.noContent().build(); // Retorna uma resposta vazia (código 204)
	}
	
}
