package gabriel.hb.MyLifeBackend.resources.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategorizedReadingsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String themeName;
    private List<String> primeiraLeitura = new ArrayList<>();
    private List<String> segundaLeitura = new ArrayList<>();
    private List<String> terceiraLeitura = new ArrayList<>();
    private List<String> evangelhos = new ArrayList<>();
    private List<String> descartados = new ArrayList<>();

    public CategorizedReadingsResponse(String themeName) {
        this.themeName = themeName;
    }

    // Getters
    public String getThemeName() { return themeName; }
    public List<String> getPrimeiraLeitura() { return primeiraLeitura; }
    public List<String> getSegundaLeitura() { return segundaLeitura; }
    public List<String> getTerceiraLeitura() { return terceiraLeitura; }
    public List<String> getEvangelhos() { return evangelhos; }
    public List<String> getDescartados() { return descartados; }
}