package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByUsername(String username);

    Optional<User> findByUsername(String username);

    //relationships should be fetched eagerly.
    @EntityGraph(attributePaths = "personalData")
    List<User> findAll();

    @Query(value = "select u from User u where lower(u.personalData.lastname) like lower(concat('%%', :apellido, '%%')) or lower(u.personalData.name) like lower(concat('%%', :nombre, '%%'))")
    public List<User> findUsuariosByNombreApellido(@Param("nombre") String nombre, @Param("apellido") String apellido);

    @Query(value = "select u from User u where lower(u.personalData.lastname) like lower(concat('%%', :apellido, '%%'))")
    public List<User> findUsuariosByApellido(@Param("apellido") String apellido);

    @Query(value = "select u from User u where lower(u.personalData.name) like  lower(concat('%%', :nombre, '%%'))")
    public List<User> findUsuariosByNombre(@Param("nombre") String nombre);

    @Query(value = "SELECT u FROM User u WHERE u.personalData.email like %:correo%")
    public List<User> findUsuariosByCorreo(@Param("correo") String correo);

    @Query(value = "SELECT u FROM User u")
    List<User> findAllPageable(Pageable pageable);

    @Modifying
    @Query("UPDATE User u SET u.isActive = ?1 WHERE u.username = ?2")
    int actualizarIsActive(Boolean active, String username);

    @Modifying
    @Query("UPDATE User u SET u.isNotLocked = ?1 WHERE u.username = ?2")
    int actualizarNotLocked(Boolean noLock, String username);

    @Modifying
    @Query("UPDATE User u SET u.username = ?1,u.password = ?2, u.dateEntry=?3, u.dateLastLogin=?4, u.isActive=?5, u.isNotLocked=?6 WHERE u.idUser = ?7")
    int actualizarUserWithOutDP(String username, String password, Date dateEntry, Date dateLastLogin, Boolean isActive, Boolean isNotLocked, Integer idUser);

    @Modifying
    @Query("UPDATE User u SET u.isNotLocked = false, u.timeLock=?1 WHERE u.username = ?2")
    int putTimeLock(Timestamp timeLock, String username);

    Optional<User> findByPersonalData_IdPersonalData(Integer personalData);
    @Query(value="select\n" +
            "\tr.*\n" +
            "from\n" +
            "\t \"user\" r\n" +
            "where\n" +
            "\textract(year\n" +
            "from\n" +
            "\tr.time_lock) = :year\n" +
            "\tand extract(month\n" +
            "from\n" +
            "\tr.time_lock) =:month\n" +
            "\tand extract(day\n" +
            "from\n" +
            "\tr.time_lock)=:day", nativeQuery = true)
    List<User> findByTimeLockDate(Integer day, Integer month, Integer year);


}
