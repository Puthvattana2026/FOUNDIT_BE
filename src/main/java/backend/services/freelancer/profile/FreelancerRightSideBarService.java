package backend.services.freelancer.profile;

import java.util.List;

import backend.dtos.freelancer.profile.client_view.FreelancerRightSideBarClientViewDTO;
import backend.dtos.freelancer.profile.me.FreelancerRightSideBarDTO;
import backend.models.freelancer.profile.FreelancerProfile;
import backend.models.freelancer.profile.FreelancerRightSideBar;

public interface FreelancerRightSideBarService {
	FreelancerRightSideBar getByFreelancerRightSideBar(Long id);
	FreelancerProfile getByFreelancerProfileId(Long id);
	FreelancerRightSideBar save(Long id, FreelancerRightSideBar request);
	FreelancerRightSideBar saveForFreelancer(Long freelancerId, FreelancerRightSideBar request);
	FreelancerRightSideBar update(Long freelancerId, Long id, FreelancerRightSideBarDTO requestDTO);
	FreelancerRightSideBar incrementViewCount(Long clientId, Long freelancerId, Long sideBarId);
	List<FreelancerRightSideBar> getRightSideBar(Long id);
	FreelancerRightSideBarClientViewDTO freelancerRightSideBarClientView(Long id);
	FreelancerRightSideBar me(Long id);
}
