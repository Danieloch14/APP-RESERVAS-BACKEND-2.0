package netlife.devmasters.booking.domain;

import jakarta.persistence.*;
import lombok.Data;
import netlife.devmasters.booking.domain.dto.MenuPermissions;

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
        + "	m.icon, "
        + "	permisos "
        + "	from public.menu m,	 "
        + "	(select distinct id_menu, permissions from public.r_menu_rol gmr where id_rol in "
        + "	(select id_rol	from public.r_user_rol ru where id_user =  "
        + "	(select u.id_user from public.user u where u.username = :idUser and id_rol = :idRol))) permisos "
        + "	where m.id_menu = permisos.id_menu "
        + "	order by coalesce(parent_menu, 0), order_menu",
        resultSetMapping = "MenuPermisos")


@SqlResultSetMapping(name = "MenuPermisos", classes = @ConstructorResult(targetClass = MenuPermissions.class, columns = {
        @ColumnResult(name = "id_menu", type = Integer.class),
        @ColumnResult(name = "label", type = String.class),
        @ColumnResult(name = "path", type = String.class),
        @ColumnResult(name = "parent_menu", type = Integer.class),
        @ColumnResult(name = "order_menu", type = Integer.class),
        @ColumnResult(name = "description", type = String.class),
        @ColumnResult(name = "icon", type = String.class),
        @ColumnResult(name = "permisos", type = String.class)
}))
public class Menu implements Serializable {

    private static final long serialVersionUID = 2695780129293062043L;

    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    protected Integer idMenu;

    protected String label;
    protected String path;
    @Column(name = "parent_menu")
    protected Integer parentMenu;
    @Column(name = "order_menu")
    protected Integer order;
    protected String description;
    protected String icon;

    public Menu() {
    }
}
