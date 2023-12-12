package netlife.devmasters.booking.controller;

import netlife.devmasters.booking.util.HttpResponse;
import netlife.devmasters.booking.domain.Menu;
import netlife.devmasters.booking.domain.dto.MenuPermissions;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.repository.MenuRepository;
import netlife.devmasters.booking.service.MenuService;
import netlife.devmasters.booking.util.ResponseEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static netlife.devmasters.booking.constant.MessagesConst.REGISTER_DELETED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = { "/api/v1/menus"})
public class MenuResource {
	
	private MenuService menuService;
	
	@Autowired
	private MenuRepository repo;
	@Autowired
	public MenuResource(MenuService menuService) {
		this.menuService = menuService;
	}
	
	@GetMapping("/by-user-rol")
    public ResponseEntity<List<MenuPermissions>> findMenuByIdUsuario(@RequestParam("codUser") String idUsuario, @RequestParam("codRol") Integer idRol) {
        List<MenuPermissions> listaMenu = this.menuService.findMenuByIdUsuario(idUsuario, idRol);
        return new ResponseEntity<List<MenuPermissions>>(listaMenu, OK);
    }
	
	@GetMapping("")
	public ResponseEntity<List<Menu>> findAll() {
		List<Menu> listaMenu = this.menuService.getAll();
        return new ResponseEntity<List<Menu>>(listaMenu, OK);
	}
	
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> guardar(@RequestBody Menu obj) throws DataException {
		return new ResponseEntity<>(menuService.save(obj), HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Menu> actualizarDatos(@RequestBody Menu obj) throws DataException{
		return new ResponseEntity<>(this.menuService.update(obj), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<HttpResponse> eliminarDatos(@PathVariable("id") Integer codigo) throws DataException {
		this.menuService.delete(codigo);
		return ResponseEntityUtil.response(HttpStatus.OK, REGISTER_DELETED);
	}

	private Object response(HttpStatus badRequest, String string) {
		return null;
	}
	
	@GetMapping("/listarPrimerNivel")
	public ResponseEntity<List<Menu>> getAllMenuPrimerNivel() throws DataException {
		List<Menu> listaMenu = this.menuService.getAllMenuPrimerNivel();
        return new ResponseEntity<List<Menu>>(listaMenu, OK);
	}
	
	@GetMapping("listarHijos/{codMenuPadre}")
	public ResponseEntity<List<Menu>> findByMenuPadre(@PathVariable("codMenuPadre") Integer menuPadre) throws DataException 
	{
		List<Menu> listaMenu = this.menuService.findByMenuPadre(menuPadre);
        return new ResponseEntity<List<Menu>>(listaMenu, OK);
	}
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Menu>> getMenuById(@PathVariable("id")int id) throws DataException
	{
		Optional<Menu> Menu = this.menuService.getById(id);
		return new ResponseEntity<Optional<Menu>>(Menu, OK);
	}
	

}
