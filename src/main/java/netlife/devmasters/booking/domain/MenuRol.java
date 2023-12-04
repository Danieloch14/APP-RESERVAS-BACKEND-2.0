package netlife.devmasters.booking.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "r_menu_rol")
@Data
public class MenuRol {

	@EmbeddedId
	protected MenuRolId menuRolId;
	protected String permissions;

	public MenuRol() {

	}

	public MenuRol(Long codMenu, Long codRol, String permissions) {
		this.menuRolId = new MenuRolId(codMenu, codRol);
		this.permissions = permissions;
	}

}
