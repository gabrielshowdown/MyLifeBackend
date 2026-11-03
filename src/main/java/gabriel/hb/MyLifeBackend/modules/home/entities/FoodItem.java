package gabriel.hb.MyLifeBackend.modules.home.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import gabriel.hb.MyLifeBackend.modules.home.entities.enums.FoodCategory;
import gabriel.hb.MyLifeBackend.modules.home.entities.enums.FoodLocation;
import gabriel.hb.MyLifeBackend.modules.home.entities.enums.FoodUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_hm_item_alimento")
public class FoodItem implements Serializable {
	private static final long serialVersionUID = 1L;

	/* Atributos */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(name = "nome", nullable = false, length = 80)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "categoria", nullable = false, length = 20)
	private FoodCategory category;

	@Enumerated(EnumType.STRING)
	@Column(name = "local_armazenamento", nullable = false, length = 20)
	private FoodLocation location;

	@Column(name = "quantidade", nullable = false, precision = 10, scale = 2)
	private BigDecimal quantity;

	@Enumerated(EnumType.STRING)
	@Column(name = "unidade", nullable = false, length = 10)
	private FoodUnit unit;

	/* Só pode ser nulo em itens caseiros (ex.: torta congelada, sem prazo definido) */
	@Column(name = "data_vencimento")
	private LocalDate expirationDate;
	/* Data da compra ou, em itens caseiros, do preparo/congelamento */
	@Column(name = "data_compra")
	private LocalDate purchaseDate;

	@Column(name = "aberto", nullable = false)
	private boolean opened;
	@Column(name = "data_abertura")
	private LocalDate openedDate;

	@Column(name = "observacao", length = 200)
	private String notes;
	/* Emoji escolhido pelo usuário; se vazio o front usa o da categoria. Exige o banco em utf8mb4 */
	@Column(name = "emoji", length = 16)
	private String emoji;

	@Column(name = "sem_gluten", nullable = false)
	private boolean glutenFree;
	@Column(name = "sem_lactose", nullable = false)
	private boolean lactoseFree;
	@Column(name = "caseiro", nullable = false)
	private boolean homemade;

	/* Construtores */
	public FoodItem() {}

	/* Métodos Acessores */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FoodCategory getCategory() {
		return category;
	}

	public void setCategory(FoodCategory category) {
		this.category = category;
	}

	public FoodLocation getLocation() {
		return location;
	}

	public void setLocation(FoodLocation location) {
		this.location = location;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public FoodUnit getUnit() {
		return unit;
	}

	public void setUnit(FoodUnit unit) {
		this.unit = unit;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(LocalDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public LocalDate getOpenedDate() {
		return openedDate;
	}

	public void setOpenedDate(LocalDate openedDate) {
		this.openedDate = openedDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getEmoji() {
		return emoji;
	}

	public void setEmoji(String emoji) {
		this.emoji = emoji;
	}

	public boolean isGlutenFree() {
		return glutenFree;
	}

	public void setGlutenFree(boolean glutenFree) {
		this.glutenFree = glutenFree;
	}

	public boolean isLactoseFree() {
		return lactoseFree;
	}

	public void setLactoseFree(boolean lactoseFree) {
		this.lactoseFree = lactoseFree;
	}

	public boolean isHomemade() {
		return homemade;
	}

	public void setHomemade(boolean homemade) {
		this.homemade = homemade;
	}

	/* Métodos Comparativos */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FoodItem other = (FoodItem) obj;
		return Objects.equals(id, other.id);
	}
}
