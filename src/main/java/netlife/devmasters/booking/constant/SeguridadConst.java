
package netlife.devmasters.booking.constant;

import org.springframework.beans.factory.annotation.Value;

public class SeguridadConst {
	public static final long TIEMPO_EXPIRACION = 432_000_000; // 5 days expressed in milliseconds
	public static final String PREFIJO_TOKEN = "Bearer ";
	public static final String HEADER_APP = "X-API-Key";
	public static final String CABECERA_TOKEN_JWT = "Jwt-Token";
	public static final String TOKEN_NO_VERIFICADO = "Token no puede ser verificado";
	public static final String PERMISOS = "permisos";
	public static final String ACCESO_RESTRINGIDO = "Requiere identificarse para acceder a esta página";
	public static final String ACCESO_DENEGADO = "No tiene permisos para acceder a esta página";
	public static final String METOD_HTTP_OPTIONS = "OPTIONS";
	public static final String PLATAFORMA = "Plataforma de reservas de recursos";
	public static final String PLATAFORMA_ADMIN = "Administración de la plataforma de reservas de recursos";

	public static final String[] URLS_PUBLICAS = {
			"/api/v1/users/login",
			"/api/v1/users/registro",
			"/api/v1/users/imagen/**",
			"/api/v1/users/guardarArchivo",
			"/api/v1/users/usuario/maxArchivo",
			"/api/v1/users/resetPassword/**",
			"/swagger-ui/**",
			"/api-docs/**", 
	};
}
