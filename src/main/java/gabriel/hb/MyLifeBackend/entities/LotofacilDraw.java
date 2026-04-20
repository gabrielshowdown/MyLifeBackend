package gabriel.hb.MyLifeBackend.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_concurso_lotofacil")
public class LotofacilDraw implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// Atributos
	@Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name = "qtd_pares")
    private int evenCount; // É gerado como 'even_Count' no banco, seria como colocar um @Column(name="even_Count")
	@Column(name = "qtd_impares")
	private int oddCount;
	@Column(name = "qtd_repetidos")
    private int repeatedCount;
	@Column(name = "data_apuracao")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate drawDate;
	
	@Column(name = "oficial")
    private boolean isOfficial;
    
    @OneToMany(mappedBy = "draw", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LotofacilDrawNumber> drawNumbers = new ArrayList<>();
	
	// Construtores
    public LotofacilDraw() {
    }

    public LotofacilDraw(int evenCount, int oddCount) {
        this.evenCount = evenCount;
        this.oddCount = oddCount;
    }

	// Métodos Acessores
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getEvenCount() {
		return evenCount;
	}

	public void setEvenCount(int evenCount) {
		this.evenCount = evenCount;
	}

	public int getOddCount() {
		return oddCount;
	}

	public void setOddCount(int oddCount) {
		this.oddCount = oddCount;
	}

	public int getRepeatedCount() {
		return repeatedCount;
	}

	public void setRepeatedCount(int repeatedCount) {
		this.repeatedCount = repeatedCount;
	}

	public List<LotofacilDrawNumber> getDrawNumbers() {
		return drawNumbers;
	}

	public void setDrawNumbers(List<LotofacilDrawNumber> drawNumbers) {
		this.drawNumbers = drawNumbers;
	}

	public void addDrawNumber(LotofacilDrawNumber drawNumber) {
		drawNumber.setDraw(this);
        drawNumbers.add(drawNumber);
    }

    public void removeDrawNumber(LotofacilDrawNumber drawNumber) {
    	drawNumbers.remove(drawNumber);
    }
    
    public LocalDate getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(LocalDate drawDate) {
        this.drawDate = drawDate;
    }

	public boolean isOfficial() {
		return isOfficial;
	}

	public void setOfficial(boolean isOfficial) {
		this.isOfficial = isOfficial;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LotofacilDraw other = (LotofacilDraw) obj;
		return Objects.equals(id, other.id);
	}

}
