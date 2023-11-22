package netlife.devmasters.booking.service;

import netlife.devmasters.booking.domain.Region;
import netlife.devmasters.booking.exception.domain.DataException;

import java.util.List;
import java.util.Optional;

public interface RegionService {
    Region save(Region obj) throws DataException;

    List<Region> getAll();

    Optional<Region> getById(int id);

    Region update(Region objActualizado,Integer idRegion) throws DataException;

    void delete(int id) throws Exception;
}
