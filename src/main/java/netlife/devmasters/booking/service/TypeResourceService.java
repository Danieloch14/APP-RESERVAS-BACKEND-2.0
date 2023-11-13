package netlife.devmasters.booking.service;

import netlife.devmasters.booking.domain.TypeResource;
import netlife.devmasters.booking.domain.dto.TypeResourceCreate;
import org.hibernate.exception.DataException;

import java.util.List;
import java.util.Optional;

public interface TypeResourceService {
    TypeResource save(TypeResourceCreate obj) throws DataException;

    List<TypeResource> getAll();

    Optional<TypeResource> getById(int id);

    TypeResourceCreate update(TypeResourceCreate objActualizado) throws DataException;

    void delete(int id) throws DataException;
}
