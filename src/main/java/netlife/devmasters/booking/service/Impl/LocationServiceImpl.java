package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.Location;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.repository.LocationRepository;
import netlife.devmasters.booking.repository.RegionRepository;
import netlife.devmasters.booking.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository repo;
    @Autowired
    private RegionRepository regionRepository;

    @Override
    public Location save(Location obj) throws DataException {
        /*
        Location location = new Location();
        location.setAddress(obj.getAddress());
        location.setPlace(obj.getPlace());
        location.setFloor(obj.getFloor());
        Region region = regionRepository.findById(obj.getIdRegion()).get();
        location.setIdRegion(region);
        return repo.save(location);

         */
        return repo.save(obj);
    }

    @Override
    public List<Location> getAll() {

        return repo.findAll();
    }

    @Override
    public Optional<Location> getById(int id) {
        return repo.findById(id);
    }

    @Override
    public Location update(Location objActualizado, Integer idLocation) throws DataException {
        objActualizado.setIdLocation(idLocation);
        return repo.save(objActualizado);
    }

    @Override
    public void delete(int id) throws Exception {
        Optional<?> objGuardado = repo.findById(id);
        if (objGuardado.isEmpty()) {
            throw new Exception("No se encontro el objeto a eliminar");
        }
        try {
            repo.deleteById(id);
        } catch (Exception e) {
            if (e.getMessage().contains("constraint")) {
                throw new Exception("Existen datos relacionados");
            }
        }

    }
}
