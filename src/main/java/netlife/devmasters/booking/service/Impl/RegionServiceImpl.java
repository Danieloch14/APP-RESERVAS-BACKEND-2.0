package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.Region;
import netlife.devmasters.booking.repository.RegionRepository;
import netlife.devmasters.booking.service.RegionService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegionServiceImpl implements RegionService {
    @Autowired
    private RegionRepository repo;
    @Override
    public Region save(Region obj) throws DataException {
        return repo.save(obj);
    }

    @Override
    public List<Region> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Region> getById(int id) {
        return repo.findById(id);
    }

    @Override
    public Region update(Region objActualizado) throws DataException {
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
