package netlife.devmasters.booking.service.Impl;


import jakarta.mail.MessagingException;
import netlife.devmasters.booking.domain.PersonalData;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.repository.PersonalDataRepository;
import netlife.devmasters.booking.repository.UserRepository;
import netlife.devmasters.booking.service.EmailService;
import netlife.devmasters.booking.service.PersonalDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static netlife.devmasters.booking.constant.MessagesConst.*;


@Service
public class PersonalDataServiceImpl implements PersonalDataService {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private PersonalDataRepository repo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository usuarioRepository;

    BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    @Override
    public PersonalData saveDatosPersonales(PersonalData obj) throws DataException, MessagingException, IOException {
        if (obj.getName() == null || obj.getEmail() == null || obj.getName().isEmpty()
                || obj.getEmail().isEmpty())
            throw new DataException(EMPTY_REGISTER);
        Optional<PersonalData> objGuardado = repo.findOneByEmail(obj.getEmail());
        if (objGuardado.isPresent()) {
            throw new DataException(CEDULA_YA_EXISTE);
        }
        return repo.save(obj);
    }

    @Override
    public List<PersonalData> getAllDatosPersonales() {
        return repo.findAll();
    }

    @Override
    public Page<PersonalData> getAllDatosPersonales(Pageable pageable) throws Exception {
        try {
            return repo.findAll(pageable);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public Optional<PersonalData> getDatosPersonalesById(Integer codigo) {
        return repo.findById(codigo);
    }


    @Override
    public List<PersonalData> saveAll(List<PersonalData> datosPersonales) {
        return repo.saveAll(datosPersonales);
    }

    @Override
    public PersonalData updateDatosPersonales(PersonalData objActualizado, Integer id) throws DataException {

        // verifica si el correo ya está registrado para otro usuario
        String correoPersonal = objActualizado.getEmail();

        if (correoPersonal != null && !correoPersonal.isBlank()) {
            List<PersonalData> lista = this.getByCorreoPersonal(correoPersonal);

            if (lista != null && !lista.isEmpty()) {

                Boolean usuarioActual = false;

                for (PersonalData datoPersonal : lista) {
                    if (datoPersonal.getIdPersonalData().compareTo(objActualizado.getIdPersonalData()) == 0) {
                        usuarioActual = true;
                    }
                }

                if (!usuarioActual) {
                    throw new DataException(CORREO_YA_EXISTE);
                }
            }
        }
        objActualizado.setIdPersonalData(id);

        return repo.save(objActualizado);
    }

    private List<PersonalData> getByCorreoPersonal(String correoPersonal) {
        return this.repo.findAllByEmail(correoPersonal);
    }

    @Override
    public Page<PersonalData> search(String filtro, Pageable pageable) throws Exception {
        try {
            // Page<DatosPersonales> datos =
            // repo.findByNombreContainingOrApellidoContaining(filtro, filtro, pageable);
            // Page<Persona> datos = personaRepository.search(filtro, pageable);
            Page<PersonalData> datos = repo.searchNativo(filtro, pageable);
            return datos;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) throws DataException {
        Optional<?> objGuardado = repo.findById(id);
        if (objGuardado.isEmpty()) {
            throw new DataException(REGISTRO_NO_EXISTE);
        }
        try {
            repo.deleteById(id);
        } catch (Exception e) {
            if (e.getMessage().contains("constraint")) {
                throw new DataException(RELATED_DATA);
            }
        }
    }

    public boolean isPasswordMatches(String claveOriginal, String hashPassword) {
        return bcrypt.matches(claveOriginal, hashPassword);
    }
}
