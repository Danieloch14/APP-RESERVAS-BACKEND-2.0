
package netlife.devmasters.booking.constant;

public class SecurityConst {
	public static final long TIEMPO_EXPIRACION = 432_000_000; // 5 days expressed in milliseconds
	public static final String PREFIJ_TOKEN = "Bearer ";
	public static final String HEADER_APP = "X-API-Key";
	public static final String HEADER_TOKEN_JWT = "Jwt-Token";
	public static final String TOKEN_NO_VERIFY = "Token no puede ser verificado";
	public static final String PERMISSIONS = "permisos";
	public static final String RESTRINGED_ACCES = "Requiere identificarse para acceder a esta página";
	public static final String DENY_ACCES = "No tiene permisos para acceder a esta página";
	public static final String METOD_HTTP_OPTIONS = "OPTIONS";
	public static final String PLATAFORM = "Plataforma de reservas de recursos";
	public static final String PLATAFORM_ADMIN = "Administración de la plataforma de reservas de recursos";

	public static final String[] URLS_PUBLICS = {
			"/api/v1/users/login",
			"/api/v1/users/register",
			"/api/v1/regions",
			"/api/v1/roles",
			"/api/v1/users/imagen/**",
			"/api/v1/users/guardarArchivo",
			"/api/v1/users/usuario/maxArchivo",
			"/api/v1/users/resetPassword/**",
			"/swagger-ui/**",
			"/api-docs/**", 
			"/api/v1/registers-requests",
			"/v3/api-docs/**",
            "/swagger-ui/**",
            "/v2/api-docs/**",
            "/swagger-resources/**",
			"/doc/swagger-ui/**",
	};
}
