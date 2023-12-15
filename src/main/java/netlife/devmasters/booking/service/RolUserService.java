package netlife.devmasters.booking.service;


import netlife.devmasters.booking.domain.Rol;
import netlife.devmasters.booking.domain.RolUser;
import netlife.devmasters.booking.exception.domain.DataException;

import java.util.List;

public interface RolUserService {

    List<RolUser> getAll();

    List<RolUser> getAllByUsuario(Long codUsuario);

    List<RolUser> getAllByRol(Long codRol);
    RolUser getByRolAndUsuario(Long codRol, Long codUsuario);

    RolUser save(RolUser obj) throws DataException;

    RolUser update(RolUser objActualizado) throws DataException;

    void delete(RolUser obj) throws DataException;

    void deleteAllByRolUserId_codUsuario(Long codUsuario);

    List<RolUser> saveAll(Iterable<RolUser> entities);

    void deleteAndSave(Iterable<RolUser> entities, Long codUsuario);
}
