package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.util.HttpResponse;
import netlife.devmasters.booking.domain.MenuRol;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.service.MenuRolService;
import netlife.devmasters.booking.util.ResponseEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static netlife.devmasters.booking.constant.MessagesConst.REGISTER_DELETED;
import static netlife.devmasters.booking.constant.MessagesConst.SUCCESS;


@RestController
@RequestMapping("/api/v1/menus-roles")

public class MenuRolResource {
    @Autowired
    private MenuRolService menuRolService;

    @GetMapping("")
    public List<MenuRol> listar() {
        return this.menuRolService.getAll();
    }

    @GetMapping("/{id}")
    public List<MenuRol> listarPorRol(@PathVariable("id") Long codRol) {
        return this.menuRolService.getAllByRol(codRol);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> guardar(@RequestBody MenuRol obj) throws DataException {
        return new ResponseEntity<>(this.menuRolService.save(obj), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<MenuRol> actualizarDatos(@RequestBody MenuRol obj) throws DataException {
        return new ResponseEntity<>(this.menuRolService.update(obj), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<HttpResponse> eliminarDatos(@RequestBody MenuRol obj) throws DataException {
        this.menuRolService.delete(obj);
        return ResponseEntityUtil.response(HttpStatus.OK, REGISTER_DELETED);
    }

    @PostMapping("/menu")
    public ResponseEntity<?> asignar(@RequestBody List<MenuRol> lista) throws DataException {
        this.menuRolService.deleteAndSave(lista);
        return ResponseEntityUtil.response(HttpStatus.OK, SUCCESS);
    }

}
