package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.Resource;
import netlife.devmasters.booking.domain.TypeResource;
import netlife.devmasters.booking.domain.dto.TypeResourceCreate;
import netlife.devmasters.booking.repository.ResourceRepository;
import netlife.devmasters.booking.service.ResourceService;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceRepository repo;
    @Override
    public Resource save(Resource obj) throws DataException {
        return repo.save(obj);
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
    public Resource update(Resource objActualizado) throws DataException {
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
