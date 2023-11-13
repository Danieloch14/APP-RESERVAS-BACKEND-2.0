package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.Resource;
import netlife.devmasters.booking.domain.dto.TypeResourceCreate;
import netlife.devmasters.booking.service.ResourceService;
import org.hibernate.exception.DataException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Override
    public Resource save(Resource obj) throws DataException {
        return null;
    }

    @Override
    public List<Resource> getAll() {
        return null;
    }

    @Override
    public Optional<Resource> getById(int id) {
        return Optional.empty();
    }

    @Override
    public TypeResourceCreate update(Resource objActualizado) throws DataException {
        return null;
    }

    @Override
    public void delete(int id) throws DataException {

    }
}
