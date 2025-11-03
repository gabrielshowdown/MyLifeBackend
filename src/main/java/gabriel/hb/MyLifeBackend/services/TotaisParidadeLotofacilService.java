package gabriel.hb.MyLifeBackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gabriel.hb.MyLifeBackend.entities.TotaisParidadeLotofacil;
import gabriel.hb.MyLifeBackend.repositories.TotaisParidadeLotofacilRepository;
import gabriel.hb.MyLifeBackend.services.exceptions.DatabaseException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;

@Service // Registra a classe como um componente/service do spring e vai poder ser
			// injetado no TotaisParidadeLotofacilResource
public class TotaisParidadeLotofacilService {

	@Autowired // O Spring resolve essa injeção de dependencia e associar uma instancia de
				// TotaisParidadeLotofacilRepository
	private TotaisParidadeLotofacilRepository repository;

	public List<TotaisParidadeLotofacil> findAll() {
		return repository.findAll();
	}

	public TotaisParidadeLotofacil findById(Long id) {
		Optional<TotaisParidadeLotofacil> obj = repository.findById(id); // o findById retona um Optional
		return obj.orElseThrow(() -> new ResourceNotFoundException(id)); // Poderia ser um return obj.get(); para pegar
																			// o 'TotaisParidadeLotofacil' do obj;
	}

	public TotaisParidadeLotofacil insert(TotaisParidadeLotofacil obj) {
		return repository.save(obj);
	}

	public void delete(Long id) {
		try {
			if (repository.existsById(id)) {
				repository.deleteById(id);
			} else {
				throw new ResourceNotFoundException(id); // Lança uma exceção através do 'ResourceExceptionHandler', que
															// captura as excecões que ocorrem
			}
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Transactional
	public void atualizaTotais(int pares, int impares, Long idConcurso) {

		String chaveParidade = String.format("%02dI/%02dP", impares, pares);

		// 2. Buscar o registro correspondente
		TotaisParidadeLotofacil total = repository.findByParidade(chaveParidade)
				.orElseThrow(() -> new RuntimeException("Registro de paridade " + chaveParidade + " não encontrado."));

		// 3. Incrementar a quantidade
		total.setQtd(total.getQtd() + 1);
		repository.save(total);

		// 4. Recalcular porcentagens
		// recalcularPorcentagens();
	}

	public void recalcularPorcentagens() {
		List<TotaisParidadeLotofacil> totais = repository.findAll();

		int somaTotal = totais.stream().mapToInt(TotaisParidadeLotofacil::getQtd).sum();

		if (somaTotal == 0) return;

		for (TotaisParidadeLotofacil total : totais) {
			double porcentagem = (total.getQtd() * 100.0) / somaTotal;
			total.setPorcentagem(porcentagem);
		}

		repository.saveAll(totais);
	}

}
