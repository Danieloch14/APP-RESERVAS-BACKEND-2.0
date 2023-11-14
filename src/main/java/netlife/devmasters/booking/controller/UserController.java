//TODO: revisar

package epntech.cbdmq.pe.resource;

import static epntech.cbdmq.pe.constante.ArchivoConst.CARPETA_USUARIO;
import static epntech.cbdmq.pe.constante.ArchivoConst.FORWARD_SLASH;
import static epntech.cbdmq.pe.constante.ArchivoConst.URL_IMAGEN_TEMPORAL;
import static epntech.cbdmq.pe.constante.SeguridadConst.CABECERA_TOKEN_JWT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import epntech.cbdmq.pe.dominio.admin.Paralelo;
import epntech.cbdmq.pe.dominio.util.UsuarioDtoRead;
import epntech.cbdmq.pe.dominio.util.UsuarioInfoDto;
import epntech.cbdmq.pe.excepcion.dominio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import epntech.cbdmq.pe.dominio.HttpResponse;
import epntech.cbdmq.pe.dominio.UserPrincipal;
import epntech.cbdmq.pe.dominio.Usuario;
import epntech.cbdmq.pe.dominio.util.NombreApellido;
import epntech.cbdmq.pe.excepcion.GestorExcepciones;
import epntech.cbdmq.pe.servicio.UsuarioService;
import epntech.cbdmq.pe.util.JWTTokenProvider;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping(path = { "/", "/usuario" })
public class UsuarioResource extends GestorExcepciones {
	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	public static final String EMAIL_ENVIADO = "Se envió un email con el nuevo password a: ";
	public static final String USUARIO_ELIMINADO_EXITO = "Usuario eliminado";
	private AuthenticationManager authenticationManager;
	private UsuarioService usuarioService;
	private JWTTokenProvider jwtTokenProvider;

