package backend.repositories.freelancer.setting;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.models.authentication.Freelancer;
import backend.models.freelancer.setting.Setting;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long>{
    Optional<Setting> findByFreelancer(Freelancer freelancer);
    boolean existsByFreelancer(Freelancer freelancer);
}
