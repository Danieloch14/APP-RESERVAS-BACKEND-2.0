package netlife.devmasters.booking.service;

import netlife.devmasters.booking.domain.Resource;
import netlife.devmasters.booking.domain.dto.ReservationCalendar;
import netlife.devmasters.booking.domain.dto.SearchResourceDto;
import netlife.devmasters.booking.exception.domain.DataException;

import java.util.List;
import java.util.Optional;

public interface ResourceService {
    Resource save(Resource obj) throws DataException;

    List<Resource> getAll();

    Optional<Resource> getById(int id);
    List<Resource> getByIdRegion(int idRegion);
    List<Resource> getByNameRegion(String nameRegion);
    List<Resource> getAvailables(SearchResourceDto searchResourceDto) throws DataException;

    Resource update(Resource objActualizado, Integer idResource) throws DataException;

    void delete(int id) throws Exception;
}
