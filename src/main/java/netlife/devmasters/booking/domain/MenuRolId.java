package netlife.devmasters.booking.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class MenuRolId {
	protected Long idMenu;
	protected Long idRol;
	
	public MenuRolId(Long codMenu2, Long codRol2) {
		this.idMenu = codMenu2;
		this.idRol = codRol2;
	}
	
	public MenuRolId() {
		
	}

}
