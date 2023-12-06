package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.domain.RegisterRequest;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.service.RegisterRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/V1/registers-requests")
public class RegisterRequestController {
    @Autowired
    private RegisterRequestService service;

    @GetMapping("")
    Iterable<RegisterRequest> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    RegisterRequest getById(@PathVariable("id") Integer id) {
        return service.getById(id).get();
    }

    @PostMapping("")
    RegisterRequest save(RegisterRequest obj) {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    RegisterRequest update(@PathVariable("id") Integer id, @RequestBody RegisterRequest obj) throws DataException {
        return service.update(obj, id);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") int id) throws Exception {
        service.delete(id);
    }

}
