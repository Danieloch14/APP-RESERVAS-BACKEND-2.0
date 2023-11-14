package netlife.devmasters.booking.exception.dominio;

public class NombreUsuarioExisteExcepcion extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8414101860420752596L;

	public NombreUsuarioExisteExcepcion(String mensaje) {
        super(mensaje);
    }
}
