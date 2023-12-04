package netlife.devmasters.booking.repository;

import netlife.devmasters.booking.domain.Menu;
import netlife.devmasters.booking.domain.MenuPermisos;
import netlife.devmasters.booking.exception.domain.DataException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
	
	public static Sort defaultSort = Sort.by(Sort.Order.asc("parentMenu").with(NullHandling.NULLS_FIRST) /*, Sort.Order.asc("orden")*/);
	
	Optional<Menu> getById(int id);
	
	@Query(nativeQuery = true)
	List<MenuPermisos> findMenuByIdUsuario(@Param("id_usuario") String idUsuario);

	List<Menu> findByParentMenuIsNullOrderByOrder();
	//List<Menu> findByParentMenuIsNullOrderByOrder();
	
	List<Menu> findByParentMenuOrderByOrder(Integer menuPadre);

	Menu findMenuByLabelIgnoreCaseAndParentMenu(String etiqueta, Integer menuPadre);

}
