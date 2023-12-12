package netlife.devmasters.booking.service.Impl;

import netlife.devmasters.booking.domain.Rol;
import netlife.devmasters.booking.exception.domain.DataException;
import netlife.devmasters.booking.repository.RolRepository;
import netlife.devmasters.booking.service.RolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static netlife.devmasters.booking.constant.MessagesConst.EMPTY_REGISTER;
import static netlife.devmasters.booking.constant.MessagesConst.REGISTER_ALREADY_EXIST;


@Service
@Transactional
public class RolServiceImpl implements RolService {
	@Autowired
	private RolRepository rolRepository;

	@Autowired
	public RolServiceImpl(RolRepository rolRepository) {
		this.rolRepository = rolRepository;
	}
	
	public List<Rol> getAll(){
		return this.rolRepository.findAll();
	}

	@Override
	public Rol save(Rol obj) throws DataException {
		if (obj.getNombre().trim().isEmpty())
			throw new DataException(EMPTY_REGISTER);
		
		Optional<Rol> objGuardado = rolRepository.findByNombreIgnoreCase(obj.getNombre());
		if (objGuardado.isPresent() && !objGuardado.get().getIdRol().equals(obj.getIdRol())) {
			throw new DataException(REGISTER_ALREADY_EXIST);
		}

		obj.setNombre(obj.getNombre().toUpperCase());
		return rolRepository.save(obj);
	}

	@Override
	public Rol getByName(String name) throws DataException {
		return rolRepository.findByNombreIgnoreCase(name).orElse(null);
	}

	@Override
	public Optional<Rol> getById(Long id) {
		return rolRepository.findById(id);
	}

	@Override
	public Rol update(Rol objActualizado) throws DataException {
		
		
		return this.save(objActualizado);
		}
		


	@Override
	public void delete(Long id) throws DataException {
		this.rolRepository.deleteById(id);
		
	}
	
	

}
