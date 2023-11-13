package netlife.devmasters.booking.service;

import netlife.devmasters.booking.domain.TypeResource;
import org.hibernate.exception.DataException;

import java.util.List;
import java.util.Optional;

public interface TypeResourceService {
    TypeResource save(TypeResource obj) throws DataException;

    List<TypeResource> getAll();

    Optional<TypeResource> getById(int id);

    TypeResource update(TypeResource objActualizado) throws DataException;

    void delete(int id) throws Exception;
}
