package netlife.devmasters.booking.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "menu")
@Data

@NamedNativeQuery(name = "MenuPermisos.findMenuByIdUsuario", query = "	select m.id_menu, "
		+ "	m.label, "
		+ "	m.path, "
		+ "	m.parent_menu, "
		+ "	m.order_menu, "
		+ "	m.description, "
		+ "	permisos "
		+ "	from public.menu m,	 "
		+ "	(select distinct id_menu, permissions from public.r_menu_rol gmr where id_rol in "
		+ "	(select id_rol	from public.r_user_rol ru where id_user =  "
		+ "	(select u.id_user from user u where u.username = :id_usuario))) permisos "
		+ "	where m.id_menu = permisos.id_menu "
		+ "	order by coalesce(parent_menu, 0), order_menu",
		resultSetMapping = "MenuPermisos")



@SqlResultSetMapping(name = "MenuPermisos", classes = @ConstructorResult(targetClass = MenuPermisos.class, columns = {
		@ColumnResult(name = "cod_menu"),
		@ColumnResult(name = "etiqueta"),
		@ColumnResult(name = "ruta"),
		@ColumnResult(name = "menu_padre"),
		@ColumnResult(name = "orden"),
		@ColumnResult(name = "descripcion"),
		@ColumnResult(name = "permisos")
}))
public class Menu implements Serializable {
	
	private static final long serialVersionUID = 2695780129293062043L;

	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	protected Integer codMenu;

	protected String label;
	protected String path;
	@Column(name = "parent_menu")
	protected Integer parentMenu;
	protected Integer order;
	protected String description;

	public Menu() {
	}

	public Menu(Integer codMenu, String label, String path, Integer menu_padre, Integer order, String description) {
		super();
		this.codMenu = codMenu;
		this.label = label;
		this.path = path;
		this.parentMenu = menu_padre;
		this.order = order;
		this.description = description;
	}

}
