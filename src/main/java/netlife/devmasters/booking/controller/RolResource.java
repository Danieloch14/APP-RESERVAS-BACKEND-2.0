package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.util.HttpResponse;
import netlife.devmasters.booking.domain.Rol;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.service.Impl.RolServiceImpl;
import netlife.devmasters.booking.util.ResponseEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static netlife.devmasters.booking.constant.MessagesConst.REGISTER_DELETED;


@RestController
@RequestMapping("/api/v1/roles")

public class RolResource {
	
	@Autowired
	private RolServiceImpl rolService;
	
	@GetMapping("")
	public List<Rol> listar(){
		return this.rolService.getAll();
	}
	
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> guardar(@RequestBody Rol obj) throws DataException {
		return new ResponseEntity<>(rolService.save(obj), HttpStatus.OK);
	}
	
	@PutMapping("")
	public ResponseEntity<Rol> actualizarDatos(@RequestBody Rol obj) throws DataException{
		return new ResponseEntity<>(this.rolService.update(obj), HttpStatus.OK);
	}

	
	@DeleteMapping("/{id}")
	public ResponseEntity<HttpResponse> eliminarDatos(@PathVariable("id") Long codigo) throws DataException {
		this.rolService.delete(codigo);
		return ResponseEntityUtil.response(HttpStatus.OK, REGISTER_DELETED);
	}


	

}
