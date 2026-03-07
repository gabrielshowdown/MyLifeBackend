package gabriel.hb.MyLifeBackend.entities;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_numero_concurso_lotofacil")
public class LotofacilDrawNumber implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// Atributos
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name = "numero")
    private int number;
	@Column(name = "repetido")
    private boolean isRepeated;

    @ManyToOne
    @JoinColumn(name = "concurso_id") // iria criar a coluna como draw_id automaticamente
    private LotofacilDraw draw;
	
	// Construtores
    public LotofacilDrawNumber() {
    }

    public LotofacilDrawNumber(int number, boolean isRepeated, LotofacilDraw draw) {
        this.number = number;
        this.isRepeated = isRepeated;
        this.draw = draw;
    }

	// Métodos Acessores
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    public boolean getIsRepeated() {
        return isRepeated;
    }

    public void setIsRepeated(boolean isRepeated) {
        this.isRepeated = isRepeated;
    }

    // o Jackson usa os métodos getters para serializar
    @JsonIgnore
    public LotofacilDraw getDraw() {
        return draw;
    }

    public void setDraw(LotofacilDraw draw) {
        this.draw = draw;
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LotofacilDrawNumber other = (LotofacilDrawNumber) obj;
		return Objects.equals(id, other.id);
	}

}
