package gabriel.hb.MyLifeBackend.config;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import gabriel.hb.MyLifeBackend.entities.User;
import gabriel.hb.MyLifeBackend.repositories.UserRepository;

@Configuration
@Profile("test2") // Colocar o ambiente do Application.properties que deseja mockar os dados
public class TestConfig2 implements CommandLineRunner{ // Interface que tem um método que executa quando a aplicação for iniciada
	
	@Autowired //o Spring resolve essa injeção de dependencia e associar uma instancia de UserRepository
	private UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
		
		User u1 = new User(null, "Maria Brown", "senha123", "F", "Jund", LocalDate.of(2020, 1, 15)); 
		User u2 = new User(null, "Alex Green", "senha123", "M", "Jundcity", LocalDate.parse("2024-01-20"));
		
		userRepository.saveAll(Arrays.asList(u1, u2));
		
	}
	
}
