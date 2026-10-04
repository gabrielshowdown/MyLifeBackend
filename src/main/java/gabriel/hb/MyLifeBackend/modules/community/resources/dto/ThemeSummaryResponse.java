package gabriel.hb.MyLifeBackend.modules.community.resources.dto;

import java.io.Serializable;
import java.time.LocalDate;

/* Poderia ser do jeito abaixo :*/
// public record ThemeSummaryResponse(Long id, String themeName, LocalDate celebrationDate) {}

public class ThemeSummaryResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /* Atributos */
    Long id;
    String themeName;
    LocalDate celebrationDate;
    
    /* Construtor */
	public ThemeSummaryResponse(Long id, String themeName, LocalDate celebrationDate) {
		super();
		this.id = id;
		this.themeName = themeName;
		this.celebrationDate = celebrationDate;
	}

	/* Métodos Acessores */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public LocalDate getCelebrationDate() {
		return celebrationDate;
	}

	public void setCelebrationDate(LocalDate celebrationDate) {
		this.celebrationDate = celebrationDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}