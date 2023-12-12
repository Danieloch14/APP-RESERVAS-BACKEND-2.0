package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.util.HttpResponse;
import netlife.devmasters.booking.domain.RolUser;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.service.RolUserService;
import netlife.devmasters.booking.util.ResponseEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static netlife.devmasters.booking.constant.MessagesConst.SUCCESS;
import static netlife.devmasters.booking.constant.MessagesConst.REGISTER_DELETED;


@RestController
@RequestMapping("/api/v1/roles-users")
public class RolUsuarioResource {

	@Autowired
	private RolUserService rolUsuarioService;

	@GetMapping("")
	public List<RolUser> listar() {
		return this.rolUsuarioService.getAll();
	}
	
	@GetMapping("/user/{id}")
	public List<RolUser> listarPorUsuario(@PathVariable("id") Long codUsuario) {
		return this.rolUsuarioService.getAllByUsuario(codUsuario);
	}
	
	@GetMapping("/listarporrol/{id}")
	public List<RolUser> listarPorRol(@PathVariable("id") Long codRol) {
		return this.rolUsuarioService.getAllByRol(codRol);
	}

	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> guardar(@RequestBody RolUser obj) throws DataException {
		return new ResponseEntity<>(this.rolUsuarioService.save(obj), HttpStatus.OK);
	}

	@PutMapping("")
	public ResponseEntity<RolUser> actualizarDatos(@RequestBody RolUser obj) throws DataException {
		return new ResponseEntity<>(this.rolUsuarioService.update(obj), HttpStatus.OK);
	}

	@DeleteMapping("")
	public ResponseEntity<HttpResponse> eliminarDatos(@RequestBody RolUser obj) throws DataException {
		this.rolUsuarioService.delete(obj);
		return ResponseEntityUtil.response(HttpStatus.OK, REGISTER_DELETED);
	}
	
	@PostMapping("/rol/{id}")
	public ResponseEntity<?> asignar(@RequestBody List<RolUser> lista, @PathVariable("id") Long codUsuario) throws DataException {
		this.rolUsuarioService.deleteAndSave(lista, codUsuario);
		return ResponseEntityUtil.response(HttpStatus.OK, SUCCESS);
	}
}
