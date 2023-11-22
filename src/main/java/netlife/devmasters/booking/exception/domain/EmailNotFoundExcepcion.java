package netlife.devmasters.booking.exception.domain;

public class EmailNotFoundExcepcion extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 927905031782230541L;

	public EmailNotFoundExcepcion(String mensaje) {
        super(mensaje);
    }
}
