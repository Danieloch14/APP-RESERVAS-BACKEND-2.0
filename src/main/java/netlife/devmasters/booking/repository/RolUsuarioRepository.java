package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.RolUser;
import netlife.devmasters.booking.domain.RolUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RolUsuarioRepository extends JpaRepository<RolUser, RolUserId>{
	
	 @Query("select ru FROM RolUser ru WHERE ru.rolUserId.idUser = :codUsuario")
		List<RolUser> findByCodUsuario(@Param("codUsuario") Long codUsuario);
	 
	 @Query("select ru FROM RolUser ru WHERE ru.rolUserId.idRol = :codRol")
		List<RolUser> findByCodRol(@Param("codRol") Long codRol);
	 @Query("select ru FROM RolUser ru WHERE ru.rolUserId.idRol = :codRol and ru.rolUserId.idUser = :codUsuario")
	 		RolUser findByCodRolAndCodUsuario(@Param("codRol") Long codRol, @Param("codUsuario") Long codUsuario);
	    
	    void deleteAllByRolUserId_IdUser(Long codUsuario);
	    
	    <S extends RolUser> List<S> saveAll(Iterable<S> entities);
	    
	    

}
