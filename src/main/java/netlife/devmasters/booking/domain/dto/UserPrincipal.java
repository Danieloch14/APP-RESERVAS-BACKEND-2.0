package netlife.devmasters.booking.domain.dto;

import netlife.devmasters.booking.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
//to store the object in the SecurityContext, which is a context object that is available to all Spring Security-aware components.
public class UserPrincipal implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2566624400445006252L;
	private User usuario;

	public UserPrincipal(User user) {
		this.usuario = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//return stream(this.usuario.getPermisos()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		return new ArrayList<>();
	}

	@Override
	public String getPassword() {
		return this.usuario.getPassword();
	}

	@Override
	public String getUsername() {
		return this.usuario.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.usuario.isNotLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.usuario.isActive();
	}
}
