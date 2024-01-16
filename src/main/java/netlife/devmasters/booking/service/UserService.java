package netlife.devmasters.booking.service;

import jakarta.mail.MessagingException;
import netlife.devmasters.booking.domain.User;
import netlife.devmasters.booking.exception.domain.*;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(User usuario)
            throws UserNotFoundException, UsernameExistExcepcion, EmailExistExcepcion, MessagingException, IOException, DataException;

    public Optional<User> getById(Integer codigo);

    List<User> getUsers();

    List<User> getUsersPageable(Pageable pageable);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User updatedUser(User usuario)
            throws UserNotFoundException, UsernameExistExcepcion, EmailExistExcepcion, IOException,
            NotFileImageExcepcion, DataException;

    int UpdatedActive(Boolean active, String username)
            throws UserNotFoundException, UsernameExistExcepcion, EmailExistExcepcion, IOException,
            NotFileImageExcepcion;

    int UpdatedNotLock(Boolean notLock, String username)
            throws UserNotFoundException, UsernameExistExcepcion, EmailExistExcepcion, IOException,
            NotFileImageExcepcion;

    void deleteUser(String username) throws Exception;

    void resetPassword(String email, String password)
            throws MessagingException, EmailNotFoundExcepcion, UserNotFoundException, IOException;

    void changePassword(String email, String lastPassword, String newPassword)
            throws MessagingException, UserNotFoundException, IOException;

    public List<User> findUsersByFullyName(String nombre, String apellido);

    public List<User> findUsersByLastname(String apellido);

    public List<User> findUsersByName(String nombre);

    public List<User> findUsersByEmail(String correo);

    Optional<User> getUsersByCodDatoPersonal(Integer codDatoPersonal);

    int UpdatedLockTime(String username, Integer lockTime)
            throws UserNotFoundException, UsernameExistExcepcion, EmailExistExcepcion, IOException,
            NotFileImageExcepcion;

    int UpdatedNotLockTime();
    List <User> findUserByTimeLock(Timestamp timeLock);
    List <User> findUserByTimeLockDate(int year, int month, int day);
}
