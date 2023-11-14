package netlife.devmasters.booking.constant;

import org.springframework.beans.factory.annotation.Value;

public class MensajesConst {
	
	@Value("${spring.jpa.properties.hibernate.default_schema}")
	public static String DEFAULT_SCHEMA;
	
	public static final String REGISTRO_YA_EXISTE = "El registro con esa información ya existe";
	public static final String REGISTRO_VACIO = "El registro no permite campos vacíos";
	public static final String REGISTRO_NO_EXISTE = "No existe información con el id ingresado";
	public static final String REGISTRO_YA_EXISTE_PARALELO = "El registro con esa información ya existe en otro nivel";
	public static final String EXITO = "Información almacenada con éxito!";
	public static final String CEDULA_YA_EXISTE = "Cédula ya existe";
	public static final String CEDULA_NO_EXISTE = "No existe información con la Cédula";
	public static final String CEDULA_INCORRECTA = "La Cédula ingresada es Incorrecta";
	public static final String CORREO_YA_EXISTE = "Correo ya existe";
	public static final String REGISTRO_ELIMINADO_EXITO = "Registro eliminado con éxito";
	public static final String FECHAS_YA_EXISTE = "Ya se encuentran registradas esas fechas";
	public static final String DATOS_RELACIONADOS = "No se puede eliminar, existen datos relacionados";
    public static final String ZIP_EXITO = "Carpeta comprimida con éxito!";
    public static final String FOLDER_MAX_SIZE = "La carpeta supera el límite máximo permitido ";

}
