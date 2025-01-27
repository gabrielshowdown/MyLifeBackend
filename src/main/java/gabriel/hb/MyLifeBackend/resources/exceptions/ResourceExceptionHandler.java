package gabriel.hb.MyLifeBackend.resources.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import gabriel.hb.MyLifeBackend.services.exceptions.DatabaseException;
import gabriel.hb.MyLifeBackend.services.exceptions.InvalidLoginException;
import gabriel.hb.MyLifeBackend.services.exceptions.ResourceNotFoundException;
import gabriel.hb.MyLifeBackend.services.exceptions.UserAlreadyRegisteredException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice // Intercepta as exceções que acontecem, para que esse objeto faça um possível tratamento.
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class) // Esse método 'resourceNotFound' vai interceptar qq exceção desse tipo 'ResourceNotFoundException' 
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
		String error = "Resource not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DatabaseException.class) // Esse método 'database' vai interceptar qq exceção desse tipo 'DatabaseException' 
	public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request){
		String error = "Database error";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(InvalidLoginException.class) // Esse método 'invalidLogin' vai interceptar qq exceção desse tipo 'InvalidLoginException' 
	public ResponseEntity<StandardError> invalidLogin(InvalidLoginException e, HttpServletRequest request){
		String error = "Login invalid";
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(UserAlreadyRegisteredException.class) // Esse método 'userAlreadyRegistered' vai interceptar qq exceção desse tipo 'UserAlreadyRegisteredException' 
	public ResponseEntity<StandardError> userAlreadyRegistered(UserAlreadyRegisteredException e, HttpServletRequest request){
		String error = "Username already registered";
		HttpStatus status = HttpStatus.CONFLICT;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

}
