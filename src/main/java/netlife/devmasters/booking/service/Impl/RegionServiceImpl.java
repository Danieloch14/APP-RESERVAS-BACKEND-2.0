package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.Region;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.repository.RegionRepository;
import netlife.devmasters.booking.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static netlife.devmasters.booking.constant.MessagesConst.EMPTY_REGISTER;
import static netlife.devmasters.booking.constant.MessagesConst.REGISTER_ALREADY_EXIST;

@Service
public class RegionServiceImpl implements RegionService {
    @Autowired
    private RegionRepository repo;

    @Override
    public Region save(Region regionSave) throws DataException {
        if (regionSave.getName().trim().isEmpty())
            throw new DataException(EMPTY_REGISTER);
        Optional<Region> objSave = repo.findByNameIgnoreCase(regionSave.getName());
        if (objSave.isPresent()) {

            // valida si existe eliminado
            /*
            Region regionDelete = objGuardado.get();
            if (regionDelete.getEstado().compareToIgnoreCase(EstadosConst.ELIMINADO) == 0) {
                regionDelete.setEstado(EstadosConst.ACTIVO);
                return repo.save(regionDelete);
            } else {
            */
            throw new DataException(REGISTER_ALREADY_EXIST);
        }


        return repo.save(regionSave);
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
    public Region update(Region regionUpdate, Integer idRegion) throws DataException {
        if(regionUpdate.getName() !=null) {
            Optional<Region> objUpdated = repo.findByNameIgnoreCase(regionUpdate.getName());
            if (objUpdated.isPresent()&& !objUpdated.get().getIdRegion().equals(regionUpdate.getIdRegion())) {
                throw new DataException(REGISTER_ALREADY_EXIST);
            }
        }
        regionUpdate.setIdRegion(idRegion);
        return repo.save(regionUpdate);
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
