package gabriel.hb.MyLifeBackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import gabriel.hb.MyLifeBackend.entities.LotofacilTotalsNumbers;
import gabriel.hb.MyLifeBackend.entities.LotofacilTotalsParities;
import gabriel.hb.MyLifeBackend.entities.LotofacilTotalsRepetitions;
import gabriel.hb.MyLifeBackend.repositories.LotofacilTotalsNumbersRepository;
import gabriel.hb.MyLifeBackend.repositories.LotofacilTotalsParitiesRepository;
import gabriel.hb.MyLifeBackend.repositories.LotofacilTotalsRepetitionsRepository;

@Configuration
@Profile("test") // Colocar o ambiente do Application.properties que deseja mockar os dados
public class TestConfig implements CommandLineRunner{ // Interface que tem um método que executa quando a aplicação for iniciada
	
	//o Spring resolve essa injeção de dependencia e associar uma instancia de Repository
	@Autowired private LotofacilTotalsRepetitionsRepository lotofacilTotalsRepetitionsRepository;
	@Autowired private LotofacilTotalsParitiesRepository lotofacilTotalsParitiesRepository;
	@Autowired private LotofacilTotalsNumbersRepository lotofacilTotalsNumbersRepository;
	
	@Override
	public void run(String... args) throws Exception {
		
		/* Trechos para inciar as tabelas de totais caso estejam vazias */
		if (lotofacilTotalsRepetitionsRepository.count() == 0) {
			int i = 5;
			do {
				lotofacilTotalsRepetitionsRepository.save(new LotofacilTotalsRepetitions(i, 0, 0.0));
				i++;
			} while (i <= 15);

		}
		if (lotofacilTotalsParitiesRepository.count() == 0) {
			lotofacilTotalsParitiesRepository.save(new LotofacilTotalsParities("13I/02P", 0, 0.0));
			lotofacilTotalsParitiesRepository.save(new LotofacilTotalsParities("12I/03P", 0, 0.0));
			lotofacilTotalsParitiesRepository.save(new LotofacilTotalsParities("11I/04P", 0, 0.0));
			lotofacilTotalsParitiesRepository.save(new LotofacilTotalsParities("10I/05P", 0, 0.0));
			lotofacilTotalsParitiesRepository.save(new LotofacilTotalsParities("09I/06P", 0, 0.0));
			lotofacilTotalsParitiesRepository.save(new LotofacilTotalsParities("08I/07P", 0, 0.0));
			lotofacilTotalsParitiesRepository.save(new LotofacilTotalsParities("07I/08P", 0, 0.0));
			lotofacilTotalsParitiesRepository.save(new LotofacilTotalsParities("06I/09P", 0, 0.0));
			lotofacilTotalsParitiesRepository.save(new LotofacilTotalsParities("05I/10P", 0, 0.0));
			lotofacilTotalsParitiesRepository.save(new LotofacilTotalsParities("04I/11P", 0, 0.0));
			lotofacilTotalsParitiesRepository.save(new LotofacilTotalsParities("03I/12P", 0, 0.0));
		}
		if (lotofacilTotalsNumbersRepository.count() == 0) {
			int i = 0;
			while (i < 25) {
				lotofacilTotalsNumbersRepository.save(new LotofacilTotalsNumbers(0, 0.0));
				i++;
			}
		}
	}
	
}
