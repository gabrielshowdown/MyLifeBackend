package gabriel.hb.MyLifeBackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import gabriel.hb.MyLifeBackend.entities.TotaisNumerosLotofacil;
import gabriel.hb.MyLifeBackend.entities.TotaisParidadeLotofacil;
import gabriel.hb.MyLifeBackend.entities.TotaisRepeticoesLotofacil;
import gabriel.hb.MyLifeBackend.repositories.TotaisNumerosLotofacilRepository;
import gabriel.hb.MyLifeBackend.repositories.TotaisParidadeLotofacilRepository;
import gabriel.hb.MyLifeBackend.repositories.TotaisRepeticoesLotofacilRepository;

@Configuration
@Profile("test") // Colocar o ambiente do Application.properties que deseja mockar os dados
public class TestConfig implements CommandLineRunner{ // Interface que tem um método que executa quando a aplicação for iniciada
	
	//o Spring resolve essa injeção de dependencia e associar uma instancia de Repository
	@Autowired private TotaisRepeticoesLotofacilRepository totaisRepeticoesLotofacilRepository;
	@Autowired private TotaisParidadeLotofacilRepository totaisParidadeLotofacilRepository;
	@Autowired private TotaisNumerosLotofacilRepository totaisNumerosLotofacilRepository;
	
	@Override
	public void run(String... args) throws Exception {
		
		/* Trechos para inciar as tabelas de totais caso estejam vazias */
		if (totaisRepeticoesLotofacilRepository.count() == 0) {
			int i = 5;
			do {
				totaisRepeticoesLotofacilRepository.save(new TotaisRepeticoesLotofacil(i, 0, 0.0));
				i++;
			} while (i <= 15);

		}
		if (totaisParidadeLotofacilRepository.count() == 0) {
			totaisParidadeLotofacilRepository.save(new TotaisParidadeLotofacil("13I/02P", 0, 0.0));
			totaisParidadeLotofacilRepository.save(new TotaisParidadeLotofacil("12I/03P", 0, 0.0));
			totaisParidadeLotofacilRepository.save(new TotaisParidadeLotofacil("11I/04P", 0, 0.0));
			totaisParidadeLotofacilRepository.save(new TotaisParidadeLotofacil("10I/05P", 0, 0.0));
			totaisParidadeLotofacilRepository.save(new TotaisParidadeLotofacil("09I/06P", 0, 0.0));
			totaisParidadeLotofacilRepository.save(new TotaisParidadeLotofacil("08I/07P", 0, 0.0));
			totaisParidadeLotofacilRepository.save(new TotaisParidadeLotofacil("07I/08P", 0, 0.0));
			totaisParidadeLotofacilRepository.save(new TotaisParidadeLotofacil("06I/09P", 0, 0.0));
			totaisParidadeLotofacilRepository.save(new TotaisParidadeLotofacil("05I/10P", 0, 0.0));
			totaisParidadeLotofacilRepository.save(new TotaisParidadeLotofacil("04I/11P", 0, 0.0));
			totaisParidadeLotofacilRepository.save(new TotaisParidadeLotofacil("03I/12P", 0, 0.0));
		}
		if (totaisNumerosLotofacilRepository.count() == 0) {
			int i = 0;
			while (i < 25) {
				totaisNumerosLotofacilRepository.save(new TotaisNumerosLotofacil(0, 0.0));
				i++;
			}
		}
	}
	
}
