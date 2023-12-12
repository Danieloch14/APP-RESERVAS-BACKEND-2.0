package netlife.devmasters.booking.service;


import netlife.devmasters.booking.domain.Rol;
import netlife.devmasters.booking.exception.domain.DataException;

import java.util.List;
import java.util.Optional;

public interface RolService {

	Rol save(Rol obj) throws DataException;
	Rol getByName(String name) throws DataException;

	List<Rol> getAll();

	Optional<Rol> getById(Long id);

	Rol update(Rol objActualizado) throws DataException;

	void delete(Long id) throws DataException;
}
