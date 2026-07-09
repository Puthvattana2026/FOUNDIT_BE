package backend.services.authentication;

import backend.models.authentication.Register;

public interface RegisterService {
	Register register(Register userRegister);
}
