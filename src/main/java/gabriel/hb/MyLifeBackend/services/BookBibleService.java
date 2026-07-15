package gabriel.hb.MyLifeBackend.services;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gabriel.hb.MyLifeBackend.entities.BookBible;
import gabriel.hb.MyLifeBackend.entities.enums.ReadingCategory;
import gabriel.hb.MyLifeBackend.repositories.BookBibleRepository;
import gabriel.hb.MyLifeBackend.resources.dto.CategorizedReadingsResponse;
import gabriel.hb.MyLifeBackend.resources.dto.ProcessReadingsRequest;
import gabriel.hb.MyLifeBackend.services.exceptions.BookBibleAlreadyRegisteredException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;

@Service
public class BookBibleService {

    @Autowired
    private BookBibleRepository repository;

    public List<BookBible> findAll() {
        return repository.findAll();
    }

    public List<BookBible> findByCategory(ReadingCategory category) {
        return repository.findByCategory(category);
    }

    // Método principal para o Frontend mudar um livro de lista
    public BookBible updateCategory(Long id, ReadingCategory newCategory) {
    	BookBible entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        
        entity.setCategory(newCategory);
        return repository.save(entity);
    }
    
    public BookBible insert(BookBible obj) {
		if(findByName(obj.getName()).isEmpty()) {
			return repository.save(obj);
		}
		else {
			throw new BookBibleAlreadyRegisteredException(" com o nome: " + obj.getName());
		}
	}
    
    public List<BookBible> findByName(String name) {
        return repository.findByName(name);
    }
    
    public CategorizedReadingsResponse processReadingsText(ProcessReadingsRequest request) {
        CategorizedReadingsResponse response = new CategorizedReadingsResponse(request.getThemeName());
        
        // 1. Dicionário de tradução (Espanhol -> Português)
        Map<String, String> translationMap = new HashMap<>();
        translationMap.put("Gen", "Gn");
        translationMap.put("Lev", "Lv");
        translationMap.put("Num", "Nm");
        translationMap.put("Tob", "Tb");
        translationMap.put("Jos", "Js");
        translationMap.put("Jue", "Jz");
        translationMap.put("1Sa", "1Sm");
        translationMap.put("2Sa", "2Sm");
        translationMap.put("1Re", "1Rs");
        translationMap.put("2Re", "2Rs");
        translationMap.put("Neh", "Ne");
        translationMap.put("1Mac", "1Mc");
        translationMap.put("2Mac", "2Mc");
        translationMap.put("Prov", "Pv");
        translationMap.put("Sal", "Sl");
        translationMap.put("Job", "Jó");
        translationMap.put("Sab", "Sb");
        translationMap.put("Jer", "Jr");
        translationMap.put("Miq", "Mq");
        translationMap.put("Dan", "Dn");
        translationMap.put("Bar", "Br");
        translationMap.put("Rom", "Rm");
        translationMap.put("1Tim", "1Tm");
        translationMap.put("2Tim", "2Tm");
        translationMap.put("Flp", "Fl");
        translationMap.put("Heb", "Hb");
        translationMap.put("Sant", "Tg");
        translationMap.put("1Pe", "1Pd");
        translationMap.put("2Pe", "2Pd");
        translationMap.put("1Tes", "1Ts");
        translationMap.put("2Tes", "2Ts");
        translationMap.put("Gal", "Gl");
        translationMap.put("Jn", "Jo");
        translationMap.put("1Jn", "1Jo");
        translationMap.put("2Jn", "2Jo");
        translationMap.put("3Jn", "3Jo");
        translationMap.put("Act", "At");
        translationMap.put("Pv", "Pr");
        translationMap.put("Zac", "Zc");
        translationMap.put("Lam", "Lm");
        translationMap.put("Col", "Cl");
        translationMap.put("Tit", "Tt");
        translationMap.put("Jon", "Jn");
        
     // 2. Busca todos os livros
        List<BookBible> allBooks = repository.findAll();

        // 3. Mapa Rápido para buscar o ID do livro pela Sigla (para usarmos na ordenação)
        Map<String, Long> bookIdMap = allBooks.stream()
            .collect(Collectors.toMap(
                b -> b.getAbbreviation().toLowerCase(), 
                BookBible::getId, 
                (v1, v2) -> v1 // Evita erros se houver siglas duplicadas no banco
            ));

        String[] lines = request.getRawText().split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || !line.contains(" ")) {
                continue; 
            }

