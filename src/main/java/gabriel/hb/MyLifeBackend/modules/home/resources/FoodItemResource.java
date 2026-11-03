package gabriel.hb.MyLifeBackend.modules.home.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import gabriel.hb.MyLifeBackend.modules.home.entities.FoodItem;
import gabriel.hb.MyLifeBackend.modules.home.services.FoodItemService;

@RestController
@RequestMapping(value = "/food-items")
public class FoodItemResource {

	@Autowired
	private FoodItemService service;

	@GetMapping
	public ResponseEntity<List<FoodItem>> findAll() {
		return ResponseEntity.ok().body(service.findAll());
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<FoodItem> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(service.findById(id));
	}

	@PostMapping
	public ResponseEntity<FoodItem> insert(@RequestBody FoodItem obj) {
		obj = service.insert(obj);
		/* Retorna o código 201 e o id do item criado */
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<FoodItem> update(@PathVariable Long id, @RequestBody FoodItem obj) {
		return ResponseEntity.ok().body(service.update(id, obj));
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build(); /* Resposta vazia (código 204) */
	}
}
