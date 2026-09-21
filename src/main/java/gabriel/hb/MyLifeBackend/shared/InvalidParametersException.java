package gabriel.hb.MyLifeBackend.shared;

public class InvalidParametersException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidParametersException(String msg) {
        super("Parâmetros inválidos  " + msg);
	}
	
}
