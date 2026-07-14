package gabriel.hb.MyLifeBackend.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import gabriel.hb.MyLifeBackend.entities.BookBible;
import gabriel.hb.MyLifeBackend.entities.enums.ReadingCategory;
import gabriel.hb.MyLifeBackend.services.BookBibleService;

@RestController
@RequestMapping(value = "/books")
public class BookBibleResource {

    @Autowired
    private BookBibleService service;

    // GET Geral
    @GetMapping
    public ResponseEntity<List<BookBible>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    // GET por Categoria (O Angular pode chamar isso 5 vezes, ou filtrar no frontend)
    @GetMapping(value = "/category/{category}")
    public ResponseEntity<List<BookBible>> findByCategory(@PathVariable ReadingCategory category) {
        return ResponseEntity.ok().body(service.findByCategory(category));
    }

    // PUT para mudar a categoria de um livro
    @PutMapping(value = "/{id}/category")
    public ResponseEntity<BookBible> updateCategoria(@PathVariable Long id, @RequestBody ReadingCategory newCategory) {
    	BookBible updated = service.updateCategory(id, newCategory);
        return ResponseEntity.ok().body(updated);
    }
    
    @PostMapping // Método POST para insercao
	public ResponseEntity<BookBible> insert(@RequestBody BookBible obj){ // Objeto chega como JSON e é deserializado para um obj BookBible
		obj = service.insert(obj);
		// Trecho abaixo para retorna o código 201 e não o 200, e mostrar o id do user criado
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
				  buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj); 
	}
}