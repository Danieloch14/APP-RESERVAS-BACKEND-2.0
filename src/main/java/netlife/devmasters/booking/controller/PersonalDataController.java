package netlife.devmasters.booking.controller;

import jakarta.mail.MessagingException;
import netlife.devmasters.booking.domain.HttpResponse;
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

import static netlife.devmasters.booking.constant.MessagesConst.REGISTRO_ELIMINADO_EXITO;

@RestController
@RequestMapping("/personals-data")
public class PersonalDataController {

    @Autowired
    private PersonalDataService objService;

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> guardarDatosPersonales(@RequestBody PersonalData obj) throws DataException, MessagingException, IOException {
        return new ResponseEntity<>(objService.saveDatosPersonales(obj), HttpStatus.OK);
    }

    @GetMapping("/listar")
    public List<PersonalData> listarDatos() {
        return objService.getAllDatosPersonales();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDatosPorId(@PathVariable("id") Integer codigo) {
        try {
            return objService.getDatosPersonalesById(codigo).map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return response(HttpStatus.NOT_FOUND, "Error. Por favor intente más tarde.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonalData> actualizarDatos(@PathVariable("id") Integer codigo,
                                                        @RequestBody PersonalData obj) throws DataException {
        PersonalData personalData = objService.updateDatosPersonales(obj, codigo);
        return new ResponseEntity<>(personalData, HttpStatus.OK);
    }

    @GetMapping("/buscarpaginado")
    public ResponseEntity<?> search(@RequestParam String filtro, Pageable pageable) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(objService.search(filtro, pageable));
        } catch (Exception e) {
            return response(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> eliminarDatos(@PathVariable("id") int codigo) throws DataException {
        objService.deleteById(codigo);
        return response(HttpStatus.OK, REGISTRO_ELIMINADO_EXITO);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message),
                httpStatus);
    }
}
