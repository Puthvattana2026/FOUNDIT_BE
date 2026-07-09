package backend.dto.authentication;

import backend.enums.authentication.Role;
import lombok.Data;

@Data
public class RegisterRequestDTO {
	private String username;
	private String email;
	private String password;
	private Role Role;
}
