package backend.services.authentication;

public interface ResetPasswordService {
	void sendVerifyCode(String email);
	boolean resendVerifyCode(String email);
	void resetPassword(String email, String verifyCode, String newPassword);
}
