package gabriel.hb.MyLifeBackend.resources;

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

import gabriel.hb.MyLifeBackend.entities.Item;
import gabriel.hb.MyLifeBackend.services.ItemService;

@RestController
@RequestMapping(value = "/items")
public class ItemResource {
	
	@Autowired //O Spring resolve essa injeção de dependencia e associar uma instancia de UserService
	private ItemService service;
	
	@GetMapping
	public ResponseEntity<List<Item>> findAll(){
		List<Item> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Item> findById(@PathVariable Long id){ // Pega o valor passado de parâmetro da URL
		Item obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	@PostMapping // Método POST para insercao
	public ResponseEntity<Item> insert(@RequestBody Item obj){ // Objeto chega como JSON e é deserializado para um obj item
		obj = service.insert(obj);
		// Trecho abaixo para retorna o código 201 e não o 200, e mostrar o id do user criado
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
				  buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj); 
	}
	
	@DeleteMapping(value = "/{id}") // Método DELETE do HTTP
	public ResponseEntity<Void> delete (@PathVariable Long id){ // Parâmetro passado na URL
		service.delete(id);
		return ResponseEntity.noContent().build(); // Retorna uma resposta vazia (código 204)
	}
	
	
	@PutMapping(value = "/{id}")// Método PUT do HTTP
	public ResponseEntity<Item> update(@PathVariable Long id, @RequestBody Item obj){ // O update recebe o id a ser editado e o corpo do item editado
		obj = service.update(id, obj);
		return ResponseEntity.ok().body(obj);
	}
}
