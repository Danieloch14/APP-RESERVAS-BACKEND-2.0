package netlife.devmasters.booking.service;

import netlife.devmasters.booking.domain.TypeResource;
import netlife.devmasters.booking.exception.domain.DataException;

import java.util.List;
import java.util.Optional;

public interface TypeResourceService {
    TypeResource save(TypeResource obj) throws DataException;

    List<TypeResource> getAll();

    Optional<TypeResource> getById(int id);

    TypeResource update(TypeResource objActualizado, Integer idTypeResource) throws DataException;

    void delete(int id) throws Exception;
}
