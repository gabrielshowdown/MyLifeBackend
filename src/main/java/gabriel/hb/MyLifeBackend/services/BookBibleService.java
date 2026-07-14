package gabriel.hb.MyLifeBackend.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // Adicione outras se encontrar exceções. As que não estiverem aqui, ficam como estão (Ex: Mt, Mc).

        // 2. Busca todos os livros cadastrados uma única vez para performar melhor
        List<BookBible> allBooks = repository.findAll();

        // 3. Quebra o texto recebido em linhas
        String[] lines = request.getRawText().split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();

            // Ignorar linhas em branco
            if (line.isEmpty()) continue;

            // Ignorar Títulos (ex: "Evangelios", "Cartas").
            // Sabemos que uma leitura real tem um espaço separando o livro do capítulo/versículo (ex: "Mt 23,23")
            if (!line.contains(" ")) {
                continue; 
            }

            // Separa a sigla do resto (capitulos/versículos)
            // Exemplo: "2Pe 3,13" -> parts[0] = "2Pe", parts[1] = "3,13"
            String[] parts = line.split(" ", 2);
            String originalAbbrev = parts[0];
            String verses = parts[1];

            // 4. Traduz a sigla se ela existir no dicionário
            String translatedAbbrev = translationMap.getOrDefault(originalAbbrev, originalAbbrev);
            String translatedReading = translatedAbbrev + " " + verses;

            // 5. Categorização (Encontra a sigla traduzida no banco de dados)
            BookBible matchedBook = allBooks.stream()
                    .filter(b -> b.getAbbreviation().equalsIgnoreCase(translatedAbbrev))
                    .findFirst()
                    .orElse(null);

            ReadingCategory category = (matchedBook != null) ? matchedBook.getCategory() : ReadingCategory.DESCARTADO;

            // 6. Adiciona na lista correta do Response
            switch (category) {
                case PRIMEIRA_LEITURA:
                    response.getPrimeiraLeitura().add(translatedReading);
                    break;
                case SEGUNDA_LEITURA:
                    response.getSegundaLeitura().add(translatedReading);
                    break;
                case TERCEIRA_LEITURA:
                    response.getTerceiraLeitura().add(translatedReading);
                    break;
                case EVANGELHO:
                    response.getEvangelhos().add(translatedReading);
                    break;
                case DESCARTADO:
                default:
                    response.getDescartados().add(translatedReading);
                    break;
            }
        }

        return response;
    }
}