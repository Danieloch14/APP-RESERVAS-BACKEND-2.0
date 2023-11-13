package netlife.devmasters.booking.service;

import netlife.devmasters.booking.domain.Location;
import netlife.devmasters.booking.domain.Location;
import netlife.devmasters.booking.domain.dto.LocationCreate;
import netlife.devmasters.booking.domain.dto.TypeResourceCreate;
import org.hibernate.exception.DataException;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    Location save(LocationCreate obj) throws DataException;

    List<Location> getAll();

    Optional<Location> getById(int id);

    TypeResourceCreate update(LocationCreate objActualizado) throws DataException;

    void delete(int id) throws DataException;
}
