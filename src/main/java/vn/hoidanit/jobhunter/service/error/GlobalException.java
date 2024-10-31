package vn.hoidanit.jobhunter.service.error;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import vn.hoidanit.jobhunter.domain.RestResponce;

@RestControllerAdvice
public class GlobalException {
  @ExceptionHandler(
      value = {
        IdInvalidException.class,
        EntityNotFoundException.class,
        UsernameNotFoundException.class,
        BadCredentialsException.class
      })
  public ResponseEntity<RestResponce<Object>> handleException(Exception ex) {
    RestResponce<Object> res = new RestResponce<Object>();
    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    res.setError(ex.getMessage());
    res.setMessage("Exception occurs...");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }

  @ExceptionHandler(
      value = {
        EmailExistedException.class,
      })
  public ResponseEntity<RestResponce<Object>> handleEmalExistedException(Exception ex) {
    RestResponce<Object> res = new RestResponce<Object>();
    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    res.setError(ex.getMessage());
    res.setMessage("Email already exist");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }

  // public ResponseEntity<RestResponce<Object>> EmalExistedException(Exception
  // ex) {
  // RestResponce<Object> res = new RestResponce<Object>();
  // res.setStatusCode(HttpStatus.BAD_REQUEST.value());
  // res.setError(ex.getMessage());
  // res.setMessage("Exception occurs...");
  // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  // }
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<RestResponce<Object>> handleNotFoundException(Exception ex) {
    RestResponce<Object> res = new RestResponce<Object>();
    res.setStatusCode(HttpStatus.NOT_FOUND.value());
    res.setError(ex.getMessage());
    res.setMessage("404 Not Found. URL may not exist...");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<RestResponce<Object>> validationError(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    final List<FieldError> fieldErrors = result.getFieldErrors();

    RestResponce<Object> res = new RestResponce<Object>();
    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    res.setError(ex.getBody().getDetail());

    List<String> errors =
        fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
    res.setMessage(errors.size() > 1 ? errors : errors.get(0));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }

  @ExceptionHandler(
      value = {
        StorageException.class,
      })
  public ResponseEntity<RestResponce<Object>> handleUploadFileException(Exception ex) {
    RestResponce<Object> res = new RestResponce<Object>();
    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    res.setError(ex.getMessage());
    res.setMessage("File Exception");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }
}
