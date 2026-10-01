package gabriel.hb.MyLifeBackend.modules.community.resources.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* Classe para tratar a resposta ao processar filtragem de leituras (process-text) */
public class CategorizedReadingsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /* Atributos */
    private String themeName;
    private List<String> firstReading = new ArrayList<>();
    private List<String> secondReading = new ArrayList<>();
    private List<String> thirdReading = new ArrayList<>();
    private List<String> gospel = new ArrayList<>();
    private List<String> discarded = new ArrayList<>();

    /* Construtor */
    public CategorizedReadingsResponse(String themeName) {
        this.themeName = themeName;
    }

    /* Métodos Acessores */
    public String getThemeName() { return themeName; }
    public List<String> getFirstReading() { return firstReading; }
    public List<String> getSecondReading() { return secondReading; }
    public List<String> getThirdReading() { return thirdReading; }
    public List<String> getGospel() { return gospel; }
    public List<String> getDiscarded() { return discarded; }
}