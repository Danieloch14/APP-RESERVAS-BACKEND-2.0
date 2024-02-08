package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.Reservation;
import netlife.devmasters.booking.domain.Resource;
import netlife.devmasters.booking.domain.TypeResource;
import netlife.devmasters.booking.domain.dto.ReservationCreate;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.exception.domain.ReservationException;
import netlife.devmasters.booking.repository.ReservationRepository;
import netlife.devmasters.booking.service.ReservationService;
import netlife.devmasters.booking.service.ResourceService;
import netlife.devmasters.booking.service.TypeResourceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository repo;
    @Autowired
    private TypeResourceService typeResourceService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Reservation reserve(ReservationCreate reservationSave) throws DataException, ReservationException {
        Time time = new Time(reservationSave.getHours(), reservationSave.getMinutes(), 0);
        Timestamp endDate = new Timestamp(reservationSave.getStartDate().getTime() + time.getTime() - 18000000);
        Timestamp startDate = new Timestamp(reservationSave.getStartDate().getTime() - 18000000);
        reservationSave.setStartDate(startDate);
        reservationSave.setEndDate(endDate);
        Reservation resource = modelMapper.map(reservationSave, Reservation.class);

        if (this.isAvailable(reservationSave) && isInRangeAnticipation(reservationSave)) {
            return repo.save(resource);
        } else if (!isInRangeAnticipation(reservationSave)) {
            throw new ReservationException("Reserva en conflicto debido a que no se encuentra en el rango de anticipacion");
        } else {
            throw new ReservationException("Reserva en conflicto con el recurso para el horario especificado");
        }
    }

    private void validateReservation(ReservationCreate reservationSave) throws ReservationException {
        if (reservationSave.getEndDate().before(reservationSave.getStartDate())) {
            throw new ReservationException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
    }

    @Override
    public Boolean isAvailable(ReservationCreate reservationSave) throws DataException {
        List<Reservation> objSave = repo.findByIdResource_IdResourceAndStartDateBetween(reservationSave.getIdResource(),
                reservationSave.getStartDate());

        return objSave.isEmpty() ? true : false;
    }

    @Override
    public Boolean isInRangeAnticipation(ReservationCreate obj) throws DataException {
        Objects.requireNonNull(obj, "La reserva no puede ser nula");

        java.sql.Timestamp actualDate = new java.sql.Timestamp(System.currentTimeMillis());
        java.sql.Timestamp startDate = obj.getStartDate();
        Objects.requireNonNull(startDate, "La fecha de inicio de la reserva no puede ser nula");

        long hours = (startDate.getTime() - actualDate.getTime()) / (1000 * 60 * 60);

        Optional<Resource> resource = resourceService.getById(obj.getIdResource());
        if (resource.isEmpty()) {
            throw new DataException("No se encontró el recurso");
        }

        Resource r = resource.get();
        Optional<TypeResource> typeResource = typeResourceService.getById(r.getIdTypeResource().getIdTypeResource());

        if (typeResource.isEmpty()) {
            throw new DataException(
                    "No se encontró el tipo de recurso");
        }

        TypeResource t = typeResource.get();
        return hours >= 0 && hours <= t.getTimeAnticipation();
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
        Timestamp actualDate = new Timestamp(System.currentTimeMillis());
        Iterable<Reservation> reservations = repo.findAll();
        for (Reservation reservation : reservations) {
            if (reservation.getEndDate().before(actualDate)) {
                reservation.setStatus("INACTIVO");
                repo.save(reservation);
            }
        }
        return repo.findAll();
    }

    @Override
    public Optional<Reservation> getById(int id) {
        return repo.findById(id);
    }

    @Override
    public Reservation update(Reservation objActualizado, Integer idReservation) throws DataException {
        Time time = new Time(objActualizado.getHours(), objActualizado.getMinutes(), 0);
        Timestamp endDate = new Timestamp(objActualizado.getStartDate().getTime() + time.getTime() - 18000000);
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
