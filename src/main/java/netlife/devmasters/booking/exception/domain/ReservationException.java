package netlife.devmasters.booking.exception.domain;

public class ReservationException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 690598431810325860L;

    public ReservationException(String mensaje) {
        super(mensaje);
    }
    public ReservationException(String message, Throwable cause) {
        super(message, cause);
    }
}
