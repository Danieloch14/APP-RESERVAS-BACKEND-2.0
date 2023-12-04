package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long>{
	
	Rol findRolByIdRol(Integer codRol);
	
	Optional<Rol> findByNombreIgnoreCase(String Nombre);

}
