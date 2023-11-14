package netlife.devmasters.booking.service;

import jakarta.mail.MessagingException;
import netlife.devmasters.booking.domain.User;
import netlife.devmasters.booking.exception.dominio.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(User usuario)
            throws UserNotFoundException, UsernameExistExcepcion, EmailExistExcepcion, MessagingException, IOException;

    public Optional<User> getById(Integer codigo);

    List<User> getUsers();

    List<User> getUsuariosPageable(Pageable pageable);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User actualizarUsuario(User usuario)
            throws UserNotFoundException, UsernameExistExcepcion, EmailExistExcepcion, IOException,
            NoEsArchivoImagenExcepcion;

    int actualizarActive(Boolean active, String username)
            throws UserNotFoundException, UsernameExistExcepcion, EmailExistExcepcion, IOException,
            NoEsArchivoImagenExcepcion;

    int actualizarNotLock(Boolean notLock, String username)
            throws UserNotFoundException, UsernameExistExcepcion, EmailExistExcepcion, IOException,
            NoEsArchivoImagenExcepcion;

    void eliminarUsuario(String username) throws Exception;

    void resetPassword(String email)
            throws MessagingException, EmailNoEncontradoExcepcion, UserNotFoundException, IOException;

    void guardarArchivo(String nombreArchivo, MultipartFile archivo) throws IOException, ArchivoMuyGrandeExcepcion;

    long tamañoMáximoArchivo();

    public List<User> findUsuariosByNombreApellido(String nombre, String apellido);

    public List<User> findUsuariosByApellido(String apellido);

    public List<User> findUsuariosByNombre(String nombre);

    public List<User> findUsuariosByCorreo(String correo);

    Optional<User> getUsuarioByCodDatoPersonal(Integer codDatoPersonal);
}
