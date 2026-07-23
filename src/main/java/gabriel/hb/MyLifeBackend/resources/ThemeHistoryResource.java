package gabriel.hb.MyLifeBackend.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public ResponseEntity<ThemeHistory> insert(@RequestBody ThemeHistory obj) {
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }
}