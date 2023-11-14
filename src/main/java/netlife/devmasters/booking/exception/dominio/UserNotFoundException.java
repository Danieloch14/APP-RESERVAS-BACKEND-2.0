package netlife.devmasters.booking.exception.dominio;

public class UserNotFoundException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3011869622475572504L;

	public UserNotFoundException(String mensaje) {
        super(mensaje);
    }
}
