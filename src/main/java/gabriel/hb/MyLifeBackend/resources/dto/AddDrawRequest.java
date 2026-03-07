package gabriel.hb.MyLifeBackend.resources.dto;

import java.util.List;

/* Classe para tratar a requisiçao que vem do frontend para adicionar concurso manualmente */
public class AddDrawRequest {
	
	/* Atributos */
	private Long drawId;
    private List<String> dozens; // Ex: ["01", "06", "07", ...]
    private String drawDate;

    /* Construtor padrão */
    public AddDrawRequest() {
    }

    /* Getters e Setters */
    public Long getDrawId() {
        return drawId;
    }

    public void setdrawId(Long drawId) {
        this.drawId = drawId;
    }

    public List<String> getDozens() {
        return dozens;
    }

    public void setdozens(List<String> dozens) {
        this.dozens = dozens;
    }
    
    public String getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(String drawDate) {
        this.drawDate = drawDate;
    }

}
