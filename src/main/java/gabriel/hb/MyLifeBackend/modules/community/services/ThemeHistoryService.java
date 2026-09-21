package gabriel.hb.MyLifeBackend.modules.community.services;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gabriel.hb.MyLifeBackend.modules.community.entities.BookBible;
import gabriel.hb.MyLifeBackend.modules.community.entities.ThemeHistory;
import gabriel.hb.MyLifeBackend.modules.community.entities.enums.ReadingCategory;
import gabriel.hb.MyLifeBackend.modules.community.repositories.BookBibleRepository;
import gabriel.hb.MyLifeBackend.modules.community.repositories.ThemeHistoryRepository;
import gabriel.hb.MyLifeBackend.modules.community.resources.dto.CategorizedReadingsResponse;
import gabriel.hb.MyLifeBackend.modules.community.resources.dto.ProcessReadingsRequest;
import gabriel.hb.MyLifeBackend.shared.ResourceNotFoundException;

@Service
public class ThemeHistoryService {

    @Autowired
    private ThemeHistoryRepository repository;
    @Autowired
    private BookBibleRepository repositoryBooks;

    public ThemeHistory insert(ThemeHistory obj) {
        obj.setCreationDate(LocalDate.now());
        return repository.save(obj);
    }

    public List<ThemeHistory> findAll() {
        return repository.findAll();
    }
    
    public List<ThemeHistory> findByThemename(String themeName) {
    	List<ThemeHistory> obj = repository.findByThemeName(themeName); 
		if (obj.isEmpty()) {
			throw new ResourceNotFoundException(themeName);
		}
		return obj;
    }
    
