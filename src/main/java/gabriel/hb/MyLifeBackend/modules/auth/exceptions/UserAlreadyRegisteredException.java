package gabriel.hb.MyLifeBackend.modules.auth.exceptions;

public class UserAlreadyRegisteredException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserAlreadyRegisteredException(String msg) {
        super("Usuário já cadastrado " + msg);
	}
	
}
