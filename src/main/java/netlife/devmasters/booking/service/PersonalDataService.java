package netlife.devmasters.booking.service;

import jakarta.mail.MessagingException;
import netlife.devmasters.booking.domain.PersonalData;
import netlife.devmasters.booking.exception.domain.DataException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import java.io.IOException;
import java.util.Optional;

public interface PersonalDataService {

    PersonalData saveDatosPersonales(PersonalData obj) throws DataException, MessagingException, IOException;

    List<PersonalData> getAllDatosPersonales();

    Optional<PersonalData> getDatosPersonalesById(Integer codigo);

    PersonalData updateDatosPersonales(PersonalData objActualizado, Integer id) throws DataException;

    Page<PersonalData> search(String filtro, Pageable pageable) throws Exception;

    void deleteById(int id) throws DataException;
}
