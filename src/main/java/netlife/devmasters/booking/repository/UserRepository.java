package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByUsername(String username);

	//relationships should be fetched eagerly.
	@EntityGraph(attributePaths = "codDatosPersonales")
	List<User> findAll();

	@Query(value = "select u from User u where lower(u.codDatosPersonales.lastname) like lower(concat('%%', :apellido, '%%')) or lower(u.codDatosPersonales.name) like lower(concat('%%', :nombre, '%%'))")
	public List<User> findUsuariosByNombreApellido(@Param("nombre") String nombre, @Param("apellido") String apellido) ;
	
	@Query(value = "select u from User u where lower(u.codDatosPersonales.lastname) like lower(concat('%%', :apellido, '%%'))")
	public List<User> findUsuariosByApellido(@Param("apellido") String apellido) ;
	
	@Query(value = "select u from User u where lower(u.codDatosPersonales.name) like  lower(concat('%%', :nombre, '%%'))")
	public List<User> findUsuariosByNombre(@Param("nombre") String nombre);

	@Query(value = "SELECT u FROM User u WHERE u.codDatosPersonales.email like %:correo%")
	public List<User> findUsuariosByCorreo(@Param("correo") String correo);

	@Query(value = "SELECT u FROM User u")
	List<User> findAllPageable(Pageable pageable);
	@Modifying
	@Query("UPDATE User u SET u.isActive = ?1 WHERE u.username = ?2")
	int actualizarIsActive(Boolean active, String username);

	@Modifying
	@Query("UPDATE User u SET u.isActive = ?1 WHERE u.username = ?2")
	int actualizarNotLocked(Boolean noLock, String username);

	Optional<User> findByCodDatosPersonales_idPersonalData(Integer personalData);

}
