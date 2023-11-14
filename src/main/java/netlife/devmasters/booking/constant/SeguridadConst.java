
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
			"/usuario/login", 
			"/usuario/registro", 
			"/usuario/imagen/**", 
			"/usuario/guardarArchivo",
			"/usuario/maxArchivo",
			"/apis/test/informes/**", 
			"/link/**", 
			"/usuario/resetPassword/**", 
			"/provincia/**", 
			"/canton/**",
			"/inscripcionfor/**", 
			"/apicbdmq/**", 
			"/swagger-ui/**",
			"/api-docs/**", 
			"/periodoacademico/validaestado",
			"/curso/**",
			"/cursoEstado/**",
			"/inscripcionEsp/**",
			"/convocatoria/activa",
			"/estacionTrabajo/listar",
			"/cargo/listar",
			"/grado/listar",
			"/grado/listarRangos",
			"/postulantesValidos/resultadoPostulantes",
			"/pruebadetalle/listarConDatos",
			"/pruebadetalle/listarConDatos/**",
			"/postulantesValidos/resultadoPostulantes/**",
			"/postulantesValidos/resultadoPostulantes",
	};
}
