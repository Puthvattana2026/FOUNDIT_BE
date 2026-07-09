package backend.controllers.authenticaiton;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.dtos.authentication.ChooseRoleDTO;
import backend.models.authentication.Register;
import backend.services.authentication.ChooseRoleService;
import backend.utils.authentication.jwt.SignKey;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/role")
@CrossOrigin(value = "${frontend.url}")
@RequiredArgsConstructor
@Slf4j
public class ChoosRoleController {
	
	private final ChooseRoleService chooseRoleService;
	
	@PutMapping("/update-role")
	public ResponseEntity<?> chooseRole(@RequestBody ChooseRoleDTO request){
		
	    if (request.getEmail() == null || request.getEmail().isEmpty()) {
	        return ResponseEntity.status(401).body(Map.of(
	            "error", "Authentication required",
	            "message", "Email is required"
	        ));
	    }
		
	    try {
	        Register user = chooseRoleService.chooseRole(request.getEmail(), request.getRole());
	        String token = createToken(user);
	        return ResponseEntity.ok(Map.of(
	            "message", "Role updated successfully",
	            "role", user.getRole().name(),
	            "token", token
	        ));
	    } catch (RuntimeException e) {
	        return ResponseEntity.badRequest().body(Map.of(
	            "error", "Role update failed",
	            "message", e.getMessage()
	        ));
	    }
	}

	private String createToken(Register user) {
		List<String> authorities = List.of("ROLE_" + user.getRole().name());

		return Jwts.builder()
				.setSubject(user.getEmail())
				.setIssuedAt(new Date())
				.claim("role", user.getRole().name())
				.claim("authorities", authorities)
				.setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
				.setIssuer("FoundIT")
				.signWith(SignKey.getKey())
				.compact();
	}
}
