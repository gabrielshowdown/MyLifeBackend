package gabriel.hb.MyLifeBackend.modules.community.repositories.projection;

import java.time.LocalDate;

public interface ThemeSummaryProjection {
	
	Long getId();
    String getThemeName();
    LocalDate getCelebrationDate();

}
