package backend.utils.authentication.jwt;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import backend.repositories.authentication.RegisterRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import backend.dtos.authentication.LoginRequestDTO;
import backend.models.authentication.Client;
import backend.models.authentication.Freelancer;
import backend.models.authentication.Register;
import backend.models.admin.AdminSetting;
import backend.repositories.admin.AdminSettingRepository;
import backend.repositories.authentication.ClientRepository;
import backend.repositories.authentication.FreelancerRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtLogin extends UsernamePasswordAuthenticationFilter{
	
	private final AuthenticationManager authenticationManager;
    private final ClientRepository clientRepository;
    private final FreelancerRepository freelancerRepository;
    private final RegisterRepository registerRepository;
    private final AdminSettingRepository adminSettingRepository;
    private static final Map<String, Integer> FAILED_ATTEMPTS = new ConcurrentHashMap<>();
    private static final int FAILED_ATTEMPT_CACHE_LIMIT = 1_000;
    
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		
		ObjectMapper objMapper = new ObjectMapper();
		
		try {
			
			//check content lenght
			if(request.getContentLength() <= 0) throw new RuntimeException("Request body is empty. Please provide username and password.");
			
			// read value from dto request
			LoginRequestDTO reuqestUser = objMapper.readValue(request.getInputStream(), LoginRequestDTO.class);
			
			//check username and password if empty or null
			if(reuqestUser.getEmail().isEmpty() || reuqestUser.getEmail() == null || reuqestUser.getPassword().isEmpty() || reuqestUser.getPassword() == null) throw new RuntimeException("Username and password are required.");
			request.setAttribute("loginEmail", reuqestUser.getEmail());

			Register register = registerRepository.findByEmail(reuqestUser.getEmail()).orElse(null);
			if (register != null && FAILED_ATTEMPTS.getOrDefault(reuqestUser.getEmail(), 0) >= maxLoginAttempts()) {
				register.setAccountNonLocked(false);
				registerRepository.save(register);
				throw new LockedException("Too many failed login attempts");
			}
			
			// wrape user in a box
			Authentication auth = new UsernamePasswordAuthenticationToken(reuqestUser.getEmail(), reuqestUser.getPassword());
			
			// mark as authenticate
			Authentication authenticate = authenticationManager.authenticate(auth);
			
			return authenticate;
		} catch (IOException e) {
			throw new RuntimeException("Failed to parse login request", e);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		try {
	        SecurityContextHolder.getContext().setAuthentication(authResult);

			String email = authResult.getName();
			
			String token = Jwts.builder()
					           .setSubject(authResult.getName())
					           .setIssuedAt(new Date())
							   .claim("authorities", JwtAuthorities.toAuthorityNames(authResult.getAuthorities()))
							   .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
							   .setIssuer("FoundIT")
							   .signWith(SignKey.getKey())
							   .compact();
			
			Register userRegister = registerRepository.findByEmail(email)
					.orElseThrow(() -> new RuntimeException("Invalid username/email or password"));
			AdminSetting setting = adminSettingRepository.findById(1L).orElse(null);
			if (setting != null && setting.isMaintenanceMode() && !"ADMIN".equals(userRegister.getRole().name())) {
				response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write("{\"status\":\"MAINTENANCE\",\"message\":\"" + escapeJson(setting.getMaintenanceMessage()) + "\"}");
				response.getWriter().flush();
				return;
			}
			FAILED_ATTEMPTS.remove(email);
			userRegister.setAccountNonLocked(true);
			registerRepository.save(userRegister);

			switch (userRegister.getRole().name()) {
				case "CLIENT":
					if (clientRepository.findByEmail(userRegister.getEmail()).isEmpty()) {
						Client client = new Client();
						client.setRegister(userRegister);
						client.setEmail(userRegister.getEmail());
						client.setPassword(userRegister.getPassword());
						client.setUsername(userRegister.getUsername());
						clientRepository.save(client);
		
					}
					break;
		
				case "FREELANCER":
					if (freelancerRepository.findByEmail(userRegister.getEmail()).isEmpty()) {
						Freelancer freelancer = new Freelancer();
						freelancer.setRegister(userRegister);
						freelancer.setEmail(userRegister.getEmail());
						freelancer.setPassword(userRegister.getPassword());
						freelancer.setUsername(userRegister.getUsername());
						freelancerRepository.save(freelancer);
					}
					break;
				}
			
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write("{\"token\":\"" + token + "\"}");
            response.getWriter().flush();
            
		} catch (Exception e) {
			log.debug("Jwt verification failed: {}", e.getMessage());
			SecurityContextHolder.clearContext();
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    response.setContentType("application/json");
		    response.getWriter().write("Unauthorized");
		    return;
		}
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		Object email = request.getAttribute("loginEmail");
		if (email instanceof String loginEmail) {
			registerRepository.findByEmail(loginEmail).ifPresent(register -> {
				if (FAILED_ATTEMPTS.size() > FAILED_ATTEMPT_CACHE_LIMIT) {
					FAILED_ATTEMPTS.clear();
				}
				int failedAttempts = FAILED_ATTEMPTS.merge(loginEmail, 1, Integer::sum);
				if (failedAttempts >= maxLoginAttempts()) {
					register.setAccountNonLocked(false);
				}
				registerRepository.save(register);
			});
		}

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write("{\"status\":\"UNAUTHORIZED\",\"message\":\"Invalid email or password.\"}");
	}

	private int maxLoginAttempts() {
		AdminSetting setting = adminSettingRepository.findById(1L).orElse(null);
		if (setting == null) {
			return 5;
		}
		return Math.max(1, Math.min(5, setting.getMaxLoginAttempts()));
	}

	private String escapeJson(String value) {
		if (value == null) {
			return "";
		}
		return value.replace("\\", "\\\\").replace("\"", "\\\"");
	}
}
