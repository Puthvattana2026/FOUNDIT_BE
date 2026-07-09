package backend.services.authentication;

import backend.models.authentication.Register;

public interface ChooseRoleService {
	Register chooseRole(String email, String role);
	void createRoleEntity(Register registeredUser);
}
