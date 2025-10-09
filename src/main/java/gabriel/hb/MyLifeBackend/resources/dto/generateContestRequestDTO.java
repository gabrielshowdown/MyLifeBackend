package gabriel.hb.MyLifeBackend.resources.dto;

public class generateContestRequestDTO {
	
	private Long concursoAnteriorId;
    private int qtdPares;
    private int qtdImpares;
    private int qtdRepetidos;
    
    public generateContestRequestDTO() {
    	
    }
    
	public generateContestRequestDTO(Long concursoAnteriorId, int qtdPares, int qtdImpares, int qtdRepetidos) {
		super();
		this.concursoAnteriorId = concursoAnteriorId;
		this.qtdPares = qtdPares;
		this.qtdImpares = qtdImpares;
		this.qtdRepetidos = qtdRepetidos;
	}

	public Long getConcursoAnteriorId() {
		return concursoAnteriorId;
	}

	public void setConcursoAnteriorId(Long concursoAnteriorId) {
		this.concursoAnteriorId = concursoAnteriorId;
	}

	public int getQtdPares() {
		return qtdPares;
	}

	public void setQtdPares(int qtdPares) {
		this.qtdPares = qtdPares;
	}

	public int getQtdImpares() {
		return qtdImpares;
	}

	public void setQtdImpares(int qtdImpares) {
		this.qtdImpares = qtdImpares;
	}

	public int getQtdRepetidos() {
		return qtdRepetidos;
	}

	public void setQtdRepetidos(int qtdRepetidos) {
		this.qtdRepetidos = qtdRepetidos;
	}

}
