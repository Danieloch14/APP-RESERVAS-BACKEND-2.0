package netlife.devmasters.booking.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

import static netlife.devmasters.booking.constant.SecurityConst.RESTRINGED_ACCES;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
/*
se utiliza como punto de entrada
para las respuestas cuando se deniega la autenticación de un usuario.
Usuario no autenticado
Cuando un token no es valido
 */
@Component
public class JwtAutenticacionEntryPoint extends Http403ForbiddenEntryPoint {

	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        HttpResponse httpResponse = new HttpResponse(FORBIDDEN.value(), FORBIDDEN, FORBIDDEN.getReasonPhrase().toUpperCase(), RESTRINGED_ACCES);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(FORBIDDEN.value());
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, httpResponse);
        outputStream.flush();
    }
}