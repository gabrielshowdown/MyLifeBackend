package gabriel.hb.MyLifeBackend.services.exceptions;

public class UserAlreadyRegisteredException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserAlreadyRegisteredException(String msg) {
        super("Usuário já cadastrado" + msg);
	}
	
}
