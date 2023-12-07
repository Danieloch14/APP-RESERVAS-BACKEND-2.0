package netlife.devmasters.booking.service;

import netlife.devmasters.booking.domain.Menu;
import netlife.devmasters.booking.domain.dto.MenuPermissions;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.repository.MenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static netlife.devmasters.booking.constant.MessagesConst.EMPTY_REGISTER;
import static netlife.devmasters.booking.constant.MessagesConst.REGISTER_ALREADY_EXIST;


@Service
@Transactional
public class MenuService {
	private MenuRepository menuRepository;

	@Autowired
	public MenuService(MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
	}

	public List<MenuPermissions> findMenuByIdUsuario(@Param("idUsuario") String idUsuario, @Param("idRol") Integer idRol) {
		return this.menuRepository.findMenuByIdUsuario(idUsuario, idRol);
	}

	public List<Menu> getAll() {
		return this.menuRepository.findAll(MenuRepository.defaultSort);
	}

	public Optional<Menu> getById(Integer id) {
		return menuRepository.findById(id);
	}

	public Menu save(Menu menu) throws DataException {
		if (menu.getLabel().trim().isEmpty())
			throw new DataException(EMPTY_REGISTER);
		Menu objGuardado = menuRepository.findMenuByLabelIgnoreCaseAndParentMenu(menu.getLabel(), menu.getParentMenu());
		if (objGuardado != null && !objGuardado.getIdMenu().equals(menu.getIdMenu())) {
			throw new DataException(REGISTER_ALREADY_EXIST);
		}

		menu.setLabel(menu.getLabel()/*.toUpperCase()*/);
		return menuRepository.save(menu);

	}

	public Menu update(Menu menu) throws DataException {
		/*if (menu.getEtiqueta() != null) {
			Menu objGuardado = menuRepository.findMenuByEtiquetaIgnoreCase(menu.getEtiqueta());
			if (objGuardado != null && !objGuardado.getCodMenu().equals(menu.getCodMenu())) {
				throw new DataException(REGISTRO_YA_EXISTE);
			}
		}*/
		return this.save(menu);
	}

	public void delete(Integer id) throws DataException {
		this.menuRepository.deleteById(id);

	}

	// obtener menús de primer nivel
	public List<Menu> getAllMenuPrimerNivel() {
		return this.menuRepository.findByParentMenuIsNullOrderByOrder();
	}

	// obtener lista de hijos
	public List<Menu> findByMenuPadre(Integer menuPadre) {
		return this.menuRepository.findByParentMenuOrderByOrder(menuPadre);
	}
	public Optional<Menu> findById(Integer id) {
		return this.findById(id);
	}

}
