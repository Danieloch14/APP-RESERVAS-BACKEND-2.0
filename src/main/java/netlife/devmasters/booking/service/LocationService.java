package netlife.devmasters.booking.service;

import netlife.devmasters.booking.domain.Location;
import org.hibernate.exception.DataException;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    Location save(Location obj) throws DataException;

    List<Location> getAll();

    Optional<Location> getById(int id);

    Location update(Location objActualizado) throws DataException;

    void delete(int id) throws Exception;
}
