package gabriel.hb.MyLifeBackend.modules.home.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import gabriel.hb.MyLifeBackend.modules.home.entities.FoodItem;
import gabriel.hb.MyLifeBackend.modules.home.repositories.FoodItemRepository;
import gabriel.hb.MyLifeBackend.shared.InvalidParametersException;
import gabriel.hb.MyLifeBackend.shared.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class FoodItemService {

	@Autowired
	private FoodItemRepository repository;

	public List<FoodItem> findAll() {
		/* Vencimento mais próximo primeiro; o front reordena conforme o usuário clica nas colunas */
		return repository.findAll(Sort.by(Sort.Direction.ASC, "expirationDate").and(Sort.by("id")));
	}

	public FoodItem findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public FoodItem insert(FoodItem obj) {
		obj.setId(null); /* Garante que é um insert, mesmo que o JSON venha com id */
		validate(obj);
		return repository.save(obj);
	}

	public FoodItem update(Long id, FoodItem obj) {
		try {
			FoodItem entity = repository.getReferenceById(id); /* Obj monitorado pelo JPA, sem ir ao banco como o findById */
			validate(obj);
			updateData(entity, obj);
			return repository.save(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException(id);
		}
		repository.deleteById(id);
	}

	private void updateData(FoodItem entity, FoodItem obj) {
		entity.setName(obj.getName());
		entity.setCategory(obj.getCategory());
		entity.setLocation(obj.getLocation());
		entity.setQuantity(obj.getQuantity());
		entity.setUnit(obj.getUnit());
		entity.setExpirationDate(obj.getExpirationDate());
		entity.setPurchaseDate(obj.getPurchaseDate());
		entity.setOpened(obj.isOpened());
		entity.setOpenedDate(obj.getOpenedDate());
		entity.setNotes(obj.getNotes());
		entity.setEmoji(obj.getEmoji());
		entity.setGlutenFree(obj.isGlutenFree());
		entity.setLactoseFree(obj.isLactoseFree());
		entity.setHomemade(obj.isHomemade());
	}

	/* Mesmas regras do modal do front. Lança InvalidParametersException (400) via ResourceExceptionHandler */
	private void validate(FoodItem obj) {
		if (obj.getName() == null || obj.getName().isBlank()) {
			throw new InvalidParametersException("O nome do item é obrigatório.");
		}
		obj.setName(obj.getName().trim());

		if (obj.getCategory() == null || obj.getLocation() == null || obj.getUnit() == null) {
			throw new InvalidParametersException("Informe a categoria, o local e a unidade do item.");
		}
		if (obj.getQuantity() == null || obj.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidParametersException("A quantidade deve ser maior que zero.");
		}
		/* Itens caseiros (ex.: torta congelada) podem não ter vencimento definido */
		if (obj.getExpirationDate() == null && !obj.isHomemade()) {
			throw new InvalidParametersException("A data de vencimento é obrigatória.");
		}
		if (obj.getPurchaseDate() != null && obj.getExpirationDate() != null
				&& obj.getPurchaseDate().isAfter(obj.getExpirationDate())) {
			throw new InvalidParametersException("A data de compra não pode ser posterior ao vencimento.");
		}
		/* Sem o flag de aberto, a data de abertura não faz sentido */
		if (!obj.isOpened()) {
			obj.setOpenedDate(null);
		}
		/* Texto em branco vira nulo, para o front cair no emoji da categoria */
		if (obj.getEmoji() != null && obj.getEmoji().isBlank()) {
			obj.setEmoji(null);
		}
	}
}
