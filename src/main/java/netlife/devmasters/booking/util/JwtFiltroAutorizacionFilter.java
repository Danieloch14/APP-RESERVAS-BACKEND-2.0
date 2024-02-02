package netlife.devmasters.booking.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import netlife.devmasters.booking.constant.FileConst;
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

import static netlife.devmasters.booking.constant.SecurityConst.*;
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
			// patrons to exclude the filter
			String requestURI = request.getRequestURI();
			String excludedUrlPattern = "/link/\\d+";
			String excludedUrlPattern2 = "/doc/swagger-ui/.*";
			String excludedUrlPattern3 = "/v3/api-docs/swagger-config.*";
			String excludedUrlPattern4 = "/v3/api-docs.*";
			if (requestURI.matches(excludedUrlPattern) ||
					requestURI.matches(excludedUrlPattern2) ||
					requestURI.matches(excludedUrlPattern3) ||
					requestURI.matches(excludedUrlPattern4)) {
				// allow to continue filter chain in seguridad config normally
				// Configuración CORS para Swagger
				response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
				response.setHeader("Access-Control-Allow-Origin", "http://localhost:8083");
				response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
				response.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");
				response.setHeader("Access-Control-Allow-Credentials", "true");
				response.setHeader("Access-Control-Max-Age", "3600");
				response.setStatus(OK.value());
				response.setHeader(APP_KEY, APP_KEY);
				filterChain.doFilter(request, response);
				return;
			}
			// OPTIONS is used in CORS preflight, dont require security management
			else if (request.getMethod().equalsIgnoreCase(METOD_HTTP_OPTIONS)) {
				response.setStatus(OK.value());
			} else {
				String authorizationHeader = request.getHeader(AUTHORIZATION);
				if (authorizationHeader == null || !authorizationHeader.startsWith(PREFIJ_TOKEN)) {

					// request.getRequestURI()
					RequestMatcher matcher = new RequestHeaderRequestMatcher(HEADER_APP,
							APP_KEY);
					System.out.println("matcher: " + matcher);

					if (!matcher.matches(request)) {
						System.out.println("ACCESO NO AUTORIZADO JAIR");
						response.addHeader("errorHeader", "ACCESO NO AUTORIZADO JAIR");
						SecurityContextHolder.clearContext();
						response.setStatus(HttpStatus.FORBIDDEN.value());
						response.flushBuffer();
						response.sendError(HttpStatus.FORBIDDEN.value(), "ACCESO NO AUTORIZADO JAIR");
						response.setContentType("application/json");
						return;
					}
					filterChain.doFilter(request, response);

					return;
				}
				String token = authorizationHeader.substring(PREFIJ_TOKEN.length());
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
			String mensaje = FileConst.BIG_FILE;
			response.addHeader("errorHeader", mensaje);
		}
		filterChain.doFilter(request, response);
	}

}
