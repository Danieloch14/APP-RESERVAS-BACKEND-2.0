
package netlife.devmasters.booking.service.Impl;


import jakarta.mail.MessagingException;
import netlife.devmasters.booking.domain.DatoPersonal;
import netlife.devmasters.booking.domain.User;
import netlife.devmasters.booking.domain.UserPrincipal;
import netlife.devmasters.booking.exception.dominio.*;
import netlife.devmasters.booking.repository.UserRepository;
import netlife.devmasters.booking.service.IntentoLoginService;
import netlife.devmasters.booking.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static netlife.devmasters.booking.constant.ArchivoConst.ARCHIVO_MUY_GRANDE;
import static netlife.devmasters.booking.constant.UsuarioImplConst.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.MediaType.*;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UsuarioServiceImpl implements UserService, UserDetailsService {
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private IntentoLoginService loginAttemptService;
	/*
	private EmailService emailService;
	@Value("${pecb.archivos.ruta}")
	private String ARCHIVOS_RUTA;

	@Value("${spring.servlet.multipart.max-file-size}")
	public DataSize TAMAÑO_MÁXIMO;


	 */
	@Autowired
	public UsuarioServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                              IntentoLoginService loginAttemptService
			/*, EmailService emailService, EstudianteService estudianteService,
                              InstructorService instructorService
			 */

	) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.loginAttemptService = loginAttemptService;
		/*
		this.emailService = emailService;
		this.estudianteService = estudianteService;
		this.instructorService = instructorService;
		 */
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findUserByUsername(username);
		if (user == null) {
			LOGGER.error(NO_EXISTE_USUARIO + username);
			throw new UsernameNotFoundException(NO_EXISTE_USUARIO + username);
		} else {
			validateLoginAttempt(user);
			user.setDateLastLogin(user.getDateLastLogin());
			user.setDateLastLogin(new Date());
			userRepository.save(user);
			UserPrincipal userPrincipal = new UserPrincipal(user);
			LOGGER.info(NOMBRE_USUARIO_ENCONTRADO + username);
			return userPrincipal;
		}

	}

	@Override
	// public Usuario registrar(String firstName, String lastName, String username,
	// String email) throws UsuarioNoEncontradoExcepcion,
	// NombreUsuarioExisteExcepcion, EmailExisteExcepcion, MessagingException {
	public User registrar(User usuario) throws UsuarioNoEncontradoExcepcion, NombreUsuarioExisteExcepcion,
			EmailExisteExcepcion, MessagingException, IOException {
		validateNewUsernameAndEmail(EMPTY, usuario.getUsername(),
				usuario.getCodDatosPersonales().getEmail());

		// datos de usuario
		User user = new User();
		// user.setUserId(generateUserId());
		String password = generatePassword();
		// user.setNombres(firstName);
		// user.setApellidos(lastName);
		user.setUsername(usuario.getUsername());
		// user.setEmail(email);
		user.setDateEntry(new Date());
		user.setPassword(encodePassword(password));
		user.setActive(true);
		user.setNotLocked(true);
		// user.setUrlImagenPerfil(getTemporaryProfileImageUrl(username));

		// datos personales
		DatoPersonal datos = new DatoPersonal();
		datos.setName(usuario.getCodDatosPersonales().getName());
		datos.setLastname(usuario.getCodDatosPersonales().getLastname());
		datos.setEmail(usuario.getCodDatosPersonales().getEmail());

		// asocia datos personales con usuario
		user.setCodDatosPersonales(datos);

		userRepository.save(user);

		userRepository.flush();
		/*
		emailService.sendNewPasswordEmail(usuario.getCodDatosPersonales().getNombre(), password,
				usuario.getCodDatosPersonales().getCorreoPersonal());
		 */
		return user;
	}

	@Override
	public Optional<User> getById(Long codigo) {

		// TODO Auto-generated method stub
		return userRepository.findById(codigo);

	}


	@Override
	public User actualizarUsuario(User usuario) throws UsuarioNoEncontradoExcepcion, NombreUsuarioExisteExcepcion,
			EmailExisteExcepcion, IOException, NoEsArchivoImagenExcepcion {
		User currentUser = validateNewUsernameAndEmail(usuario.getUsername(), usuario.getUsername(),
				usuario.getCodDatosPersonales().getEmail());

		currentUser.setActive(usuario.isActive());
		currentUser.setNotLocked(usuario.isNotLocked());

		// TODO: validar campos actualizables

		// currentUser.setNombres(newFirstName);
		// currentUser.setApellidos(newLastName);
//		currentUser.setNombreUsuario(usuario);
		// currentUser.setEmail(newEmail);
//		currentUser.setActive(isActive);
//		currentUser.setNotLocked(isNonLocked);
		userRepository.save(currentUser);
		// saveProfileImage(currentUser, profileImage);

		// TODO: eliminar log
		// LOGGER.info("Actualizar usuario ejecutado");

		return currentUser;
	}

	@Override
	public int actualizarActive(Boolean active, String username) throws UsuarioNoEncontradoExcepcion,
			NombreUsuarioExisteExcepcion, EmailExisteExcepcion, IOException, NoEsArchivoImagenExcepcion {
		return userRepository.actualizarIsActive(active, username);
	}

	@Override
	public int actualizarNotLock(Boolean notLock, String username) throws UsuarioNoEncontradoExcepcion,
			NombreUsuarioExisteExcepcion, EmailExisteExcepcion, IOException, NoEsArchivoImagenExcepcion {
		return userRepository.actualizarNotLocked(notLock, username);
	}

	@Override
	public void resetPassword(String nombreUsuario) throws MessagingException, UsuarioNoEncontradoExcepcion, IOException {

		User usuario = userRepository.findUserByUsername(nombreUsuario);
		if (usuario == null) {
			throw new UsuarioNoEncontradoExcepcion(NO_EXISTE_USUARIO + nombreUsuario);
		}

		String password = generatePassword();
		usuario.setPassword(encodePassword(password));
		userRepository.save(usuario);

		// datos personales
		DatoPersonal datos = usuario.getCodDatosPersonales();

		// asocia datos personales con usuario
		usuario.setCodDatosPersonales(datos);

		userRepository.save(usuario);

		userRepository.flush();
		/*

		emailService.sendNewPasswordEmail(usuario.getCodDatosPersonales().getNombre(), password,
				usuario.getCodDatosPersonales().getCorreoPersonal());


		 */
	}


	@Override
	public List<User> getUsuarios() {
		return userRepository.findAll();
	}

	@Override
	public List<User> getUsuariosPageable(Pageable pageable) {

		return userRepository.findAllPageable(pageable);

	}
	@Override
	public User findUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}

	@Override
	public User findUserByEmail(String email) {

		List<User> lista = userRepository.findUsuariosByCorreo(email);

		if (lista != null && !lista.isEmpty()) {
			return lista.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void eliminarUsuario(String username) throws Exception {
		User user = userRepository.findUserByUsername(username);
		/*
		 * try { Path userFolder = Paths.get(CARPETA_USUARIO +
		 * user.getNombreUsuario()).toAbsolutePath().normalize();
		 * FileUtils.deleteDirectory(new File(userFolder.toString())); } catch
		 * (IOException e1) { LOGGER.
		 * error("Se ha producido un error al eliminar los archivos del usuario: " +
		 * user.getNombreUsuario()); e1.printStackTrace(); }
		 */

		userRepository.deleteById(user.getIdUser());

	}
	private String encodePassword(String password) {

		String encodedPassword = passwordEncoder.encode(password);
		return encodedPassword;
	}

	private String generatePassword() {
		return RandomStringUtils.randomAlphanumeric(20);
	}

	private String generateUserId() {
		return RandomStringUtils.randomNumeric(10);
	}

	private void validateLoginAttempt(User user) {
		if (user.isNotLocked()) {
			if (loginAttemptService.excedeMaximoIntentos(user.getUsername())) {
				user.setNotLocked(false);
			} else {
				user.setNotLocked(true);
			}
		} else {
			loginAttemptService.retirarUsuarioDeCache(user.getUsername());
		}
	}

	private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail)
			throws UsuarioNoEncontradoExcepcion, NombreUsuarioExisteExcepcion, EmailExisteExcepcion {
		User userByNewUsername = findUserByUsername(newUsername);
		User userByNewEmail = findUserByEmail(newEmail);

		// si valida un usuario registrado
		if (StringUtils.isNotBlank(currentUsername)) {
			User currentUser = findUserByUsername(currentUsername);

			// si no encuentra datos para el usuario registrado
			if (currentUser == null) {
				throw new UsuarioNoEncontradoExcepcion(NO_EXISTE_USUARIO + currentUsername);
			}

			// sale si ya existe ese nombre de usuario para otro usuario registrado
			if (userByNewUsername != null && !currentUser.getIdUser().equals(userByNewUsername.getIdUser())) {
				throw new NombreUsuarioExisteExcepcion(NOMBRE_USUARIO_YA_EXISTE);
			}

			// TODO: confirmar si es requerida esta validación
			// sale si ya existe ese email para un usuario registrado
			if (currentUser.getCodDatosPersonales().getEmail().compareToIgnoreCase(newEmail) != 0) {
				if (userByNewEmail != null /* && !currentUser.getCodUsuario().equals(userByNewEmail.getCodUsuario()) */) {
					//throw new EmailExisteExcepcion(EMAIL_YA_EXISTE);
					LOGGER.info(EMAIL_YA_EXISTE + " " + newEmail + " " + currentUser.getCodDatosPersonales().getEmail());
				}
			}

			return currentUser;
		}
		// cuando no es un usuario registrado
		else {
			// sale si ya existe ese nombre de usuario
			if (userByNewUsername != null) {
				throw new NombreUsuarioExisteExcepcion(NOMBRE_USUARIO_YA_EXISTE);
			}

			// TODO: confirmar si es requerida esta validación
			// sale si ya existe ese email para un usuario
			if (userByNewEmail != null) {
				//throw new EmailExisteExcepcion(EMAIL_YA_EXISTE);
				LOGGER.info(EMAIL_YA_EXISTE + " " + newEmail + " " + userByNewEmail.getCodDatosPersonales().getEmail());
			}

			return null;
		}
	}

	public void guardarArchivo(String nombreArchivo, MultipartFile archivo)
			throws IOException, ArchivoMuyGrandeExcepcion {
		/*

		if (archivo.getSize() > TAMAÑO_MÁXIMO.toBytes()) {
			throw new ArchivoMuyGrandeExcepcion(ARCHIVO_MUY_GRANDE);
		}

		Path ruta = Paths.get(ARCHIVOS_RUTA).toAbsolutePath().normalize();
		if (!Files.exists(ruta)) {
			Files.createDirectories(ruta);
		}

		Files.copy(archivo.getInputStream(), ruta.resolve(nombreArchivo), StandardCopyOption.REPLACE_EXISTING);
		LOGGER.info("Archivo guardado: " + ARCHIVOS_RUTA + nombreArchivo);

		 */
	}

	public long tamañoMáximoArchivo() {
		return 1l;
				//TAMAÑO_MÁXIMO.toBytes();
	}

	public List<User> findUsuariosByNombreApellido(String nombre, String apellido) {
		return this.userRepository.findUsuariosByNombreApellido(nombre, apellido);
	}

	@Override
	public List<User> findUsuariosByApellido(String apellido) {
		return this.userRepository.findUsuariosByApellido(apellido);
	}

	@Override
	public List<User> findUsuariosByNombre(String nombre) {
		return this.userRepository.findUsuariosByNombre(nombre);
	}

	@Override
	public List<User> findUsuariosByCorreo(String correo) {
		return this.userRepository.findUsuariosByCorreo(correo);
	}



	@Override
	public Optional<User> getUsuarioByCodDatoPersonal(Integer codDatoPersonal) {
		return userRepository.findByCodDatosPersonales_idPersonalData(codDatoPersonal);
	}

}