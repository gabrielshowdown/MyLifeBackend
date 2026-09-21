package gabriel.hb.MyLifeBackend.shared;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import gabriel.hb.MyLifeBackend.modules.auth.exceptions.InvalidLoginException;
import gabriel.hb.MyLifeBackend.modules.auth.exceptions.UserAlreadyRegisteredException;
import gabriel.hb.MyLifeBackend.modules.community.exceptions.BookBibleAlreadyRegisteredException;
import gabriel.hb.MyLifeBackend.modules.lotteries.exceptions.InvalidParametersDrawException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice /* Intercepta as exceções que acontecem, para que as classes mencionadas façam um possível tratamento. */
public class ResourceExceptionHandler {
	
	/* Esse método 'resourceNotFound' vai interceptar qualquer exceção desse tipo 'ResourceNotFoundException' */
	@ExceptionHandler(ResourceNotFoundException.class)  
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
		String error = "Resource not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	/* Esse método 'database' vai interceptar qualquer exceção desse tipo 'DatabaseException' */
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request){
		String error = "Database error";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	/* Esse método 'invalidLogin' vai interceptar qualquer exceção desse tipo 'InvalidLoginException' */
	@ExceptionHandler(InvalidLoginException.class)
	public ResponseEntity<StandardError> invalidLogin(InvalidLoginException e, HttpServletRequest request){
		String error = "Login invalid";
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	/* Esse método 'userAlreadyRegistered' vai interceptar qualquer exceção desse tipo 'UserAlreadyRegisteredException' */
	@ExceptionHandler(UserAlreadyRegisteredException.class) 
	public ResponseEntity<StandardError> userAlreadyRegistered(UserAlreadyRegisteredException e, HttpServletRequest request){
		String error = "Username already registered";
		HttpStatus status = HttpStatus.CONFLICT;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	/* Esse método 'BookBibleAlreadyRegistered' vai interceptar qualquer exceção desse tipo 'BookBibleAlreadyRegisteredException' */
	@ExceptionHandler(BookBibleAlreadyRegisteredException.class) 
	public ResponseEntity<StandardError> bookBibleAlreadyRegistered(BookBibleAlreadyRegisteredException e, HttpServletRequest request){
		String error = "Book already registered";
		HttpStatus status = HttpStatus.CONFLICT;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	/* Esse método 'parametersInvalid' vai interceptar qualquer exceção desse tipo 'InvalidLParametersException' */
	@ExceptionHandler(InvalidParametersException.class)
	public ResponseEntity<StandardError> parametersInvalid(InvalidParametersException e, HttpServletRequest request){
		String error = "Draw Parameters Invalid";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	/* Esse método 'parametersDrawInvalid' vai interceptar qualquer exceção desse tipo 'InvalidParametersDrawException' */
	@ExceptionHandler(InvalidParametersDrawException.class)
	public ResponseEntity<StandardError> parametersDrawInvalid(InvalidParametersDrawException e, HttpServletRequest request){
		String error = "Draw Parameters Invalid";
		HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

}
