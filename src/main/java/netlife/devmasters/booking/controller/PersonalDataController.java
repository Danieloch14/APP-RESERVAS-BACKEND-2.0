package netlife.devmasters.booking.controller;

import jakarta.mail.MessagingException;
import netlife.devmasters.booking.util.HttpResponse;
import netlife.devmasters.booking.domain.PersonalData;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.service.PersonalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static netlife.devmasters.booking.constant.MessagesConst.REGISTER_DELETED;

@RestController
@RequestMapping("/api/v1/personals-data")
public class PersonalDataController {

    @Autowired
    private PersonalDataService objService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> guardarDatosPersonales(@RequestBody PersonalData obj) throws DataException, MessagingException, IOException {
        return new ResponseEntity<>(objService.savePersonalData(obj), HttpStatus.OK);
    }

    @GetMapping("")
    public List<PersonalData> listarDatos() {
        return objService.getAllPersonalData();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDatosPorId(@PathVariable("id") Integer code) {
        try {
            return objService.getPersonalDataById(code).map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return response(HttpStatus.NOT_FOUND, "Error. Por favor intente más tarde.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonalData> actualizarDatos(@PathVariable("id") Integer code,
                                                        @RequestBody PersonalData obj) throws DataException {
        PersonalData personalData = objService.updatePersonalData(obj, code);
        return new ResponseEntity<>(personalData, HttpStatus.OK);
    }

    @GetMapping("/buscarpaginado")
    public ResponseEntity<?> search(@RequestParam String filter, Pageable pageable) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(objService.search(filter, pageable));
        } catch (Exception e) {
            return response(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> eliminarDatos(@PathVariable("id") int code) throws DataException {
        objService.deleteById(code);
        return response(HttpStatus.OK, REGISTER_DELETED);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message),
                httpStatus);
    }
}
