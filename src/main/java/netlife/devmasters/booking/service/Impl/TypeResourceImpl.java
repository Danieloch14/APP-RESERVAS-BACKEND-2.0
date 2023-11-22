package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.TypeResource;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.repository.TypeResourceRepository;
import netlife.devmasters.booking.service.TypeResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static netlife.devmasters.booking.constant.MessagesConst.EMPTY_REGISTER;
import static netlife.devmasters.booking.constant.MessagesConst.REGISTER_ALREADY_EXIST;

@Service
public class TypeResourceImpl implements TypeResourceService {
    @Autowired
    private TypeResourceRepository repo;
    @Override
    public TypeResource save(TypeResource obj) throws DataException {
        if (obj.getName().trim().isEmpty())
            throw new DataException(EMPTY_REGISTER);
        Optional<TypeResource> objSave = repo.findByNameIgnoreCase(obj.getName());
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
    public TypeResource update(TypeResource typeUpdate, Integer idTypeResource) throws DataException {
        if(typeUpdate.getName() !=null) {
            Optional<TypeResource> objUpdated = repo.findByNameIgnoreCase(typeUpdate.getName());
            if (objUpdated.isPresent()&& !objUpdated.get().getIdTypeResource().equals(typeUpdate.getIdTypeResource())) {
                throw new DataException(REGISTER_ALREADY_EXIST);
            }
        }
        typeUpdate.setIdTypeResource(idTypeResource);
        return repo.save(typeUpdate);
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