            String[] parts = line.split(" ", 2);
            String originalAbbrev = parts[0];
            String verses = parts[1];

            String translatedAbbrev = translationMap.getOrDefault(originalAbbrev, originalAbbrev);
            String translatedReading = translatedAbbrev + " " + verses;

            BookBible matchedBook = allBooks.stream()
                    .filter(b -> b.getAbbreviation().equalsIgnoreCase(translatedAbbrev))
                    .findFirst()
                    .orElse(null);

            ReadingCategory category = (matchedBook != null) ? matchedBook.getCategory() : ReadingCategory.DESCARTADO;

            switch (category) {
                case PRIMEIRA_LEITURA: response.getPrimeiraLeitura().add(translatedReading); break;
                case SEGUNDA_LEITURA: response.getSegundaLeitura().add(translatedReading); break;
                case TERCEIRA_LEITURA: response.getTerceiraLeitura().add(translatedReading); break;
                case EVANGELHO: response.getEvangelhos().add(translatedReading); break;
                default: response.getDescartados().add(translatedReading); break;
            }
        }

        // 4. LÓGICA DE ORDENAÇÃO CUSTOMIZADA (ID do Banco + Capítulo/Versículo)
        Comparator<String> readingComparator = (r1, r2) -> {
            String[] p1 = r1.split(" ", 2);
            String[] p2 = r2.split(" ", 2);
            
            String sigla1 = p1[0].toLowerCase();
            String sigla2 = p2[0].toLowerCase();
            
            // Pega o ID do banco. Se não achar, joga pro final (Long.MAX_VALUE)
            Long id1 = bookIdMap.getOrDefault(sigla1, Long.MAX_VALUE);
            Long id2 = bookIdMap.getOrDefault(sigla2, Long.MAX_VALUE);
            
            // Compara primeiro pelos IDs (Ordem canônica da Bíblia)
            int idComparison = id1.compareTo(id2);
            if (idComparison != 0) {
                return idComparison;
            }
            
            // Se for o MESMO livro (IDs iguais), vamos ordenar por capítulo e versículo
            String versePart1 = p1.length > 1 ? p1[1] : "";
            String versePart2 = p2.length > 1 ? p2[1] : "";
            
            int[] cv1 = extractChapterAndVerse(versePart1);
            int[] cv2 = extractChapterAndVerse(versePart2);
            
            // Compara os capítulos
            if (cv1[0] != cv2[0]) {
                return Integer.compare(cv1[0], cv2[0]);
            }
            // Se o capítulo for igual, compara os versículos
            return Integer.compare(cv1[1], cv2[1]);
        };

        // 5. Aplica a ordenação nas listas antes de devolver pro Angular
        response.getPrimeiraLeitura().sort(readingComparator);
        response.getSegundaLeitura().sort(readingComparator);
        response.getTerceiraLeitura().sort(readingComparator);
        response.getEvangelhos().sort(readingComparator);
        response.getDescartados().sort(readingComparator);

        return response;
    }
    
    // Método Auxiliar para extrair números inteiros de referências complexas (Ex: "1,26s", "3.5", "10,7s.18")
    private int[] extractChapterAndVerse(String reference) {
        int chapter = 0;
        int verse = 0;
        try {
            // Remove letras como 's', 'ss' e espaços, deixando apenas números e os separadores (, ou .)
            String cleanRef = reference.replaceAll("[^0-9,.]", "");
            
            // Divide entre capítulo e versículo usando vírgula ou ponto
            String[] parts = cleanRef.split("[,.]");
            
            if (parts.length > 0 && !parts[0].isEmpty()) {
                chapter = Integer.parseInt(parts[0]);
            }
            if (parts.length > 1 && !parts[1].isEmpty()) {
                verse = Integer.parseInt(parts[1]);
            }
        } catch (Exception e) {
            // Se falhar no parser, apenas engole a exceção e retorna 0,0 para evitar quebrar a requisição
        }
        return new int[]{chapter, verse};
    }
}