    public ThemeHistory findById(Long id) {
		Optional<ThemeHistory> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id)); /* Poderia ser um return obj.get(); para pegar o 'ThemeHistory' do obj */
	}

    /* Método que pega o texto e faz a separação das leituras */
    /**/
    public CategorizedReadingsResponse processReadingsText(ProcessReadingsRequest request) {
        CategorizedReadingsResponse response = new CategorizedReadingsResponse(request.getThemeName());
        
        /* Cria um um map com os valores em Espanhol ; Portugues*/
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
        translationMap.put("Prov", "Pr");
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

        /* Busca todos os livros bíblicos cadastrados, pois cada um já vem com uma categoria de leitura definida */
        List<BookBible> allBooks = repositoryBooks.findAll();

        /* Map para ter os valores: Id do Livro ; Abreviação (para ser usado na ordenação) */
        @SuppressWarnings("unused") /* Evitar 'mensagem' de não usado no v2 */
		Map<String, Long> bookIdMap = allBooks.stream()
            .collect(Collectors.toMap(
                b -> b.getAbbreviation().toLowerCase(), 
                BookBible::getId, // Poderia ser b -> b.getId()
                (v1, v2) -> v1 /* Evita erros se houver siglas duplicadas no banco */
            ));

        /* Pega o texto filtrando por linha através de uma regex e armazena da linha dentro de um array*/
        String[] lines = request.getRawText().split("\\r?\\n");

        /* Percorre o Array, linha a linha*/
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || !line.contains(" ")) {
                continue; 
            }

            /* Separa a linha em duas partes: a sigla do livro (ex: "Gen") e o restante (ex: "1,1-5") */
            String[] parts = line.split(" ", 2);
            String originalAbbrev = parts[0];
            String verses = parts[1];

            /* Pega a sigla que veio no getRawText() que está no originalAbbrev e procura no map translationMap que foi criado para ter as siglas nos dois idiomas */
            /* Utiliza o getOrDefault (primeiro parâmetro, é que ele está procurando como 'chave' para obter o 'valor' do map, e o segundo é o valor caso não encontrar, que é a própria sigla em espanhol */
            /* Se a sigla que veio do texto (originalAbbrev, em espanhol) estiver no dicionário de tradução → retorna a sigla traduzida em português (ex: "Gen" → "Gn") */
            String translatedAbbrev = translationMap.getOrDefault(originalAbbrev, originalAbbrev);
            String translatedReading = translatedAbbrev + " " + verses;
            
            /* Nesse momento temos o 'translatedReading' traduzido para o português , Livro e versículos */

            /* Procura o objeto livro correspondente já cadastrado no banco, pela sigla traduzida */
            BookBible matchedBook = allBooks.stream()
                    .filter(b -> b.getAbbreviation().equalsIgnoreCase(translatedAbbrev))
                    .findFirst()
                    .orElse(null);

            /* Se não encontrar o livro cadastrado, a leitura cai automaticamente em DESCARTADO */
            ReadingCategory category = (matchedBook != null) ? matchedBook.getCategory() : ReadingCategory.DESCARTADO;

            /* Direciona a leitura já traduzida para a lista correspondente da categoria do livro, evitando duplicar a mesma leitura na mesma lista */
            switch (category) {
	            case PRIMEIRA_LEITURA: 
	                if (!response.getPrimeiraLeitura().contains(translatedReading)) {
	                    response.getPrimeiraLeitura().add(translatedReading); 
	                }
	                break;
	            case SEGUNDA_LEITURA: 
	                if (!response.getSegundaLeitura().contains(translatedReading)) {
	                    response.getSegundaLeitura().add(translatedReading); 
	                }
	                break;
	            case TERCEIRA_LEITURA: 
	                if (!response.getTerceiraLeitura().contains(translatedReading)) {
	                    response.getTerceiraLeitura().add(translatedReading); 
	                }
	                break;
	            case EVANGELHO: 
	                if (!response.getEvangelhos().contains(translatedReading)) {
	                    response.getEvangelhos().add(translatedReading); 
	                }
	                break;
	            default: 
	                if (!response.getDescartados().contains(translatedReading)) {
	                    response.getDescartados().add(translatedReading); 
	                }
	                break;
	        }
        }

        /* Lógica de Ordenação (ID do Banco/Ordem Livros da bíblia + Capítulo/Versículo) Poderia ser declarado for, porém precisaria tratar o bookIdMap */
        /* Recebe dois parâmetros r1 e r1 que retornam um inteiro: negativo → r1 vem antes do r2, zero → são iguais para fins de ordenação, positivo → r2 vem antes do r1 */
        Comparator<String> readingComparator = (r1, r2) -> {
            String[] p1 = r1.split(" ", 2);
            String[] p2 = r2.split(" ", 2);
            
            String sigla1 = p1[0].toLowerCase();
            String sigla2 = p2[0].toLowerCase();
            
            /* Pega o ID do banco. Se não achar, joga pro final (Long.MAX_VALUE) */
            Long id1 = bookIdMap.getOrDefault(sigla1, Long.MAX_VALUE);
            Long id2 = bookIdMap.getOrDefault(sigla2, Long.MAX_VALUE);
            
            /* Compara primeiro pelos IDs (Ordem canônica da Bíblia) */
            int idComparison = id1.compareTo(id2);
            if (idComparison != 0) {
                return idComparison;
            }
            
            /* Se for o MESMO livro (IDs iguais), vamos ordenar por capítulo e versículo */
            /* Armazena nesse 'versPart..' os captilos e versículos Ex: "1,26s", "3.5", */
            String versePart1 = p1.length > 1 ? p1[1] : "";
            String versePart2 = p2.length > 1 ? p2[1] : "";
            
            /* Extrai capítulo e versículo como números, para não ordenar "10" antes de "2" como texto */
            /* O array 'cv' fica com dois valores, o [0] para capitulo e o [1] para versículo*/
            int[] cv1 = extractChapterAndVerse(versePart1);
            int[] cv2 = extractChapterAndVerse(versePart2);
            
            /* Compara os capítulos [0]*/
            if (cv1[0] != cv2[0]) {
                return Integer.compare(cv1[0], cv2[0]);
            }

            /* Se o capítulo for igual, compara os versículos, desempata alfabeticamente para a versão sem letra ficar na frente */
            return versePart1.compareToIgnoreCase(versePart2);
        };

        /*Aplica a ordenação nas listas antes de devolver pro Angular */
        response.getPrimeiraLeitura().sort(readingComparator);
        response.getSegundaLeitura().sort(readingComparator);
        response.getTerceiraLeitura().sort(readingComparator);
        response.getEvangelhos().sort(readingComparator);
        response.getDescartados().sort(readingComparator);

        return response;
    }
    
    /* Método Auxiliar para extrair números inteiros de referências complexas (Ex: "1,26s", "3.5", "10,7s.18") */
    private int[] extractChapterAndVerse(String reference) {
        int chapter = 0;
        int verse = 0;
        try {

            /* Remove letras como 's', 'ss' e espaços, deixando apenas números e os separadores (, ou .) */
            String cleanRef = reference.replaceAll("[^0-9,.]", "");
            
            /* Divide entre capítulo e versículo usando vírgula ou ponto */
            String[] parts = cleanRef.split("[,.\\-]");
            
            if (parts.length > 0 && !parts[0].isEmpty()) {
                chapter = Integer.parseInt(parts[0]);
            }
            if (parts.length > 1 && !parts[1].isEmpty()) {
                verse = Integer.parseInt(parts[1]);
            }
        } catch (Exception e) {
            /* Se falhar no parser, apenas engole a exceção e retorna 0,0 para evitar quebrar a requisição */
        }
        return new int[]{chapter, verse};
    }
}