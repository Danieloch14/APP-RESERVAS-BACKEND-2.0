package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.RegisterRequest;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.repository.RegisterRequestRepository;
import netlife.devmasters.booking.service.RegisterRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegisterRequestServiceImpl implements RegisterRequestService {
    @Autowired
    private RegisterRequestRepository repo;

    @Override
    public RegisterRequest save(RegisterRequest obj) {
        return repo.save(obj);
    }

    @Override
    public List<RegisterRequest> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<RegisterRequest> getById(int id) {
        return repo.findById(id);
    }

    @Override
    public RegisterRequest update(RegisterRequest objActualizado, Integer id) throws DataException {
        objActualizado.setIdRegisterRequest(id);
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
