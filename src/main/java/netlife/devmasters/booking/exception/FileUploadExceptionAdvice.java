package netlife.devmasters.booking.exception;

import netlife.devmasters.booking.util.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
//occur inside a controller method. That methos are used to generate a ResponseEntity.
// inside ->  invalid argument or a failure to access a database| logical problem
//When extends I could overwrite
public class FileUploadExceptionAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<HttpResponse> handleMaxSizeException(MaxUploadSizeExceededException exc) {
    return response(HttpStatus.EXPECTATION_FAILED, "File too large!");
  }
  
  private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
      return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
              message), httpStatus);
  }
}