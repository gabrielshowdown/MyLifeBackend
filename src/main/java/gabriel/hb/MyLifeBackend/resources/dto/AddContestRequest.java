package gabriel.hb.MyLifeBackend.resources.dto;

import java.util.List;

/* Classe para tratar a requisiçao que vem do frontend para adicionar concurso manualmente */
public class AddContestRequest {
	
	/* Atributos */
	private Long concursoId;
    private List<String> dezenas; // Ex: ["01", "06", "07", ...]

    /* Construtor padrão */
    public AddContestRequest() {
    }

    /* Getters e Setters */
    public Long getConcursoId() {
        return concursoId;
    }

    public void setConcursoId(Long concursoId) {
        this.concursoId = concursoId;
    }

    public List<String> getDezenas() {
        return dezenas;
    }

    public void setDezenas(List<String> dezenas) {
        this.dezenas = dezenas;
    }

}
