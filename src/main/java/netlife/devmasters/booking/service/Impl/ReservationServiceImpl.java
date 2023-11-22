package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.Reservation;
import netlife.devmasters.booking.domain.dto.ReservationCreate;
import netlife.devmasters.booking.exception.dominio.DataException;
import netlife.devmasters.booking.exception.dominio.ReservationException;
import netlife.devmasters.booking.repository.ReservationRepository;
import netlife.devmasters.booking.service.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository repo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Reservation reserve(ReservationCreate reservationSave) throws DataException, ReservationException {
        validateReservation(reservationSave);

        Reservation resource = modelMapper.map(reservationSave, Reservation.class);

        if (this.isAvailable(reservationSave)) {
            return repo.save(resource);
        } else {
            throw new ReservationException("Reserva en conflicto para el horario especificado");
        }
    }

    private void validateReservation(ReservationCreate reservationSave) throws ReservationException {
        if (reservationSave.getEndDate().before(reservationSave.getStartDate())) {
            throw new ReservationException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
    }
    @Override
    public Boolean isAvailable(ReservationCreate obj) throws DataException {
        Date now = new Date();
        // Crea un nuevo objeto Timestamp a partir del objeto Date
        Timestamp timestamp = new Timestamp(now.getTime());
        Optional<Reservation> objSave = repo.findByIdResource_IdResourceAndStartDateBetween(obj.getIdResource(), obj.getStartDate(), obj.getEndDate());

        return objSave.isEmpty() ? true : false;
    }

    @Override
    public List<Reservation> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Reservation> getById(int id) {
        return repo.findById(id);
    }

    @Override
    public Reservation update(Reservation objActualizado, Integer idReservation) throws DataException {
        objActualizado.setIdReservation(idReservation);
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
