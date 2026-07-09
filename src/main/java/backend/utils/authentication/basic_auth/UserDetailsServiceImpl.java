package backend.utils.authentication.basic_auth;

import backend.models.authentication.Register;
import backend.repositories.authentication.RegisterRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final RegisterRepository registerRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Register userRegister = registerRepository.findByEmail(email).orElseThrow();
		
		return AuthUserDetails.builder()
				.username(userRegister.getEmail())
				.password(userRegister.getPassword())
				.authorities(userRegister.getRole().getAuthorities())
				.accountNonExpired(userRegister.isAccountNonExpired())
				.accountNonLocked(userRegister.isAccountNonLocked())
				.credentialsNonExpired(userRegister.isCredentialsNonExpired())
				.enabled(userRegister.isEnabled())
			.build();
	}
}
