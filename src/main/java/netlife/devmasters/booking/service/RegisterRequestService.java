package netlife.devmasters.booking.service;

import jakarta.mail.MessagingException;
import netlife.devmasters.booking.domain.RegisterRequest;
import netlife.devmasters.booking.exception.domain.DataException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface RegisterRequestService {
    RegisterRequest save(RegisterRequest obj) throws DataException, MessagingException, IOException;

    List<RegisterRequest> getAll();

    Optional<RegisterRequest> getById(int id);

    RegisterRequest update(RegisterRequest objActualizado, Integer id) throws Exception;

    void delete(int id) throws Exception;
    Boolean approve(int idRequest, Boolean approved ) throws Exception;
    Boolean reject(int idRequest, Boolean approved, String reason ) throws Exception;
    Boolean sendLink(int idRequest) throws Exception;
}
