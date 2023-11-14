//TODO: revisar

package netlife.devmasters.booking.controller;

import jakarta.mail.MessagingException;
import netlife.devmasters.booking.domain.HttpResponse;
import netlife.devmasters.booking.domain.User;
import netlife.devmasters.booking.domain.UserPrincipal;
import netlife.devmasters.booking.exception.GestorExcepciones;
import netlife.devmasters.booking.exception.dominio.*;
import netlife.devmasters.booking.service.UserService;
import netlife.devmasters.booking.util.JWTTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static netlife.devmasters.booking.constant.ArchivoConst.*;
import static netlife.devmasters.booking.constant.SeguridadConst.CABECERA_TOKEN_JWT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping( "/api/user" )
public class UserController extends GestorExcepciones {
	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	public static final String EMAIL_ENVIADO = "Se envió un email con el nuevo password a: ";
	public static final String USUARIO_ELIMINADO_EXITO = "Usuario eliminado";
	private AuthenticationManager authenticationManager;
	private UserService service;
	private JWTTokenProvider jwtTokenProvider;

	@Autowired
	public UserController(
			//AuthenticationManager authenticationManager,
			UserService userService,
                          JWTTokenProvider jwtTokenProvider) {
		this.authenticationManager = authenticationManager;
		this.service = userService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user) {
	//authenticate(user.getUsername(), user.getPassword());

		User loginUser = service.findUserByUsername(user.getUsername());
		UserPrincipal userPrincipal = new UserPrincipal(loginUser);
		HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
		return new ResponseEntity<>(loginUser, jwtHeader, OK);
	}

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException,
			UsernameExistExcepcion, EmailExistExcepcion, MessagingException, IOException {
		User newUser = service.register(user);
		return new ResponseEntity<>(newUser, OK);
	}
	@PutMapping("activeLock/{id}")
	public ResponseEntity<User> actualizarDatos(@PathVariable("id") Integer codigo,
			@RequestParam(name = "isActive", required = false) Boolean active,
			@RequestParam(name = "isNotLocked", required = false) Boolean isNotLocked) throws DataException {
		return service.getById(codigo).map(datosGuardados -> {
			// datosGuardados.setCodParalelo(obj.getCodParalelo());
			Optional.ofNullable(isNotLocked).ifPresent(datosGuardados::setNotLocked);
			Optional.ofNullable(active).ifPresent(datosGuardados::setActive);
			User datosActualizados = null;

			try {
				datosActualizados = service.actualizarUsuario(datosGuardados);
			} catch (UserNotFoundException | UsernameExistExcepcion | EmailExistExcepcion | IOException
					 | NoEsArchivoImagenExcepcion e) {
				e.printStackTrace();
			}
			return new ResponseEntity<>(datosActualizados, HttpStatus.OK);
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/actualizarActive")
	public ResponseEntity<Void> updateActive(@RequestParam("valide") Boolean valide,
			@RequestParam("username") String username)
			throws UserNotFoundException, UsernameExistExcepcion, EmailExistExcepcion, IOException,
			NoEsArchivoImagenExcepcion {
		int registrosActualizados = service.actualizarActive(valide, username);
		if (registrosActualizados == 1) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/actualizarNotLocked")
	public ResponseEntity<Void> updateNotLocked(@RequestParam("valide") Boolean notLocked,
			@RequestParam("username") String username)
			throws UserNotFoundException, UsernameExistExcepcion, EmailExistExcepcion, IOException,
			NoEsArchivoImagenExcepcion {
		int registrosActualizados = service.actualizarNotLock(notLocked, username);
		if (registrosActualizados == 1) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/actualizar")
	public ResponseEntity<User> update(@RequestBody User usuario)
			throws UserNotFoundException, UsernameExistExcepcion, EmailExistExcepcion, IOException,
			NoEsArchivoImagenExcepcion {
		User updatedUser = service.actualizarUsuario(usuario);
		return new ResponseEntity<>(updatedUser, OK);
	}

	@GetMapping("/buscar/{nombreusuario}")
	public ResponseEntity<User> getUser(@PathVariable("nombreusuario") String username) {
		User user = service.findUserByUsername(username);
		return new ResponseEntity<>(user, OK);
	}


	// por query params
	@PostMapping("/buscarNombresApellidos")
	public ResponseEntity<List<User>> buscarUsuarios(
			@RequestParam(name = "nombres", required = false) String nombre,
			@RequestParam(name = "apellidos", required = false) String apellido) {

		List<User> usuarios = new ArrayList<>();

		if (nombre != null && !nombre.trim().isEmpty() && apellido != null && !apellido.trim().isEmpty()) {
			usuarios = service.findUsuariosByNombreApellido(nombre, apellido);
		} else if (nombre != null && !nombre.trim().isEmpty()) {
			usuarios = service.findUsuariosByNombre(nombre);
		} else if (apellido != null && !apellido.trim().isEmpty()) {
			usuarios = service.findUsuariosByApellido(apellido);
		}
		return ResponseEntity.ok(usuarios);
	}

	@PostMapping("/buscarCorreo")
	public ResponseEntity<List<User>> getCorreo(@RequestParam String correo) {
		List<User> users = service.findUsuariosByCorreo(correo);
		return new ResponseEntity<>(users, OK);
	}

	@PostMapping("/buscarUsuario")
	public ResponseEntity<User> getUserII(@RequestParam String usuario) {
		User users = service.findUserByUsername(usuario);
		return new ResponseEntity<>(users, OK);
	}

	@GetMapping("/lista")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = service.getUsers();
		return new ResponseEntity<>(users, OK);
	}

	@GetMapping("/listaPaginado")
	public List<User> getAllUsersPageable(Pageable pageable) {

		return service.getUsuariosPageable(pageable);

	}

	@PostMapping("/resetPassword/{nombreUsuario}")
	public ResponseEntity<HttpResponse> resetPassword(@PathVariable("nombreUsuario") String nombreUsuario)
			throws MessagingException, EmailNoEncontradoExcepcion, UserNotFoundException, IOException {
		service.resetPassword(nombreUsuario);
		return response(OK, EMAIL_ENVIADO + " la dirección de email registrada para el usuario " + nombreUsuario);
	}

	@DeleteMapping("/eliminar/{username}")
	// @PreAuthorize("hasAnyAuthority('user:delete')")
	public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username) throws Exception {
		service.eliminarUsuario(username);
		return response(OK, USUARIO_ELIMINADO_EXITO);
	}

	@GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
	public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("fileName") String fileName)
			throws IOException {
		return Files.readAllBytes(Paths.get(CARPETA_USUARIO + username + FORWARD_SLASH + fileName));
	}

	@GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
	public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
		URL url = new URL(URL_IMAGEN_TEMPORAL + username);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try (InputStream inputStream = url.openStream()) {
			int bytesRead;
			byte[] chunk = new byte[1024];
			while ((bytesRead = inputStream.read(chunk)) > 0) {
				byteArrayOutputStream.write(chunk, 0, bytesRead);
			}
		}
		return byteArrayOutputStream.toByteArray();
	}

	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
		return new ResponseEntity<>(
				new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
						message),
				httpStatus);
	}

	private HttpHeaders getJwtHeader(UserPrincipal user) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(CABECERA_TOKEN_JWT, jwtTokenProvider.generateJwtToken(user));
		return headers;
	}

	private void authenticate(String username, String password) {
		//authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}

	@PostMapping("/guardarArchivo")
	public ResponseEntity<HttpResponse> guardarArchivo(@RequestParam(value = "nombreArchivo") String nombreArchivo,
													   @RequestParam(value = "archivo") MultipartFile archivo) throws Exception {

		try {
			service.guardarArchivo(nombreArchivo, archivo);
			return response(HttpStatus.OK, "Archivo cargado con éxito");
		} catch (Exception e) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("errorHeader", e.getMessage());

			ResponseEntity<HttpResponse> response = new ResponseEntity<HttpResponse>(
					new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
							e.getMessage().toUpperCase(),
							e.getMessage()),
					headers, HttpStatus.BAD_REQUEST);

			return response;
		}
	}

	@GetMapping("/maxArchivo")
	public long tamañoMáximoArchivo() {

		try {
			return service.tamañoMáximoArchivo();
		} catch (Exception e) {

			this.LOGGER.error(e.getMessage());
			return -1;
		}
	}


}
