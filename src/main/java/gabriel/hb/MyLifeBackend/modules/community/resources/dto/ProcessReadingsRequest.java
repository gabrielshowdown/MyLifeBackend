package gabriel.hb.MyLifeBackend.modules.community.resources.dto;

import java.io.Serializable;

/* Classe para tratar a entrada de dados da requisição para a filtragem de leituras (process-text) */
public class ProcessReadingsRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /* Atributos */
    private String themeName;
    private String rawText;

    /* Construtor */
    public ProcessReadingsRequest() {}

    /* Métodos Acessores */
    public String getThemeName() { 
    	return themeName; 
    }
    
    public void setThemeName(String themeName) { 
    	this.themeName = themeName; 
    }

    public String getRawText() { 
    	return rawText;
    }
    
    public void setRawText(String rawText) { 
    	this.rawText = rawText; 
    }
}