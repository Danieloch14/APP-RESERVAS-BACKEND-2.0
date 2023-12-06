package netlife.devmasters.booking.service;

import netlife.devmasters.booking.domain.RegisterRequest;
import netlife.devmasters.booking.exception.domain.DataException;

import java.util.List;
import java.util.Optional;

public interface RegisterRequestService {
    RegisterRequest save(RegisterRequest obj);

    List<RegisterRequest> getAll();

    Optional<RegisterRequest> getById(int id);

    RegisterRequest update(RegisterRequest objActualizado, Integer id) throws DataException;

    void delete(int id) throws Exception;
}