package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.Reservation;
import netlife.devmasters.booking.domain.dto.ReservationCreate;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.exception.domain.ReservationException;
import netlife.devmasters.booking.repository.ReservationRepository;
import netlife.devmasters.booking.service.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository repo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Reservation reserve(ReservationCreate reservationSave) throws DataException, ReservationException {

        Time time = new Time(reservationSave.getHours(),reservationSave.getMinutes(),0);
        Timestamp endDate = new Timestamp(reservationSave.getStartDate().getTime() + time.getTime()- 18000000);
        reservationSave.setEndDate(endDate);
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
    public Boolean isAvailable(ReservationCreate reservationSave) throws DataException {
        Time time = new Time(reservationSave.getHours(),reservationSave.getMinutes(),0);
        Timestamp endDate = new Timestamp(reservationSave.getStartDate().getTime() + time.getTime()- 18000000);
        reservationSave.setEndDate(endDate);
        Optional<Reservation> objSave = repo.findByIdResource_IdResourceAndStartDateBetween(reservationSave.getIdResource(), reservationSave.getStartDate(), reservationSave.getEndDate());

        return objSave.isEmpty() ? true : false;
    }

    @Override
    public List<Reservation> getByYear(Integer year) throws DataException {
        return repo.findByStartDate_Year(year);

    }

    @Override
    public List<Reservation> getByMonthYear(Integer month, Integer year) throws DataException {
        return repo.findByStartDate_Month_Year(month, year);
    }

    @Override
    public List<Reservation> getByDayMonthYear(Integer day, Integer Month, Integer Year) throws DataException {
        return repo.findByStartDate_Day_Month_Year(day, Month, Year);
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
        Time time = new Time(objActualizado.getHours(),objActualizado.getMinutes(),0);
        Timestamp endDate = new Timestamp(objActualizado.getStartDate().getTime() + time.getTime()- 18000000);
        objActualizado.setEndDate(endDate);
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
