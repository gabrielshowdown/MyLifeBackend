package gabriel.hb.MyLifeBackend.modules.auth.exceptions;

public class InvalidLoginException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidLoginException(String msg) {
        super("Usuário/Senha inválidos " + msg);
	}
	
}
