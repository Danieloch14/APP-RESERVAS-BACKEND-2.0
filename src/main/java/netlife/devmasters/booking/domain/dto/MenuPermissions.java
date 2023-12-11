package netlife.devmasters.booking.domain.dto;

import lombok.Data;

@Data
public class MenuPermissions {


	private Integer idMenu;
	private String label;
	private String path;
	private Integer parentMenu;
	private Integer order;
	private String description;
	private String icon;
	private String permisos;

	public MenuPermissions(Integer idMenu, String label, String path, Integer parentMenu, Integer order, String description,String icon, String permisos) {
		this.idMenu = idMenu;
		this.label = label;
		this.path = path;
		this.parentMenu = parentMenu;
		this.order = order;
		this.description = description;
		this.icon = icon;
		this.permisos = permisos;
	}
}
