package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.domain.Reservation;
import netlife.devmasters.booking.domain.TypeResource;
import netlife.devmasters.booking.domain.dto.ReservationCreate;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.exception.domain.ReservationException;
import netlife.devmasters.booking.service.ReservationService;
import netlife.devmasters.booking.service.ResourceService;
import netlife.devmasters.booking.service.TypeResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import netlife.devmasters.booking.domain.Resource;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationSservice;
    @Autowired
    private TypeResourceService typeResourceService;
    @Autowired
    private ResourceService resourceService;

    @Autowired

    @GetMapping("")
    public Iterable<Reservation> getAll() {
        
        return reservationSservice.getAll();
    }

    @GetMapping("/by-year/{year}")
    public Iterable<Reservation> getByYear(@PathVariable("year") Integer year) throws DataException {
        return reservationSservice.getByYear(year);
    }

    @GetMapping("/by-month-year/{month}/{year}")
    public Iterable<Reservation> getByMonthYear(@PathVariable("month") Integer month,
            @PathVariable("year") Integer year) throws DataException {
        return reservationSservice.getByMonthYear(month, year);
    }

    @GetMapping("/by-day-month-year/{day}/{month}/{year}")
    public Iterable<Reservation> getByDayMonthYear(@PathVariable("day") Integer day,
            @PathVariable("month") Integer month, @PathVariable("year") Integer year) throws DataException {
        return reservationSservice.getByDayMonthYear(day, month, year);
    }

    @PostMapping("")
    public Reservation reserve(@RequestBody ReservationCreate obj)
            throws DataException, ReservationException, ParseException {
        return reservationSservice.reserve(obj);
    }

    @PostMapping("/is-available")
    public Boolean verifyDisponibility(@RequestBody ReservationCreate obj) throws DataException {
        return reservationSservice.isAvailable(obj);
    }

    @PostMapping("/{id}")
    public Reservation update(@PathVariable("id") Integer id, @RequestBody Reservation obj) throws DataException {
        return reservationSservice.update(obj, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) throws Exception {
        reservationSservice.delete(id);
    }

    @PostMapping("/is-range-anticipation")
    public Boolean verifyRangeAnticipation(@RequestBody ReservationCreate obj) throws DataException {
        return reservationSservice.isInRangeAnticipation(obj);
    }

}
