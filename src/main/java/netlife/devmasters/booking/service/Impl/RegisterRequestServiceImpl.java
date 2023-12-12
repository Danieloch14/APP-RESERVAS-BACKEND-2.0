package netlife.devmasters.booking.service.Impl;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import netlife.devmasters.booking.domain.PersonalData;
import netlife.devmasters.booking.domain.RegisterRequest;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.repository.RegisterRequestRepository;
import netlife.devmasters.booking.service.EmailService;
import netlife.devmasters.booking.service.PersonalDataService;
import netlife.devmasters.booking.service.RegisterRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static netlife.devmasters.booking.constant.RegisterConst.*;

@Service
public class RegisterRequestServiceImpl implements RegisterRequestService {
    @Autowired
    private RegisterRequestRepository repo;
    @Autowired
    private PersonalDataService personalDataSvc;
    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public RegisterRequest save(RegisterRequest obj) throws DataException, MessagingException, IOException {
        obj.setStatus(PENDING);
        PersonalData datos = personalDataSvc.savePersonalData(obj.getPersonalData());
        Date date = new Date();
        obj.setRequestDate(date);
        obj.setPersonalData(datos);
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
    public RegisterRequest update(RegisterRequest objActualizado, Integer id) throws Exception {
        objActualizado.setIdRegisterRequest(id);
        Optional<PersonalData> datos = personalDataSvc.getPersonalDataById(objActualizado.getPersonalData().getIdPersonalData());
        if (datos.isEmpty()) {
            throw new Exception("No se encontro el dato personal a editar");
        }
        try {
            personalDataSvc.updatePersonalData(objActualizado.getPersonalData(), objActualizado.getPersonalData().getIdPersonalData());
            objActualizado.setPersonalData(datos.get());
            return repo.save(objActualizado);

        } catch (Exception e) {
            throw new Exception("Existen datos relacionados");
        }
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

    @Override
    public Boolean approve(int idRequest, Boolean approved) throws Exception {
        Optional<RegisterRequest> objGuardado = repo.findById(idRequest);
        if (objGuardado.isEmpty()) {
            throw new Exception("No se encontro el objeto a editar");
        }
        try {
            objGuardado.get().setStatus(APPROVED);
            repo.save(objGuardado.get());
            this.sendLink(idRequest);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean reject(int idRequest, Boolean approved, String reason) throws Exception {
        Optional<RegisterRequest> objGuardado = repo.findById(idRequest);
        if (objGuardado.isEmpty()) {
            throw new Exception("No se encontro el objeto a editar");
        }
        try {
            objGuardado.get().setStatus(REJECTED);
            //objGuardado.get().setStatus(reason);
            repo.save(objGuardado.get());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean sendLink(int idRequest) throws Exception {
        Optional<RegisterRequest> objGuardado = repo.findById(idRequest);
        if (objGuardado.isEmpty()) {
            throw new Exception("No se encontro el objeto a editar");
        }
        try {
            emailService.sendLinkRegister(objGuardado.get());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
