package gabriel.hb.MyLifeBackend.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import gabriel.hb.MyLifeBackend.entities.ThemeHistory;
import gabriel.hb.MyLifeBackend.services.PdfExportService;
import gabriel.hb.MyLifeBackend.services.ThemeHistoryService;

@RestController
@RequestMapping(value = "/themes")
public class ThemeHistoryResource {

    @Autowired
    private ThemeHistoryService service;
    @Autowired
    private PdfExportService pdfService;

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
    
    @GetMapping(value = "/{id}/export-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long id) {
        ThemeHistory obj = service.findById(id); // Busca do banco
        byte[] pdfBytes = pdfService.generateThemePdf(obj); // Gera o PDF
        
        HttpHeaders headers = new HttpHeaders();
        // O "attachment" força o navegador a fazer o download com o nome sugerido
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Tema_" + obj.getId() + ".pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    // NOVO: Endpoint para exportar um PDF de um tema não salvo (Preview)
    @PostMapping(value = "/export-pdf-preview", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportPdfPreview(@RequestBody ThemeHistory obj) {
        
        // Enviamos o objeto que veio da memória do Frontend direto para o gerador de PDF
        byte[] pdfBytes = pdfService.generateThemePdf(obj); 
        
        HttpHeaders headers = new HttpHeaders();
        // Sugere o nome do arquivo para o download
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Leituras_" + obj.getThemeName() + ".pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}