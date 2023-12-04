package netlife.devmasters.booking.domain;

import lombok.Data;

@Data
public class MenuPermisos extends Menu {

	private static final long serialVersionUID = -7349833346217199491L;

	private String permisos;

	public MenuPermisos(Integer codMenu, String etiqueta, String ruta, Integer menu_padre, Integer orden, String descripcion, String permisos) {
		super();
		this.idMenu = codMenu;
		this.label = etiqueta;
		this.path = ruta;
		this.parentMenu = menu_padre;
		this.order = orden;
		this.permisos = permisos;
		this.description = descripcion;
	}

}
