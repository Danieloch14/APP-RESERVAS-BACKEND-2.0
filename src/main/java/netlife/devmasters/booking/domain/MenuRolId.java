package netlife.devmasters.booking.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class MenuRolId {
	protected Long codMenu;
	protected Long codRol;
	
	public MenuRolId(Long codMenu2, Long codRol2) {
		this.codMenu = codMenu2;
		this.codRol = codRol2;
	}
	
	public MenuRolId() {
		
	}

}
