package gabriel.hb.MyLifeBackend.services.exceptions;

public class DatabaseException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public DatabaseException(String msg) {
        super("Erro no bancon de dados " + msg);
	}

}
