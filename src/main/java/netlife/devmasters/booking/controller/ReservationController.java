package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.domain.Reservation;
import netlife.devmasters.booking.domain.dto.ReservationCreate;
import netlife.devmasters.booking.exception.dominio.DataException;
import netlife.devmasters.booking.service.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationSservice;
    @Autowired
    private ModelMapper modelMapper;
    @GetMapping("")
    public Iterable<Reservation> getAll(){
        return reservationSservice.getAll();
    }
    @PostMapping("")
    public Reservation save(@RequestBody ReservationCreate obj) throws DataException {
        Reservation resource = modelMapper.map(obj, Reservation.class);
        return reservationSservice.save(resource);
    }
    @PostMapping("/disponibility")
    public Reservation verifyDisponibility(@RequestBody ReservationCreate obj) throws DataException {
        return reservationSservice.reserve(obj);
    }
    @PostMapping("/{id}")
    public Reservation update(@PathVariable("id") Integer id,@RequestBody Reservation obj) throws DataException {
        return reservationSservice.update(obj,id);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) throws Exception {
        reservationSservice.delete(id);
    }
}
