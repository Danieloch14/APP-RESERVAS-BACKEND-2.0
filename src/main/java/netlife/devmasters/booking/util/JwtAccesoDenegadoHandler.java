package netlife.devmasters.booking.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

import static netlife.devmasters.booking.constant.SecurityConst.DENY_ACCES;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
/*
 se encarga de manejar las respuestas cuando se deniega el acceso a un recurso
 debido a la falta de autorización.("permisos necesarios") pero si esta autenticado
 */
@Component
public class JwtAccesoDenegadoHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        HttpResponse httpResponse = new HttpResponse(UNAUTHORIZED.value(), UNAUTHORIZED, UNAUTHORIZED.getReasonPhrase().toUpperCase(), DENY_ACCES);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(UNAUTHORIZED.value());
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, httpResponse);
        outputStream.flush();
    }
}