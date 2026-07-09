package backend.repositories.freelancer.profile;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.models.freelancer.profile.FreelancerRightSideBar;

@Repository
public interface FreelancerRightSideBarRepository extends JpaRepository<FreelancerRightSideBar, Long>{
	List<FreelancerRightSideBar> findByFreelancerProfileId(Long freelancerProfileId);
	Optional<FreelancerRightSideBar> findFirstByFreelancerProfileId(Long freelancerProfileId);
	Optional<FreelancerRightSideBar> findByIdAndFreelancerProfileId(Long id, Long freelancerProfileId);
}
