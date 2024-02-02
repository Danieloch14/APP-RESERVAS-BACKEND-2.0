package netlife.devmasters.booking.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "r_user_rol")
@Data
public class RolUser {

	@EmbeddedId
	private RolUserId rolUserId;

	public RolUser(RolUserId rolUserId) {
		this.rolUserId = rolUserId;
	}

	public RolUser(Long codRol, Long codUser) {
		this.rolUserId = new RolUserId(codRol, codUser);
	}

	public RolUser() {
	}

}
