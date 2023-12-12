package netlife.devmasters.booking.constant;

import org.springframework.beans.factory.annotation.Value;

public class MessagesConst {
	
	@Value("${spring.jpa.properties.hibernate.default_schema}")
	public static String DEFAULT_SCHEMA;
	
	public static final String REGISTER_ALREADY_EXIST = "El registro con esa información ya existe";
	public static final String EMPTY_REGISTER = "El registro no permite campos vacíos";
	public static final String REGISTER_DONT_EXIST = "No existe información con el id ingresado";
	public static final String REGISTRO_YA_EXISTE_PARALELO = "El registro con esa información ya existe en otro nivel";
	public static final String SUCCESS = "Información almacenada con éxito!";
	public static final String ID_ALREADY_EXIST = "Cédula ya existe";
	public static final String CEDULA_NO_EXISTE = "No existe información con la Cédula";
	public static final String CEDULA_INCORRECTA = "La Cédula ingresada es Incorrecta";
	public static final String EMAIL_ALREADY_EXIST = "Correo ya existe";
	public static final String REGISTER_DELETED = "Registro eliminado con éxito";
	public static final String FECHAS_YA_EXISTE = "Ya se encuentran registradas esas fechas";
	public static final String RELATED_DATA = "No se puede eliminar, existen datos relacionados";
    public static final String ZIP_EXITO = "Carpeta comprimida con éxito!";
    public static final String FOLDER_MAX_SIZE = "La carpeta supera el límite máximo permitido ";

}
