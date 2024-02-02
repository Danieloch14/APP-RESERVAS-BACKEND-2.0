package netlife.devmasters.booking.service.Impl;


import jakarta.mail.MessagingException;
import netlife.devmasters.booking.domain.PersonalData;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.repository.PersonalDataRepository;
import netlife.devmasters.booking.service.PersonalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static netlife.devmasters.booking.constant.MessagesConst.*;


@Service
public class PersonalDataServiceImpl implements PersonalDataService {
    @Autowired
    private PersonalDataRepository repo;

    @Override
    public PersonalData savePersonalData(PersonalData obj) throws DataException, MessagingException, IOException {
        PersonalData personalDataSave = new PersonalData();
        if (obj.getName() == null || obj.getEmail() == null || obj.getName().isEmpty()
                || obj.getEmail().isEmpty())
            throw new DataException(EMPTY_REGISTER);
        Optional<PersonalData> objGuardado = repo.findOneByEmail(obj.getEmail());
        if (objGuardado.isPresent()) {
            PersonalData personalDataAux = new PersonalData();
            personalDataAux.setEmail(obj.getEmail());
            personalDataAux.setName(obj.getName());
            personalDataAux.setLastname(obj.getLastname());
            personalDataAux.setCellphone(obj.getCellphone());
            personalDataAux.setAddress(obj.getAddress());
            personalDataAux.setCompany(obj.getCompany());
            personalDataAux.setPosition(obj.getPosition());
            personalDataAux.setIdPersonalData(objGuardado.get().getIdPersonalData());   
            try {
                repo.save(personalDataAux);
            }catch (Exception e){
                throw new DataException(REGISTER_ALREADY_EXIST);
            }
            personalDataSave = personalDataAux;
        }else {
            personalDataSave = repo.save(obj);
        }

        return personalDataSave;
    }

    @Override
    public List<PersonalData> getAllPersonalData() {
        return repo.findAll();
    }

    @Override
    public Optional<PersonalData> getPersonalDataById(Integer codigo) {
        return repo.findById(codigo);
    }

    @Override
    public PersonalData updatePersonalData(PersonalData objUpdated, Integer id) throws DataException {

        // verifica si el correo ya está registrado para otro usuario
        String email = objUpdated.getEmail();

        if (email != null && !email.isBlank()) {
            List<PersonalData> list = this.getByPersonalEmail(email);

            if (list != null && !list.isEmpty()) {

                Boolean usuarioActual = false;

                for (PersonalData personalData : list) {
                    if (personalData.getIdPersonalData().compareTo(objUpdated.getIdPersonalData()) == 0) {
                        usuarioActual = true;
                    }
                }

                if (!usuarioActual) {
                    throw new DataException(EMAIL_ALREADY_EXIST);
                }
            }
        }
        objUpdated.setIdPersonalData(id);

        return repo.save(objUpdated);
    }

    private List<PersonalData> getByPersonalEmail(String correoPersonal) {
        return this.repo.findAllByEmail(correoPersonal);
    }

    @Override
    public Page<PersonalData> search(String filter, Pageable pageable) throws Exception {
        try {
            Page<PersonalData> data = repo.searchNativo(filter, pageable);
            return data;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) throws DataException {
        Optional<?> objGuardado = repo.findById(id);
        if (objGuardado.isEmpty()) {
            throw new DataException(REGISTER_DONT_EXIST);
        }
        try {
            repo.deleteById(id);
        } catch (Exception e) {
            if (e.getMessage().contains("constraint")) {
                throw new DataException(RELATED_DATA);
            }
        }
    }
}
