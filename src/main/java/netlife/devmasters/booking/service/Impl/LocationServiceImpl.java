package netlife.devmasters.booking.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import netlife.devmasters.booking.domain.Location;
import netlife.devmasters.booking.domain.Region;
import netlife.devmasters.booking.domain.dto.LocationCreate;
import netlife.devmasters.booking.domain.dto.TypeResourceCreate;
import netlife.devmasters.booking.repository.LocationRepository;
import netlife.devmasters.booking.repository.RegionRepository;
import netlife.devmasters.booking.service.LocationService;
import org.hibernate.exception.DataException;
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
    public Location save(LocationCreate obj) throws DataException {
        Location location = new Location();
        location.setAddress(obj.getAddress());
        location.setPlace(obj.getPlace());
        location.setFloor(obj.getFloor());
        Region region = regionRepository.findById(obj.getIdRegion()).get();
        location.setIdRegion(region);
        return repo.save(location);
    }

    @Override
    public List<Location> getAll() {

        return repo.findAll();
    }

    @Override
    public Optional<Location> getById(int id) {
        return Optional.empty();
    }

    @Override
    public TypeResourceCreate update(LocationCreate objActualizado) throws DataException {
        return null;
    }

    @Override
    public void delete(int id) throws DataException {

    }
}
