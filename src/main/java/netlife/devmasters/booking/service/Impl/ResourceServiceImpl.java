package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.Resource;
import netlife.devmasters.booking.domain.dto.SearchResourceDto;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.repository.LocationRepository;
import netlife.devmasters.booking.repository.ReservationRepository;
import netlife.devmasters.booking.repository.ResourceRepository;
import netlife.devmasters.booking.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static netlife.devmasters.booking.constant.MessagesConst.EMPTY_REGISTER;
import static netlife.devmasters.booking.constant.MessagesConst.REGISTER_ALREADY_EXIST;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceRepository repo;
    @Autowired
    private LocationRepository repoLocation;
    @Autowired
    private ReservationRepository repoReservation;

    @Override
    public Resource save(Resource objSaveII) throws DataException {
        if (objSaveII.getCodNumber().trim().isEmpty())
            throw new DataException(EMPTY_REGISTER);
        Optional<Resource> objSave = repo.findByCodNumberIgnoreCase(objSaveII.getCodNumber());
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
        repoLocation.save(objSaveII.getIdLocation());
        repoLocation.flush();
        return repo.save(objSaveII);
    }

    @Override
    public List<Resource> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Resource> getById(int id) {
        return repo.findById(id);
    }

    @Override
    public List<Resource> getByIdRegion(int idRegion) {
        return repo.findByIdLocation_IdRegion_IdRegion(idRegion);
    }

    @Override
    public List<Resource> getByNameRegion(String nameRegion) {
        return repo.findByIdLocation_IdRegion_Name(nameRegion);
    }

    @Override
    public List<Resource> getAvailables(SearchResourceDto searchResourceDto) {
        Time time = new Time(searchResourceDto.getHours(),searchResourceDto.getMinutes(),0);
        Timestamp endDate = new Timestamp(searchResourceDto.getDate().getTime() + time.getTime()- 18000000);
        Timestamp startDate = new Timestamp(searchResourceDto.getDate().getTime() - 18000000);
        List<Resource> lista= repo.findByIdLocation_IdRegion(searchResourceDto.getIdRegion(), searchResourceDto.getCapacity(), startDate, endDate);

        return lista;
    }

    @Override
    public Resource update(Resource objActualizado, Integer idResource) throws DataException {
        if (objActualizado.getCodNumber() != null) {
            Optional<Resource> objUpdated = repo.findByCodNumberIgnoreCase(objActualizado.getCodNumber());
            if (objUpdated.isPresent() && !objUpdated.get().getIdTypeResource().equals(objActualizado.getIdTypeResource())) {
                throw new DataException(REGISTER_ALREADY_EXIST);
            }
        }
        objActualizado.setIdResource(idResource);
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
