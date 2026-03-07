package gabriel.hb.MyLifeBackend.services.exceptions;

public class InvalidLParametersDrawException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidLParametersDrawException(String msg) {
        super("Parâmetros de concurso inválidos " + msg);
	}
	
}
