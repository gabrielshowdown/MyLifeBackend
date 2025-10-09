package gabriel.hb.MyLifeBackend.services.exceptions;

public class InvalidLParametersContestException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidLParametersContestException(String msg) {
        super("Parâmetros inválidos " + msg);
	}
	
}
