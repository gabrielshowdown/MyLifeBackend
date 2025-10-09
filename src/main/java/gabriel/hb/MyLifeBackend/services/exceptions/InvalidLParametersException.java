package gabriel.hb.MyLifeBackend.services.exceptions;

public class InvalidLParametersException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidLParametersException(String msg) {
        super("Parâmetros de concurso inválidos  " + msg);
	}
	
}
