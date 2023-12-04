package netlife.devmasters.booking.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rol")
@Data

public class Rol {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_rol",nullable = false, updatable = false)
	protected Long idRol;
	@Column(name = "rol_name")
	protected String rol_name;
	protected String description;

	public Rol() {

	}

	public Rol(Long cod_rol, String rol_name, String description) {
		this.idRol = cod_rol;
		this.rol_name = rol_name;
		this.description = description;
	}

}
