package netlife.devmasters.booking.service;


import netlife.devmasters.booking.domain.MenuRol;
import netlife.devmasters.booking.exception.domain.DataException;

import java.util.List;

public interface MenuRolService {

	List<MenuRol> getAll();

	List<MenuRol> getAllByRol(Long rol);

	MenuRol save(MenuRol obj) throws DataException;

	MenuRol update(MenuRol objActualizado) throws DataException;

	void delete(MenuRol obj) throws DataException;

	void deleteAllByMenuRolId_CodRol(Long codRol);

	List<MenuRol> saveAll(Iterable<MenuRol> entities);
	
	void deleteAndSave(Iterable<MenuRol> entities);
}
