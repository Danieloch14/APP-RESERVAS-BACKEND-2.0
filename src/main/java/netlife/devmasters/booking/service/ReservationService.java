package netlife.devmasters.booking.service;

import netlife.devmasters.booking.domain.Reservation;
import netlife.devmasters.booking.domain.dto.ReservationCreate;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.exception.domain.ReservationException;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
    Reservation reserve(ReservationCreate obj) throws DataException, ReservationException;
    Boolean isAvailable(ReservationCreate obj) throws DataException;
    Boolean isInRangeAnticipation(ReservationCreate obj) throws DataException;
    List<Reservation> getByYear(Integer year) throws DataException;
    List<Reservation> getByMonthYear(Integer month, Integer year) throws DataException;
    List<Reservation> getByDayMonthYear(Integer day,Integer Month, Integer Year) throws DataException;

    List<Reservation> getAll();

    Optional<Reservation> getById(int id);

    Reservation update(Reservation objActualizado,Integer idReservation) throws DataException;

    void delete(int id) throws Exception;
}
