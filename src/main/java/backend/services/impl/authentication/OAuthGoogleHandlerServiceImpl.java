package backend.services.impl.authentication;

import org.springframework.stereotype.Service;

import backend.models.authentication.Register;
import backend.repositories.authentication.RegisterRepository;
import backend.services.authentication.OAuthGoogleHandlerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuthGoogleHandlerServiceImpl implements OAuthGoogleHandlerService{

	private final RegisterRepository registerRepository;
	
	@Override
	@Transactional
	public Register findOrCreateFromGoogle(String email, String googleSubject) {
		return registerRepository.findByEmail(email)
				.orElseGet(() -> {
					Register user = new Register();
					
					user.setEmail(email);
					user.setUsername(email);
					user.setGoogleSubject(googleSubject);
					
					user.setAccountNonExpired(true);
					user.setAccountNonLocked(true);
					user.setCredentialsNonExpired(true);
					user.setEnabled(true);
					
					return registerRepository.save(user);
				});
	}
	

}
