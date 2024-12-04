package gabriel.hb.MyLifeBackend.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(Object id) {
		super("Recurso nao encontrado. ID " + id);
	}
	
}
