package netlife.devmasters.booking.exception.domain;

public class EmailExistExcepcion extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 96472732694508762L;

	public EmailExistExcepcion(String mensaje) {
        super(mensaje);
    }
}
