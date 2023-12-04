
package netlife.devmasters.booking.service.Impl;


import jakarta.mail.MessagingException;
import netlife.devmasters.booking.domain.PersonalData;
import netlife.devmasters.booking.domain.User;
import netlife.devmasters.booking.domain.UserPrincipal;
import netlife.devmasters.booking.exception.domain.*;
import netlife.devmasters.booking.repository.UserRepository;
import netlife.devmasters.booking.service.EmailService;
import netlife.devmasters.booking.service.LoginTryService;
import netlife.devmasters.booking.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static netlife.devmasters.booking.constant.UsersImplConst.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UsuarioServiceImpl implements UserService, UserDetailsService {
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private LoginTryService loginAttemptService;

	private EmailService emailService;
	/*
@Value("${pecb.archivos.ruta}")
private String ARCHIVOS_RUTA;

@Value("${spring.servlet.multipart.max-file-size}")
public DataSize TAMAÑO_MÁXIMO;
 */
	@Autowired
	public UsuarioServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                              LoginTryService loginAttemptService
			, EmailService emailService
			/*, EstudianteService estudianteService,
                              InstructorService instructorService
			 */

	) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.loginAttemptService = loginAttemptService;

		this.emailService = emailService;
			/*
		this.estudianteService = estudianteService;
		this.instructorService = instructorService;
		 */
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findUserByUsername(username);
		if (user == null) {
			LOGGER.error(USER_DONT_EXIST + username);
			throw new UsernameNotFoundException(USER_DONT_EXIST + username);
		} else {
			validateLoginAttempt(user);
			user.setDateLastLogin(user.getDateLastLogin());
			user.setDateLastLogin(new Date());
			userRepository.save(user);
			UserPrincipal userPrincipal = new UserPrincipal(user);
			LOGGER.info(USERNAME_FOUND + username);
			return userPrincipal;
		}

	}

	@Override
	// public Usuario registrar(String firstName, String lastName, String username,
	// String email) throws UsuarioNoEncontradoExcepcion,
	// NombreUsuarioExisteExcepcion, EmailExisteExcepcion, MessagingException {
	public User register(User usuario) throws UserNotFoundException, UsernameExistExcepcion,
			EmailExistExcepcion, MessagingException, IOException {
		validateNewUsernameAndEmail(EMPTY, usuario.getUsername(), usuario.getPersonalData().getEmail());

		// datos de usuario
		User user = new User();
		// user.setUserId(generateUserId());
		String password = usuario.getPassword();
		// user.setNombres(firstName);
		// user.setApellidos(lastName);
		user.setUsername(usuario.getPersonalData().getEmail());
		// user.setEmail(email);
		user.setDateEntry(new Date());
		user.setPassword(encodePassword(password));
		user.setActive(true);
		user.setNotLocked(true);
		// user.setUrlImagenPerfil(getTemporaryProfileImageUrl(username));

		// datos personales
		PersonalData datos = new PersonalData();
		datos.setName(usuario.getPersonalData().getName());
		datos.setLastname(usuario.getPersonalData().getLastname());
		datos.setEmail(usuario.getPersonalData().getEmail());
		datos.setAddress(usuario.getPersonalData().getAddress());
		datos.setCellphone(usuario.getPersonalData().getCellphone());
		datos.setCompany(usuario.getPersonalData().getCompany());


		// asocia datos personales con usuario
		user.setPersonalData(datos);

		//it can save user without datapersonal that is in database, so it's no necessary to save it first
		//because the entity is inside the user entity
		userRepository.save(user);

		userRepository.flush();

		emailService.sendNewPasswordEmail(usuario.getPersonalData().getName(), password,
				usuario.getPersonalData().getEmail());

		return user;
	}

	@Override
	public Optional<User> getById(Integer codigo) {

		return userRepository.findById(codigo);

	}


	@Override
	public User actualizarUsuario(User usuario) throws UserNotFoundException, UsernameExistExcepcion,
			EmailExistExcepcion, IOException, NotFileImageExcepcion {
		User currentUser = validateNewUsernameAndEmail(usuario.getUsername(), usuario.getUsername(),
				usuario.getPersonalData().getEmail());

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
	public int actualizarActive(Boolean active, String username) throws UserNotFoundException,
			UsernameExistExcepcion, EmailExistExcepcion, IOException, NotFileImageExcepcion {
		return userRepository.actualizarIsActive(active, username);
	}

	@Override
	public int actualizarNotLock(Boolean notLock, String username) throws UserNotFoundException,
			UsernameExistExcepcion, EmailExistExcepcion, IOException, NotFileImageExcepcion {
		return userRepository.actualizarNotLocked(notLock, username);
	}

	@Override
	public void resetPassword(String nombreUsuario, String password) throws MessagingException, UserNotFoundException, IOException {

		User usuario = userRepository.findUserByUsername(nombreUsuario);
		if (usuario == null) {
			throw new UserNotFoundException(USER_DONT_EXIST + nombreUsuario);
		}
		usuario.setPassword(encodePassword(password));
		userRepository.save(usuario);

		// datos personales
		PersonalData datos = usuario.getPersonalData();

		// asocia datos personales con usuario
		usuario.setPersonalData(datos);

		userRepository.save(usuario);

		userRepository.flush();


		emailService.sendNewPasswordEmail(usuario.getPersonalData().getName(), password,
				usuario.getPersonalData().getEmail());



	}


	@Override
	public List<User> getUsers() {
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
			throws UserNotFoundException, UsernameExistExcepcion, EmailExistExcepcion {
		User userByNewUsername = findUserByUsername(newUsername);
		User userByNewEmail = findUserByEmail(newEmail);

		// si valida un usuario registrado
		// si es un usuario registrado entra en este bloque
		if (StringUtils.isNotBlank(currentUsername)) {
			User currentUser = findUserByUsername(currentUsername);

			// si no encuentra datos para el usuario registrado
			if (currentUser == null) {
				throw new UserNotFoundException(USER_DONT_EXIST + currentUsername);
			}

			// sale si ya existe ese nombre de usuario para otro usuario registrado
			if (userByNewUsername != null && !currentUser.getIdUser().equals(userByNewUsername.getIdUser())) {
				throw new UsernameExistExcepcion(USERNAME_EXIST);
			}

			// sale si ya existe ese email para un usuario registrado
			if (currentUser.getPersonalData().getEmail().compareToIgnoreCase(newEmail) != 0) {
				if (userByNewEmail != null /* && !currentUser.getCodUsuario().equals(userByNewEmail.getCodUsuario()) */) {
					//throw new EmailExisteExcepcion(EMAIL_YA_EXISTE);
					LOGGER.info(EMAIL_EXIST + " " + newEmail + " " + currentUser.getPersonalData().getEmail());
				}
			}

			return currentUser;
		}
		// cuando no es un usuario registrado vamos a registrar
		else {
			// Si ya existe ese nombre de usuario
			if (userByNewUsername != null) {
				throw new UsernameExistExcepcion(USERNAME_EXIST);
			}
			//no puede registrar dos usuarios con un mismo correo
			if (userByNewEmail != null) {
				//throw new EmailExisteExcepcion(EMAIL_YA_EXISTE);
				LOGGER.info(EMAIL_EXIST + " " + newEmail + " " + userByNewEmail.getPersonalData().getEmail());
			}

			return null;
		}
	}

	public void guardarArchivo(String nombreArchivo, MultipartFile archivo)
			throws IOException, BigFileExcepcion {
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
		return userRepository.findByPersonalData_IdPersonalData(codDatoPersonal);
	}

}
