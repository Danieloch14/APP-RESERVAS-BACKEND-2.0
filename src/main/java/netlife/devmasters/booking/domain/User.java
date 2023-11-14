package netlife.devmasters.booking.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user")
@Data
/*
@NamedNativeQuery(name = "UsuarioDtoRead.buscarUsuarioPersonalizado",
		query = "select u.cod_usuario, u.cod_modulo, u.nombre_usuario, u.clave, u.fecha_ultimo_login, u.fecha_ultimo_login_mostrar,u.fecha_registro, u.is_active, u.is_not_locked, dp.nombre, dp.apellido, dp.correo_personal from {h-schema}gen_usuario u join {h-schema}gen_dato_personal dp on dp.cod_datos_personales = u.cod_datos_personales",
		resultSetMapping = "UsuarioDtoRead"
)
@SqlResultSetMapping(name = "UsuarioDtoRead", classes = @ConstructorResult(targetClass = UsuarioDtoRead.class, columns = {
		@ColumnResult(name = "cod_usuario"),
		@ColumnResult(name = "cod_modulo"),
		@ColumnResult(name = "nombre_usuario"),
		@ColumnResult(name = "clave"),
		@ColumnResult(name = "fecha_ultimo_login"),
		@ColumnResult(name = "fecha_ultimo_login_mostrar"),
		@ColumnResult(name = "fecha_registro"),
		@ColumnResult(name = "is_active"),
		@ColumnResult(name = "is_not_locked"),
		@ColumnResult(name = "nombre"),
		@ColumnResult(name = "apellido"),
		@ColumnResult(name = "correo_personal")
}))

 */
public class User implements Serializable {

	private static final long serialVersionUID = 9203940391795653856L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Long idUser;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_personal_data")
	private DatoPersonal codDatosPersonales;

	// private String nombres;
	// private String apellidos;
	private String username;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	// private String email;
	// private String urlImagenPerfil;
	@Column(name="date_entry")
	private Date dateEntry;
	@Column(name="date_last_login")
	private Date dateLastLogin;
	private boolean isActive;
	private boolean isNotLocked;



	public User() {
	}

	public User(Long idUser, DatoPersonal codDatosPersonales, String username, String password, Date dateEntry, Date dateLastLogin, boolean isActive, boolean isNotLocked) {
		this.idUser = idUser;
		this.codDatosPersonales = codDatosPersonales;
		this.username = username;
		this.password = password;
		this.dateEntry = dateEntry;
		this.dateLastLogin = dateLastLogin;
		this.isActive = isActive;
		this.isNotLocked = isNotLocked;
	}
}
