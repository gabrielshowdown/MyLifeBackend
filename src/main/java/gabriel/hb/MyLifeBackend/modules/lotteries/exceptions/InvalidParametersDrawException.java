package gabriel.hb.MyLifeBackend.modules.lotteries.exceptions;

public class InvalidParametersDrawException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidParametersDrawException(String msg) {
        super("Parâmetros de concurso inválidos " + msg);
	}
	
}
