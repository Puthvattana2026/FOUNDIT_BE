package backend.dtos.authentication;

import lombok.Data;

@Data
public class ChooseRoleDTO {
	private String email;
	private String role;
}
