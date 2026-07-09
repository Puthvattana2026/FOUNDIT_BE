package backend.services.authentication;

import backend.models.authentication.Register;

public interface OAuthGoogleHandlerService {
	Register findOrCreateFromGoogle(String email, String googleSubject);
}
