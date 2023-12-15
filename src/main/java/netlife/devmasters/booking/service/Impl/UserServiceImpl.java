
package netlife.devmasters.booking.service.Impl;


import jakarta.mail.MessagingException;
import netlife.devmasters.booking.domain.*;
import netlife.devmasters.booking.domain.dto.UserPrincipal;
import netlife.devmasters.booking.exception.domain.*;
import netlife.devmasters.booking.repository.UserRepository;
import netlife.devmasters.booking.service.*;
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

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static netlife.devmasters.booking.constant.UsersImplConst.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private LoginTryService loginAttemptService;

    private EmailService emailService;
    private PersonalDataService personalDataService;
    private RolUserService rolUserService;
    private RolService rolService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                           LoginTryService loginAttemptService,
                           EmailService emailService,
                           PersonalDataService personalDataService,
                           RolUserService rolUserService,
                           RolService rolService

    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
        this.emailService = emailService;
        this.personalDataService = personalDataService;
        this.rolUserService = rolUserService;
        this.rolService = rolService;
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
    public User register(User entryUser) throws UserNotFoundException, UsernameExistExcepcion,
            EmailExistExcepcion, MessagingException, IOException, DataException {
        validateNewUsernameAndEmail(EMPTY, entryUser.getUsername(), entryUser.getPersonalData().getEmail());

        // datos de usuario
        User user = new User();
        String password = entryUser.getPassword();
        user.setUsername(entryUser.getPersonalData().getEmail());
        user.setDateEntry(new Date());
        user.setPassword(encodePassword(password));
        user.setActive(true);
        user.setNotLocked(true);
        if (entryUser.getPersonalData().getIdPersonalData() != null) {
            user.setPersonalData(entryUser.getPersonalData());
        } else {

            // datos personales
            PersonalData data = new PersonalData();
            data.setName(entryUser.getPersonalData().getName());
            data.setLastname(entryUser.getPersonalData().getLastname());
            data.setEmail(entryUser.getPersonalData().getEmail());
            data.setAddress(entryUser.getPersonalData().getAddress());
            data.setCellphone(entryUser.getPersonalData().getCellphone());
            data.setCompany(entryUser.getPersonalData().getCompany());
            data = personalDataService.savePersonalData(data);
            // asocia datos personales con usuario
            user.setPersonalData(data);
        }

        //it can save user without datapersonal in database, so it's no necessary to save it first
        //because the entity is inside the user entity
        user = userRepository.save(user);

        userRepository.flush();
        RolUser rolUser = new RolUser();
        RolUserId rolUserId = new RolUserId();
        Rol rol = rolService.getByName("USER");
        rolUserId.setIdUser(Long.valueOf(user.getIdUser()));
        rolUserId.setIdRol(rol.getIdRol());
        rolUser.setRolUserId(rolUserId);
        rolUserService.save(rolUser);

        emailService.sendNewPasswordEmail(entryUser.getPersonalData().getName(), password,
                entryUser.getPersonalData().getEmail());

        return user;
    }

    @Override
    public Optional<User> getById(Integer codigo) {

        return userRepository.findById(codigo);

    }


    @Override
    public User updatedUser(User userEntry) throws UserNotFoundException, UsernameExistExcepcion,
            EmailExistExcepcion, IOException, NotFileImageExcepcion, DataException {
        User currentUser = validateNewUsernameAndEmail(userEntry.getUsername(), userEntry.getUsername(),
                userEntry.getPersonalData().getEmail());

        currentUser.setActive(userEntry.isActive());
        currentUser.setNotLocked(userEntry.isNotLocked());
        //currentUser.setProfilePicture(usuario.getProfilePicture());
        PersonalData datos = new PersonalData();
        datos.setIdPersonalData(userEntry.getPersonalData().getIdPersonalData());
        datos.setName(userEntry.getPersonalData().getName());
        datos.setLastname(userEntry.getPersonalData().getLastname());
        datos.setEmail(userEntry.getPersonalData().getEmail());
        datos.setAddress(userEntry.getPersonalData().getAddress());
        datos.setCellphone(userEntry.getPersonalData().getCellphone());
        datos.setCompany(userEntry.getPersonalData().getCompany());
        datos.setPosition(userEntry.getPersonalData().getPosition());
        datos = personalDataService.updatePersonalData(datos, userEntry.getPersonalData().getIdPersonalData());
        userRepository.actualizarUserWithOutDP(currentUser.getUsername(), currentUser.getPassword(), currentUser.getDateEntry(), currentUser.getDateLastLogin(), currentUser.isActive(), currentUser.isNotLocked(), currentUser.getIdUser());
        return currentUser;

    }

    @Override
    public int UpdatedActive(Boolean active, String username) throws UserNotFoundException,
            UsernameExistExcepcion, EmailExistExcepcion, IOException, NotFileImageExcepcion {
        return userRepository.actualizarIsActive(active, username);
    }

    @Override
    public int UpdatedNotLock(Boolean notLock, String username) throws UserNotFoundException,
            UsernameExistExcepcion, EmailExistExcepcion, IOException, NotFileImageExcepcion {
        return userRepository.actualizarNotLocked(notLock, username);
    }

    @Override
    public void resetPassword(String userName, String password) throws MessagingException, UserNotFoundException, IOException {

        User usuario = userRepository.findUserByUsername(userName);
        if (usuario == null) {
            throw new UserNotFoundException(USER_DONT_EXIST + userName);
        }
        usuario.setPassword(encodePassword(password));

        userRepository.save(usuario);

        userRepository.flush();


        emailService.sendNewPasswordEmail(usuario.getPersonalData().getName(), password,
                usuario.getPersonalData().getEmail());


    }

    @Override
    public void changePassword(String email, String lastPassword, String newPassword) throws MessagingException, UserNotFoundException, IOException {

        User usuario = userRepository.findUserByUsername(email);
        if (usuario == null) {
            throw new UserNotFoundException(USER_DONT_EXIST + email);
        }
        if (!passwordEncoder.matches(lastPassword, usuario.getPassword())) {
            throw new UserNotFoundException("La contraseña actual no coincide con la registrada");
        }
        usuario.setPassword(encodePassword(newPassword));
        userRepository.save(usuario);
        userRepository.flush();
    }


    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersPageable(Pageable pageable) {

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
    public void deleteUser(String username) throws Exception {
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


    public List<User> findUsersByFullyName(String name, String lastname) {
        return this.userRepository.findUsuariosByNombreApellido(name, lastname);
    }

    @Override
    public List<User> findUsersByLastname(String lastname) {
        return this.userRepository.findUsuariosByApellido(lastname);
    }

    @Override
    public List<User> findUsersByName(String name) {
        return this.userRepository.findUsuariosByNombre(name);
    }

    @Override
    public List<User> findUsersByEmail(String email) {
        return this.userRepository.findUsuariosByCorreo(email);
    }


    @Override
    public Optional<User> getUsersByCodDatoPersonal(Integer codDataPersonal) {
        return userRepository.findByPersonalData_IdPersonalData(codDataPersonal);
    }

}
