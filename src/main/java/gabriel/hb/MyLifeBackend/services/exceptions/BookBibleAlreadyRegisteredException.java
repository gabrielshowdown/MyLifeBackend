package gabriel.hb.MyLifeBackend.services.exceptions;

public class BookBibleAlreadyRegisteredException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BookBibleAlreadyRegisteredException(String msg) {
        super("Livro Bíblico já cadastrado" + msg);
	}
	
}
