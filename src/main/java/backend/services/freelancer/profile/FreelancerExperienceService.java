package backend.services.freelancer.profile;

import java.util.List;

import backend.dtos.freelancer.profile.client_view.FreelancerExperienceClientViewDTO;
import backend.dtos.freelancer.profile.me.FreelancerExperienceDTO;
import backend.models.freelancer.profile.FreelancerExperience;
import backend.models.freelancer.profile.FreelancerProfile;

public interface FreelancerExperienceService {
	FreelancerExperience getById(Long id);
	FreelancerProfile getByFreelancerProfileId(Long id);
	FreelancerExperience save(Long id, FreelancerExperience request);
	FreelancerExperience update(Long id, Long experienceId, FreelancerExperienceDTO experience);
	List<FreelancerExperienceDTO> getFreelancerExperience(Long id);
	FreelancerExperienceClientViewDTO freelancerExperienceClientView(Long id);
	List<FreelancerExperienceDTO> me (Long id);
}
