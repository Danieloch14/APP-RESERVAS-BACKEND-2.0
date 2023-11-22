package netlife.devmasters.booking.service;

import netlife.devmasters.booking.domain.Location;
import netlife.devmasters.booking.exception.domain.DataException;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    Location save(Location obj) throws DataException;

    List<Location> getAll();

    Optional<Location> getById(int id);

    Location update(Location objActualizado,Integer idLocation) throws DataException;

    void delete(int id) throws Exception;
}
