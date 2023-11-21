package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.Region;
import netlife.devmasters.booking.domain.Reservation;
import netlife.devmasters.booking.exception.dominio.DataException;
import netlife.devmasters.booking.repository.ReservationRepository;
import netlife.devmasters.booking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static netlife.devmasters.booking.constant.MensajesConst.REGISTRO_VACIO;
import static netlife.devmasters.booking.constant.MensajesConst.REGISTRO_YA_EXISTE;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository repo;
    @Override
    public Reservation save(Reservation reservationSave) throws DataException {
            System.out.println("reservationSave: " + reservationSave);
            /*

    }
        if (reservationSave.getName().trim().isEmpty())
            throw new DataException(REGISTRO_VACIO);
        Optional<Region> objSave = repo.findByNameIgnoreCase(regionSave.getName());
        if (objSave.isPresent()) {

            // valida si existe eliminado
            Region regionDelete = objGuardado.get();
            if (regionDelete.getEstado().compareToIgnoreCase(EstadosConst.ELIMINADO) == 0) {
                regionDelete.setEstado(EstadosConst.ACTIVO);
                return repo.save(regionDelete);
            } else {
            throw new DataException(REGISTRO_YA_EXISTE);
        }
            */


        return repo.save(reservationSave);
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
