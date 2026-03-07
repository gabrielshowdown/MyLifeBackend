package gabriel.hb.MyLifeBackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gabriel.hb.MyLifeBackend.entities.LotofacilTotalsParities;
import gabriel.hb.MyLifeBackend.repositories.LotofacilTotalsParitiesRepository;
import gabriel.hb.MyLifeBackend.services.exceptions.DatabaseException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;

@Service // Registra a classe como um componente/service do spring e vai poder ser
			// injetado no TotaisParidadeLotofacilResource
public class LotofacilTotalsParitiesService {

	@Autowired // O Spring resolve essa injeção de dependencia e associar uma instancia de
				// TotaisParidadeLotofacilRepository
	private LotofacilTotalsParitiesRepository repository;

	public List<LotofacilTotalsParities> findAll() {
		return repository.findAll();
	}

	public LotofacilTotalsParities findById(Long id) {
		Optional<LotofacilTotalsParities> obj = repository.findById(id); // o findById retona um Optional
		return obj.orElseThrow(() -> new ResourceNotFoundException(id)); // Poderia ser um return obj.get(); para pegar
																			// o 'TotaisParidadeLotofacil' do obj;
	}

	public LotofacilTotalsParities insert(LotofacilTotalsParities obj) {
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
	public void updateTotals(int even, int odd, Long drawId) {

		String parityKey = String.format("%02dI/%02dP", odd, even);

		// 2. Buscar o registro correspondente
		LotofacilTotalsParities total = repository.findByParity(parityKey)
				.orElseThrow(() -> new RuntimeException("Registro de paridade " + parityKey + " não encontrado."));

		// 3. Incrementar a quantidade
		total.setQuantity(total.getQuantity() + 1);
		repository.save(total);

		// 4. Recalcular porcentagens
		// recalcularPorcentagens();
	}

	public void recalculatePercentages() {
		List<LotofacilTotalsParities> totalsParitiesList = repository.findAll();

		int totalSum = totalsParitiesList.stream().mapToInt(LotofacilTotalsParities::getQuantity).sum();

		if (totalSum == 0) return;

		for (LotofacilTotalsParities total : totalsParitiesList) {
			double percentage = (total.getQuantity() * 100.0) / totalSum;
			total.setPercentage(percentage);
		}

		repository.saveAll(totalsParitiesList);
	}

}
