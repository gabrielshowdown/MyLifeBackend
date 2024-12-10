package gabriel.hb.MyLifeBackend.services.exceptions;

public class InvalidLoginException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidLoginException(String msg) {
        super("Usuário/Senha inválidos " + msg);
	}
	
}
