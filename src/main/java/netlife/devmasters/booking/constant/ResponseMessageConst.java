package netlife.devmasters.booking.constant;

public class ResponseMessageConst {
  private String message;
  public static final String CARGA_EXITOSA = "Carga de archivo satisfactoriamente";
  public static final String CARGA_NO_EXITOSA = "No se puede cargar el archivo";
  public static final String CARGA_ARCHIVO_EXCEL = "Por favor cargar un archivo excel!";
  public static final String ERROR_GENERAR_ARCHIVO = "Error al generar el archivo";
  public static final String EXITO_GENERAR_ARCHIVO = "Archivo generado correctamente";


  public ResponseMessageConst(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
