package netlife.devmasters.booking.exception;


import jakarta.persistence.NoResultException;
import netlife.devmasters.booking.constant.FileConst;
import netlife.devmasters.booking.constant.MessagesConst;
import netlife.devmasters.booking.util.HttpResponse;
import netlife.devmasters.booking.exception.domain.*;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
/*
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

 */
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static netlife.devmasters.booking.constant.MessagesConst.REGISTER_ALREADY_EXIST;
import static netlife.devmasters.booking.constant.MessagesConst.RELATED_DATA;
import static org.springframework.http.HttpStatus.*;

// indicate that this class is responsible for handling exceptions in the whole rest application
//outside -> problems with HTTP such as invalid URL o missing parameter | request problem
//Automatically applies the @ResponseBody annotation to all methods
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
//occur outside of the scope of a controller method
public class ExcepcionsManagment implements ErrorController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private static final String CUENTA_BLOQUEADA = "Cuenta bloqueada - Contacte al administrador";
	private static final String METODO_NO_PERMITIDO = "Este método no está permitido en el endpoint. Enviar un request: '%s'";
	public static final String ERROR_INTERNO_SERVIDOR = "Un error inesperado se ha producido al procesar el requerimiento";
	private static final String CREDENCIALES_INCORRECTAS = "Nombre de usuario / password incorrecto";
	private static final String CUENTA_DESHABILITADA = "Cuenta deshabilitada - Contacte al administrador";
	private static final String ERROR_PROCESO_ARCHIVO = "Error al procesar el archivo";
	private static final String PERMISOS_INSUFICIENTES = "Permisos insuficientes para esta acción";
	private static final String ERROR_ENVIO_EMAIL = "Error al enviar email, verifique que la dirección ingresada sea válida.";
	private static final String ERROR_ENVIO_EMAIL_CREDENCIALES = "Error al enviar email, verifique las credenciales de envío.";
	public static final String RUTA_ERROR = "/error";
