package netlife.devmasters.booking.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;
//custom class
//TODO why
@Data
public class HttpResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss", timezone = "America/Guayaquil")
    private Date timeStamp;
    private int httpStatusCode; // 200, 201, 400, 500
    private HttpStatus httpStatus;
    private String razon;
    private String mensaje;


    // Constructor
    public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String reason, String message) {
        this.timeStamp = new Date();
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.razon = reason;
        this.mensaje = message;
    }
}
