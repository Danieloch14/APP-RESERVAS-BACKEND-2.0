package netlife.devmasters.booking.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class RolUserId {
	
	protected Long idRol;
	protected Long idUser;
	
	public RolUserId(Long idRol, Long idUser) {
		this.idRol = idRol;
		this.idUser = idUser;
	}
	
	public RolUserId() {
		
	}

}
