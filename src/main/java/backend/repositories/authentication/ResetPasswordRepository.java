package backend.repositories.authentication;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.models.authentication.Register;
import backend.models.authentication.ResetPassword;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long>{
	Optional<ResetPassword> findTopByRegisterUserAndTokenIsFalseOrderByIdDesc(Register user);
}
