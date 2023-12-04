package netlife.devmasters.booking.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class RolUserId {
	
	protected Long codRol;
	protected Long codUser;
	
	public RolUserId(Long codRol, Long codUser) {
		this.codRol = codRol;
		this.codUser = codUser;
	}
	
	public RolUserId() {
		
	}

}
