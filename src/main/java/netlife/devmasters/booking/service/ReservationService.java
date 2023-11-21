package netlife.devmasters.booking.service;

import netlife.devmasters.booking.domain.Reservation;
import netlife.devmasters.booking.exception.dominio.DataException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
    Reservation save(Reservation obj) throws DataException;

    List<Reservation> getAll();

    Optional<Reservation> getById(int id);

    Reservation update(Reservation objActualizado,Integer idReservation) throws DataException;

    void delete(int id) throws Exception;
}
