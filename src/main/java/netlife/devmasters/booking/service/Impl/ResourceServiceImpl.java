package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.Location;
import netlife.devmasters.booking.domain.Resource;
import netlife.devmasters.booking.domain.TypeResource;
import netlife.devmasters.booking.domain.dto.ReservationCreate;
import netlife.devmasters.booking.domain.dto.SearchResourceDto;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.repository.LocationRepository;
import netlife.devmasters.booking.repository.ReservationRepository;
import netlife.devmasters.booking.repository.ResourceRepository;
import netlife.devmasters.booking.service.ResourceService;
import netlife.devmasters.booking.service.TypeResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
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
     @Autowired
    private TypeResourceService typeResourceService;

    @Override
    public Resource save(Resource objSaveII) throws DataException {
        if (objSaveII.getCodNumber().trim().isEmpty())
            throw new DataException(EMPTY_REGISTER);
        List<Resource> objSave = repo.findByCodNumberIgnoreCase(objSaveII.getCodNumber());
        if (objSave != null && !objSave.isEmpty()) {

            // valida si existe eliminado
            /*
             * Region regionDelete = objGuardado.get();
             * if (regionDelete.getEstado().compareToIgnoreCase(EstadosConst.ELIMINADO) ==
             * 0) {
             * regionDelete.setEstado(EstadosConst.ACTIVO);
             * return repo.save(regionDelete);
             * } else {
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
    public List<Resource> getAvailables(SearchResourceDto searchResourceDto) throws DataException {
        Time time = new Time(searchResourceDto.getHours(), searchResourceDto.getMinutes(), 0);
        Timestamp endDate = new Timestamp(searchResourceDto.getDate().getTime() + time.getTime() - 18000000);
        java.sql.Timestamp startDate = searchResourceDto.getDate();
        List<Resource> lista = repo.findByIdLocation_IdRegion(searchResourceDto.getIdRegion(),
                searchResourceDto.getCapacity(), startDate, endDate);
        lista.forEach(resource -> {
            Optional<TypeResource> typeResource = typeResourceService
                    .getById(resource.getIdTypeResource().getIdTypeResource());

            if (typeResource.isEmpty()) {
                try {
                    throw new DataException(
                            "No se encontró el tipo de recurso");
                } catch (DataException e) {
                    e.printStackTrace();
                }
            }
            TypeResource t = typeResource.get();
            java.sql.Timestamp actualDate = new java.sql.Timestamp(System.currentTimeMillis());
            Objects.requireNonNull(startDate, "La fecha de inicio de la reserva no puede ser nula");
            long hours = (startDate.getTime() - actualDate.getTime()) / (1000 * 60 * 60);
            System.out.println(hours);
            if (hours < 0 || hours > t.getTimeAnticipation()) {
                lista.remove(resource);
                System.out.println("removido");
            }
        });
        return lista;
    }

    @Override
    public Resource update(Resource objActualizado, Integer idResource) throws DataException {
        if (objActualizado.getCodNumber() != null) {
            List<Resource> objUpdated = repo.findByCodNumberIgnoreCase(objActualizado.getCodNumber());
            if (objUpdated.size() > 1) {
                throw new DataException(REGISTER_ALREADY_EXIST);
            }
        }
        
        Location location = new Location();
        location.setIdLocation(objActualizado.getIdLocation().getIdLocation());
        location.setIdRegion(objActualizado.getIdLocation().getIdRegion());
        location.setPlace(objActualizado.getIdLocation().getPlace());
        location.setFloor(objActualizado.getIdLocation().getFloor());
        location.setAddress(objActualizado.getIdLocation().getAddress());
        repoLocation.save(location);
        objActualizado.setIdTypeResource(objActualizado.getIdTypeResource());
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
