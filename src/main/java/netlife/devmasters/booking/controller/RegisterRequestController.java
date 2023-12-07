package netlife.devmasters.booking.controller;

import jakarta.mail.MessagingException;
import netlife.devmasters.booking.domain.RegisterRequest;
import netlife.devmasters.booking.exception.domain.*;
import netlife.devmasters.booking.service.RegisterRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/registers-requests")
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
    RegisterRequest save(@RequestBody RegisterRequest obj) throws DataException, MessagingException, IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    RegisterRequest update(@PathVariable("id") Integer id, @RequestBody RegisterRequest obj) throws Exception {
        return service.update(obj, id);
    }

    @PutMapping("/approved-rejected")
    public ResponseEntity<Boolean> updateNotLocked(@RequestParam("approve") Boolean approve,
                                                   @RequestParam("idRequest") Integer idRequest) {
        Boolean result;
        if (approve) {
            try {
                result = service.approve(idRequest, approve);
            } catch (Exception e) {
                result = false;
            }
        } else {
            try {
                result = service.reject(idRequest, approve, "No se pudo aprobar");
            } catch (Exception e) {
                result = false;
            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PostMapping("/link/{id}")
    public ResponseEntity<Boolean> resendRegistrationToken(@PathVariable("id") Integer id) {
        Boolean result;
        try {
            result = service.sendLink(id);
        } catch (Exception e) {
            result = false;
        }
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") int id) throws Exception {
        service.delete(id);
    }

}
