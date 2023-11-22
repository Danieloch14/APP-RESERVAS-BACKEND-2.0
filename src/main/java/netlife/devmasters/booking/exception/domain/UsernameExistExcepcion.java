package netlife.devmasters.booking.exception.domain;

public class UsernameExistExcepcion extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8414101860420752596L;

	public UsernameExistExcepcion(String mensaje) {
        super(mensaje);
    }
}