	@Autowired
	public UsuarioResource(AuthenticationManager authenticationManager, UsuarioService userService,
			JWTTokenProvider jwtTokenProvider) {
		this.authenticationManager = authenticationManager;
		this.usuarioService = userService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping("/login")
	public ResponseEntity<Usuario> login(@RequestBody Usuario user) {
		authenticate(user.getNombreUsuario(), user.getClave());

		Usuario loginUser = usuarioService.findUserByUsername(user.getNombreUsuario());
		UserPrincipal userPrincipal = new UserPrincipal(loginUser);
		HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
		return new ResponseEntity<>(loginUser, jwtHeader, OK);
	}

	@PostMapping("/registro")
	public ResponseEntity<Usuario> register(@RequestBody Usuario user) throws UsuarioNoEncontradoExcepcion,
			NombreUsuarioExisteExcepcion, EmailExisteExcepcion, MessagingException, IOException {
		// Usuario newUser =
		// usuarioService.registrar(user.getCodDatosPersonales().getNombre(),
		// user.getCodDatosPersonales().getApellido(), user.getNombreUsuario(),
		// user.getCodDatosPersonales().getCorreo_personal());
		Usuario newUser = usuarioService.registrar(user);
		return new ResponseEntity<>(newUser, OK);
	}

	@PostMapping("/crear")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario)
			throws NombreUsuarioExisteExcepcion, EmailExisteExcepcion, IOException, UsuarioNoEncontradoExcepcion,
			MessagingException {
		Usuario nuevoUsuario = usuarioService.crear(usuario);
		return new ResponseEntity<>(nuevoUsuario, OK);
	}

//	@PostMapping("/actualizar")
//	public ResponseEntity<Usuario> update(@RequestParam("currentUsername") String currentUsername,
//			@RequestParam("firstName") String firstName,
//			@RequestParam("lastName") String lastName,
//			@RequestParam("username") String username,
//			@RequestParam("email") String email,
//			@RequestParam("role") String role,
//			@RequestParam("isActive") String isActive,
//			@RequestParam("isNonLocked") String isNonLocked,
//			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
//			throws UsuarioNoEncontradoExcepcion, NombreUsuarioExisteExcepcion, EmailExisteExcepcion, IOException,
//			NoEsArchivoImagenExcepcion {
//		Usuario updatedUser = usuarioService.actualizarUsuario(currentUsername, firstName, lastName, username, email,
//				role, Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
//		return new ResponseEntity<>(updatedUser, OK);
//	}
	@PutMapping("activeLock/{id}")
	public ResponseEntity<Usuario> actualizarDatos(@PathVariable("id") Long codigo,
			@RequestParam(name = "isActive", required = false) Boolean active,
			@RequestParam(name = "isNotLocked", required = false) Boolean isNotLocked) throws DataException {
		return usuarioService.getById(codigo).map(datosGuardados -> {
			// datosGuardados.setCodParalelo(obj.getCodParalelo());
			Optional.ofNullable(isNotLocked).ifPresent(datosGuardados::setNotLocked);
			Optional.ofNullable(active).ifPresent(datosGuardados::setActive);
			Usuario datosActualizados = null;

			try {
				datosActualizados = usuarioService.actualizarUsuario(datosGuardados);
			} catch (UsuarioNoEncontradoExcepcion | NombreUsuarioExisteExcepcion | EmailExisteExcepcion | IOException
					| NoEsArchivoImagenExcepcion e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ResponseEntity<>(datosActualizados, HttpStatus.OK);
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/actualizarActive")
	public ResponseEntity<Void> updateActive(@RequestParam("valide") Boolean valide,
			@RequestParam("username") String username)
			throws UsuarioNoEncontradoExcepcion, NombreUsuarioExisteExcepcion, EmailExisteExcepcion, IOException,
			NoEsArchivoImagenExcepcion {
		int registrosActualizados = usuarioService.actualizarActive(valide, username);
		if (registrosActualizados == 1) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/actualizarNotLocked")
	public ResponseEntity<Void> updateNotLocked(@RequestParam("valide") Boolean notLocked,
			@RequestParam("username") String username)
			throws UsuarioNoEncontradoExcepcion, NombreUsuarioExisteExcepcion, EmailExisteExcepcion, IOException,
			NoEsArchivoImagenExcepcion {
		int registrosActualizados = usuarioService.actualizarNotLock(notLocked, username);
		if (registrosActualizados == 1) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/actualizar")
	public ResponseEntity<Usuario> update(@RequestBody Usuario usuario)
			throws UsuarioNoEncontradoExcepcion, NombreUsuarioExisteExcepcion, EmailExisteExcepcion, IOException,
			NoEsArchivoImagenExcepcion {
		Usuario updatedUser = usuarioService.actualizarUsuario(usuario);
		return new ResponseEntity<>(updatedUser, OK);
	}

	@GetMapping("/buscar/{nombreusuario}")
	public ResponseEntity<Usuario> getUser(@PathVariable("nombreusuario") String username) {
		Usuario user = usuarioService.findUserByUsername(username);
		return new ResponseEntity<>(user, OK);
	}

	@PostMapping("/buscarNombreApellido")
	public ResponseEntity<List<Usuario>> getUserNombreApellido(@RequestBody NombreApellido nombreApellido) {
		List<Usuario> users = usuarioService.findUsuariosByNombreApellido(nombreApellido.getNombre(),
				nombreApellido.getApellido());
		return new ResponseEntity<>(users, OK);
	}

	// por query params
	@PostMapping("/buscarNombresApellidos")
	public ResponseEntity<List<Usuario>> buscarUsuarios(
			@RequestParam(name = "nombres", required = false) String nombre,
			@RequestParam(name = "apellidos", required = false) String apellido) {

		List<Usuario> usuarios = new ArrayList<>();

		if (nombre != null && !nombre.trim().isEmpty() && apellido != null && !apellido.trim().isEmpty()) {
			usuarios = usuarioService.findUsuariosByNombreApellido(nombre, apellido);
		} else if (nombre != null && !nombre.trim().isEmpty()) {
			usuarios = usuarioService.findUsuariosByNombre(nombre);
		} else if (apellido != null && !apellido.trim().isEmpty()) {
			usuarios = usuarioService.findUsuariosByApellido(apellido);
		}
		return ResponseEntity.ok(usuarios);
	}

	@PostMapping("/buscarCorreo")
	public ResponseEntity<List<Usuario>> getCorreo(@RequestParam String correo) {
		List<Usuario> users = usuarioService.findUsuariosByCorreo(correo);
		return new ResponseEntity<>(users, OK);
	}

	@PostMapping("/buscarUsuario")
	public ResponseEntity<Usuario> getUserII(@RequestParam String usuario) {
		Usuario users = usuarioService.findUserByUsername(usuario);
		return new ResponseEntity<>(users, OK);
	}

	@GetMapping("/lista")
	public ResponseEntity<List<Usuario>> getAllUsers() {
		List<Usuario> users = usuarioService.getUsuarios();
		return new ResponseEntity<>(users, OK);
	}

	@GetMapping("/listaPaginado")
	public List<Usuario> getAllUsersPageable(Pageable pageable) {

		return usuarioService.getUsuariosPageable(pageable);

	}

	@GetMapping("/listaRPaginado")
	public List<UsuarioDtoRead> getAllUsersPer(Pageable pageable) {

		return usuarioService.getUsuariosPer(pageable);

	}

	@PostMapping("/resetPassword/{nombreUsuario}")
	public ResponseEntity<HttpResponse> resetPassword(@PathVariable("nombreUsuario") String nombreUsuario)
			throws MessagingException, EmailNoEncontradoExcepcion, UsuarioNoEncontradoExcepcion, IOException {
		usuarioService.resetPassword(nombreUsuario);
		return response(OK, EMAIL_ENVIADO + " la dirección de email registrada para el usuario " + nombreUsuario);
	}

	@DeleteMapping("/eliminar/{username}")
	// @PreAuthorize("hasAnyAuthority('user:delete')")
	public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username) throws Exception {
		usuarioService.eliminarUsuario(username);
		return response(OK, USUARIO_ELIMINADO_EXITO);
	}

	@PostMapping("/actualizarImagenPerfil")
	public ResponseEntity<Usuario> updateProfileImage(@RequestParam("username") String username,
			@RequestParam(value = "profileImage") MultipartFile profileImage) throws UsuarioNoEncontradoExcepcion,
			NombreUsuarioExisteExcepcion, EmailExisteExcepcion, IOException, NoEsArchivoImagenExcepcion {
		Usuario user = usuarioService.actualizarImagenPerfil(username, profileImage);
		return new ResponseEntity<>(user, OK);
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
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}

	@PostMapping("/guardarArchivo")
	public ResponseEntity<HttpResponse> guardarArchivo(@RequestParam(value = "nombreArchivo") String nombreArchivo,
			@RequestParam(value = "archivo") MultipartFile archivo) throws Exception {

		try {
			usuarioService.guardarArchivo(nombreArchivo, archivo);
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
			return usuarioService.tamañoMáximoArchivo();
		} catch (Exception e) {

			this.LOGGER.error(e.getMessage());
			return -1;
		}
	}


}
