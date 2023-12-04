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


@Service
@Transactional
public class RolServiceImpl implements RolService {

	private static final String REGISTRO_VACIO ="" ;
	private static final String REGISTRO_YA_EXISTE = "";
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
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
			throw new DataException(REGISTRO_VACIO);		
		
		Optional<Rol> objGuardado = rolRepository.findByNombreIgnoreCase(obj.getNombre());
		if (objGuardado.isPresent() && !objGuardado.get().getIdRol().equals(obj.getIdRol())) {
			throw new DataException(REGISTRO_YA_EXISTE);
		}

		obj.setNombre(obj.getNombre().toUpperCase());
		return rolRepository.save(obj);
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