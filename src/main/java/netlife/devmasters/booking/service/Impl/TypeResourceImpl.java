package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.TypeResource;
import netlife.devmasters.booking.domain.dto.TypeResourceCreate;
import netlife.devmasters.booking.repository.TypeResourceRepository;
import netlife.devmasters.booking.service.TypeResourceService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeResourceImpl implements TypeResourceService {
    @Autowired
    private TypeResourceRepository repo;
    @Override
    public TypeResource save(TypeResourceCreate obj) throws DataException {
        return repo.save(obj);
    }

    @Override
    public List<TypeResource> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<TypeResource> getById(int id) {
        return repo.findById(id);
    }

    @Override
    public TypeResourceCreate update(TypeResourceCreate objActualizado) throws DataException {
        return repo.save(objActualizado);
    }

    @Override
    public void delete(int id) throws DataException {

    }
}
