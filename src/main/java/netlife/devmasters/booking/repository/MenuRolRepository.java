package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.MenuRol;
import netlife.devmasters.booking.domain.MenuRolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRolRepository extends JpaRepository<MenuRol, MenuRolId> {
	
    @Query("select mr FROM MenuRol mr WHERE mr.menuRolId.idRol = :codRol")
	List<MenuRol> findByCodRol(@Param("codRol") Long codRol);
    
    void deleteAllByMenuRolId_IdRol(Long codRol);
    
    <S extends MenuRol> List<S> saveAll(Iterable<S> entities);
	
	//Optional<MenuRol> findByCodRolAndCodMenu(Integer codMenu,	Integer codRol);

}
