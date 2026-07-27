package gabriel.hb.MyLifeBackend.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import gabriel.hb.MyLifeBackend.entities.ThemeHistory;
import gabriel.hb.MyLifeBackend.services.ThemeHistoryService;

@RestController
@RequestMapping(value = "/themes")
public class ThemeHistoryResource {

    @Autowired
    private ThemeHistoryService service;

    @GetMapping
    public ResponseEntity<List<ThemeHistory>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }
    
    @GetMapping(value = "/{id}")
	public ResponseEntity<ThemeHistory> findById(@PathVariable Long id){ // Pega o valor passado de parâmetro da URL
    	ThemeHistory obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}
    
    @GetMapping(value = "/name/{themeName}")
    public ResponseEntity<List<ThemeHistory>> findByThemename(@PathVariable String themeName) {
        List<ThemeHistory> list = service.findByThemename(themeName);
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<ThemeHistory> insert(@RequestBody ThemeHistory obj) {
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }
}