/*
	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<HttpResponse> accountDisabledException() {
		return createHttpResponse(BAD_REQUEST, CUENTA_DESHABILITADA);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<HttpResponse> badCredentialsException() {
		return createHttpResponse(BAD_REQUEST, CREDENCIALES_INCORRECTAS);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<HttpResponse> accessDeniedException() {
		return createHttpResponse(FORBIDDEN, PERMISOS_INSUFICIENTES);
	}

	@ExceptionHandler(LockedException.class)
	public ResponseEntity<HttpResponse> lockedException() {
		return createHttpResponse(UNAUTHORIZED, CUENTA_BLOQUEADA);
	}

	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception) {
		return createHttpResponse(UNAUTHORIZED, exception.getMessage());
	}

	@ExceptionHandler(EmailExisteExcepcion.class)
	public ResponseEntity<HttpResponse> emailExistException(EmailExisteExcepcion exception) {
		return createHttpResponse(BAD_REQUEST, exception.getMessage());
	}

 */
	//indicate a method that will handle a specific exception
	@ExceptionHandler(UsernameExistExcepcion.class)
	public ResponseEntity<HttpResponse> usernameExistException(UsernameExistExcepcion exception) {
		return createHttpResponse(BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(EmailNotFoundExcepcion.class)
	public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundExcepcion exception) {
		return createHttpResponse(BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception) {
		return createHttpResponse(BAD_REQUEST, exception.getMessage());
	}

//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<HttpResponse> noHandlerFoundException(NoHandlerFoundException e) {
//        return createHttpResponse(BAD_REQUEST, "There is no mapping for this URL");
//    }

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
		HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
		return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METODO_NO_PERMITIDO, supportedMethod));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
		LOGGER.error(exception.getMessage());
		exception.printStackTrace();
		return createHttpResponse(INTERNAL_SERVER_ERROR, exception.getLocalizedMessage());
	}

	@ExceptionHandler(NotFileImageExcepcion.class)
	public ResponseEntity<HttpResponse> notAnImageFileException(NotFileImageExcepcion exception) {
		LOGGER.error(exception.getMessage());
		return createHttpResponse(BAD_REQUEST, exception.getMessage());
	}
	
	//DateTimeParseException
	@ExceptionHandler(DateTimeParseException.class)
	public ResponseEntity<HttpResponse> dateTimeParseException(DateTimeParseException exception) {
		LOGGER.error(exception.getMessage());
		return createHttpResponse(BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<HttpResponse> archivoMuyGrandeException(MaxUploadSizeExceededException exception) {
		LOGGER.error(exception.getMessage() + " - MaxUploadSize = " + exception.getMaxUploadSize());

		String mensaje = FileConst.BIG_FILE;

		HttpHeaders headers = new HttpHeaders();
		headers.add("errorHeader", mensaje);

		ResponseEntity<HttpResponse> response = new ResponseEntity<HttpResponse>(
				new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
						mensaje.toUpperCase(),
						mensaje),
				headers, HttpStatus.BAD_REQUEST);

		return response;

		// return createHttpResponse(BAD_REQUEST, ARCHIVO_MUY_GRANDE);
	}

	@ExceptionHandler(NoResultException.class)
	public ResponseEntity<HttpResponse> notFoundException(NoResultException exception) {
		LOGGER.error(exception.getMessage());
		return createHttpResponse(NOT_FOUND, exception.getMessage());
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<HttpResponse> iOException(IOException exception) {
		LOGGER.error(exception.getMessage());
		return createHttpResponse(INTERNAL_SERVER_ERROR, ERROR_PROCESO_ARCHIVO);
	}

	private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
		return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus,
				httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
	}

	@RequestMapping(RUTA_ERROR)
	public ResponseEntity<HttpResponse> notFound404() {
		return createHttpResponse(NOT_FOUND, "No existe el recurso solicitado");
	}

//    @Override
//    public String getErrorPath() {
//        return ERROR_PATH;
//    }

	@ExceptionHandler(DataException.class)
	public ResponseEntity<HttpResponse> dataException(DataException exception) {
		return createHttpResponse(BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<HttpResponse> dataException(BusinessException exception) {
		return createHttpResponse(BAD_REQUEST, exception.getMessage());
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<HttpResponse> dataIntegrityViolationException(DataIntegrityViolationException exception) {
		
		Throwable cause = exception.getCause();
		
		String constraintName = ((ConstraintViolationException)exception.getCause()).getConstraintName();
		
		if (cause != null && cause instanceof ConstraintViolationException) {
			if(constraintName == null) {
				return createHttpResponse(BAD_REQUEST, RELATED_DATA);
			}
			if (constraintName.contains("_un")) {
				return createHttpResponse(BAD_REQUEST,
						REGISTER_ALREADY_EXIST);
			}
			
			if (constraintName.contains("_fk")) {
				return createHttpResponse(BAD_REQUEST, MessagesConst.RELATED_DATA);
			}
		} 
			LOGGER.error(exception.getMessage());
			exception.printStackTrace();
			return createHttpResponse(INTERNAL_SERVER_ERROR, ERROR_INTERNO_SERVIDOR);	
	}
	/*
	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<HttpResponse> messsagingException(MessagingException exception){
		LOGGER.error(exception.getMessage());
		return createHttpResponse(BAD_REQUEST, ERROR_ENVIO_EMAIL);
	}
	
	@ExceptionHandler(MailSendException.class)
	public ResponseEntity<HttpResponse> mailSendException(MailSendException exception){
		LOGGER.error(exception.getMessage());
		return createHttpResponse(BAD_REQUEST, ERROR_ENVIO_EMAIL);
	}

	@ExceptionHandler(MailAuthenticationException.class)
	public ResponseEntity<HttpResponse> mailAuthenticationException(MailAuthenticationException exception){
		LOGGER.error(exception.getMessage());
		return createHttpResponse(BAD_REQUEST, ERROR_ENVIO_EMAIL_CREDENCIALES);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<HttpResponse> messageNotReadableException(HttpMessageNotReadableException exception){
		LOGGER.error(exception.getMessage());
		return createHttpResponse(BAD_REQUEST, "Dato inválido - Revise el formato");
	}

	 */

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<HttpResponse> methodArgumentNotValidException(MethodArgumentNotValidException exception){
		LOGGER.error(exception.getMessage());
		Map<String, String> errorsArgument = new HashMap<>();
		exception.getBindingResult().getAllErrors().forEach(error -> {
			String errorMessage = error.getDefaultMessage();
			String fieldName = ((FieldError) error).getField();
			errorsArgument.put(fieldName, errorMessage);
		});

		String str = errorsArgument.entrySet().stream()
				.sorted(Map.Entry.comparingByValue())
				.map(err -> "[Atributo '" + err.getKey() + "' con error: " + err.getValue() + "]")
				.collect(Collectors.joining(""));
		return createHttpResponse(BAD_REQUEST, str);
	}
/*
	@ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
	public ResponseEntity<HttpResponse> constraintViolationException(jakarta.validation.ConstraintViolationException exception){
		LOGGER.error(exception.getMessage());
		ArrayList<String> errors = new ArrayList<>();
		exception.getConstraintViolations().forEach(error -> {
			String errorMessage = error.getMessage();
			errors.add(errorMessage);
		});

		String str = errors.stream()
				.map(err -> "[" + err + "]")
				.collect(Collectors.joining(""));
		return createHttpResponse(BAD_REQUEST, str);
	}

 */


	// org.postgresql.util.PSQLException
	@ExceptionHandler(PSQLException.class)
	public ResponseEntity<HttpResponse> psqlException(PSQLException exception){
		LOGGER.error(exception.getMessage());
		return createHttpResponse(BAD_REQUEST, exception.getMessage());
	}

	// org.springframework.orm.jpa.JpaSystemException
	@ExceptionHandler(JpaSystemException.class)
	public ResponseEntity<HttpResponse> jpaSystemException(JpaSystemException exception){

		// busca en las causas de la excepción una de tipo org.postgresql.util.PSQLException para obtener el mensaje de error
		Throwable rootCause = exception;
		while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {

			LOGGER.error(rootCause.getMessage());

			rootCause = rootCause.getCause();
			if (rootCause instanceof PSQLException) {

				// el mensaje de error de la excepción PSQLException es muy largo, se obtiene la primera línea
				String errorMessage = rootCause.getMessage();
				int index = errorMessage.indexOf("\n");
				if (index > 0) {
					errorMessage = errorMessage.substring(0, index);
				}

				return createHttpResponse(BAD_REQUEST, errorMessage );
			}
		}


		return createHttpResponse(BAD_REQUEST, exception.getMessage());
	}
	
	/*
	 * catch (DataIntegrityViolationException cve) {
	 * 
	 * LOGGER.warn("No se puede eliminar el usuario: " + username +
	 * " - Usuario tiene dependencias en módulos."); cve.printStackTrace();
	 * 
	 * throw new DataException(UsuarioImplConst.USUARIO_TIENE_DEPENDENCIAS);
	 * 
	 * }
	 */
	
	/*
	 * @ExceptionHandler(DataIntegrityViolationException.class) public
	 * ResponseEntity<HttpResponse>
	 * dataIntegrityViolationException(DataIntegrityViolationException exception){
	 * LOGGER.error(exception.getMessage()); return createHttpResponse(BAD_REQUEST,
	 * "No se puede modificar/eliminar: existen dependencias en los módulos"); }
	 */
	
}

