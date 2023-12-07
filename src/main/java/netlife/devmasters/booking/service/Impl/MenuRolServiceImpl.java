package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.MenuRol;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.repository.MenuRolRepository;
import netlife.devmasters.booking.service.MenuRolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MenuRolServiceImpl implements MenuRolService {

	private MenuRolRepository menuRolRepository;

	@Autowired
	public MenuRolServiceImpl(MenuRolRepository menuRolRepository) {
		this.menuRolRepository = menuRolRepository;
	}

	public MenuRol save(MenuRol menuRol) {
		return this.menuRolRepository.save(menuRol);
	}

	public void delete(MenuRol menuRol) {
		Optional<MenuRol> mr = this.menuRolRepository.findById(menuRol.getMenuRolId());
		if (mr.isPresent()) {
			this.menuRolRepository.delete(mr.get());
		}
	}

	@Override
	public List<MenuRol> getAll() {
		return this.menuRolRepository.findAll();
	}

	@Override
	public MenuRol update(MenuRol objActualizado) throws DataException {
		return this.save(objActualizado);
	}

	@Override
	public List<MenuRol> getAllByRol(Long codRol) {
		return this.menuRolRepository.findByCodRol(codRol);
	}

	@Override
	public void deleteAllByMenuRolId_CodRol(Long codRol) {
		this.menuRolRepository.deleteAllByMenuRolId_IdRol(codRol);
	}

	@Override
	public List<MenuRol> saveAll(Iterable<MenuRol> entities) {
		return this.menuRolRepository.saveAll(entities);
	}

	@Override
	public void deleteAndSave(Iterable<MenuRol> entities) {

		// primero, eliminar todas las entidades asociadas al rol
		if (entities != null && entities.iterator().hasNext()) {
			MenuRol mr = entities.iterator().next();

			this.deleteAllByMenuRolId_CodRol(mr.getMenuRolId().getIdRol());

			// luego, registrar la configuración recibida en la lista
			this.saveAll(entities);

		}

	}

}
