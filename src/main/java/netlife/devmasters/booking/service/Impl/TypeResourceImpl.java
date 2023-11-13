package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.TypeResource;
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
    public TypeResource save(TypeResource obj) throws DataException {
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
    public TypeResource update(TypeResource objActualizado) throws DataException {
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
