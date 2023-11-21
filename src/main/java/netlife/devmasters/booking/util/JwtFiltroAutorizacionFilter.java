package netlife.devmasters.booking.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import netlife.devmasters.booking.constant.ArchivoConst;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static netlife.devmasters.booking.constant.SeguridadConst.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

@Component
public class JwtFiltroAutorizacionFilter extends OncePerRequestFilter {
	private JWTTokenProvider jwtTokenProvider;

	@Value("${netlife.app.key}")
	private String APP_KEY;

	public JwtFiltroAutorizacionFilter(JWTTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			String requestURI = request.getRequestURI();
			String excludedUrlPattern = "/link/\\d+";
			String excludedUrlPattern2 = "/swagger-ui/.*";
			String excludedUrlPattern3 = "/api-docs.*";
			if (requestURI.matches(excludedUrlPattern)||requestURI.matches(excludedUrlPattern2)||requestURI.matches(excludedUrlPattern3)) {
				filterChain.doFilter(request, response);
				return;
			}
			else if (request.getMethod().equalsIgnoreCase(METOD_HTTP_OPTIONS)) {
				response.setStatus(OK.value());
			} else {
				String authorizationHeader = request.getHeader(AUTHORIZATION);
				if (authorizationHeader == null || !authorizationHeader.startsWith(PREFIJO_TOKEN)) {

					// request.getRequestURI()
					RequestMatcher matcher = new RequestHeaderRequestMatcher(HEADER_APP,
							APP_KEY);

					if (!matcher.matches(request)) {
						response.addHeader("errorHeader", "ACCESO NO AUTORIZADO");
						SecurityContextHolder.clearContext();
						response.setStatus(HttpStatus.FORBIDDEN.value());
						response.flushBuffer();
						return;
					}
					filterChain.doFilter(request, response);

					return;
				}
				String token = authorizationHeader.substring(PREFIJO_TOKEN.length());
				String username = jwtTokenProvider.getSubject(token);
				if (jwtTokenProvider.isTokenValid(username, token)
						&& SecurityContextHolder.getContext().getAuthentication() == null) {
					List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);
					Authentication authentication = jwtTokenProvider.getAuthentication(username, authorities, request);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else {
					SecurityContextHolder.clearContext();
				}
			}
		} catch (SizeLimitExceededException e) {
			String mensaje = ArchivoConst.ARCHIVO_MUY_GRANDE;
			response.addHeader("errorHeader", mensaje);
		}
		filterChain.doFilter(request, response);
	}

}