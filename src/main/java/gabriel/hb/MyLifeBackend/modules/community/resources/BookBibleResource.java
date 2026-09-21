package gabriel.hb.MyLifeBackend.modules.community.resources;

import java.net.URI;
import java.util.List;

import gabriel.hb.MyLifeBackend.modules.community.services.BookBibleService;
import gabriel.hb.MyLifeBackend.modules.community.entities.enums.ReadingCategory;
import gabriel.hb.MyLifeBackend.modules.community.entities.BookBible;
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

@RestController
@RequestMapping(value = "/books")
public class BookBibleResource {

    @Autowired
    private BookBibleService service;

    @GetMapping
    public ResponseEntity<List<BookBible>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping(value = "/category/{category}")
    public ResponseEntity<List<BookBible>> findByCategory(@PathVariable ReadingCategory category) {
        return ResponseEntity.ok().body(service.findByCategory(category));
    }

    @PutMapping(value = "/{id}/category")
    public ResponseEntity<BookBible> updateCategoria(@PathVariable Long id, @RequestBody ReadingCategory newCategory) {
    	BookBible updated = service.updateCategory(id, newCategory);
        return ResponseEntity.ok().body(updated);
    }
    
    @PostMapping
	public ResponseEntity<BookBible> insert(@RequestBody BookBible obj){
		obj = service.insert(obj);
		/* Trecho abaixo para retorna o código 201 e não o 200, e mostra o id do livro criado */
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
				  buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj); 
	}

}