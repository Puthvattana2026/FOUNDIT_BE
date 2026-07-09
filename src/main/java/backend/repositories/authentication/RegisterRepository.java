package backend.repositories.authentication;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.models.authentication.Register;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Long> {
	Optional<Register> findByEmail(String email);
}
