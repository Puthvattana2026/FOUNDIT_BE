package backend.services.impl.freelancer.profile;

import java.util.List;

import org.springframework.stereotype.Service;

import backend.dtos.freelancer.profile.client_view.FreelancerRightSideBarClientViewDTO;
import backend.dtos.freelancer.profile.me.FreelancerRightSideBarDTO;
import backend.exceptions.ResourceNotFoundException;
import backend.models.authentication.Freelancer;
import backend.models.freelancer.profile.FreelancerProfile;
import backend.models.freelancer.profile.FreelancerRightSideBar;
import backend.repositories.authentication.ClientRepository;
import backend.repositories.authentication.FreelancerRepository;
import backend.repositories.freelancer.profile.FreelancerProfileRepository;
import backend.repositories.freelancer.profile.FreelancerRightSideBarRepository;
import backend.services.freelancer.profile.FreelancerRightSideBarService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FreelancerRightSideBarServiceImpl implements FreelancerRightSideBarService {
	
	private final FreelancerRightSideBarRepository freelancerRightSideBarRepository;
	private final FreelancerProfileRepository freelancerProfileRepository;
	private final FreelancerRepository freelancerRepository;
	private final ClientRepository clientRepository;

	@Override
	public FreelancerProfile getByFreelancerProfileId(Long id) {
		return freelancerProfileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Freelancer Profile", id));
	}
	
	@Override
	public FreelancerRightSideBar getByFreelancerRightSideBar(Long id) {
		return freelancerRightSideBarRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Freelancer Right Side Bar", id));
	}
	
	@Override
	public FreelancerRightSideBar save(Long id, FreelancerRightSideBar request) {
		FreelancerProfile freelancerProfile = getByFreelancerProfileId(id);
		FreelancerRightSideBar rightSideBar = freelancerRightSideBarRepository
				.findFirstByFreelancerProfileId(freelancerProfile.getId())
				.orElse(request);
		rightSideBar.setFreelancerProfile(freelancerProfile);
		rightSideBar.setStartPrice(request.getStartPrice());
		rightSideBar.setViewCount(request.getViewCount());
		freelancerProfile.setRightSideCard(rightSideBar);
		return freelancerRightSideBarRepository.save(rightSideBar);
	}

	@Transactional
	@Override
	public FreelancerRightSideBar saveForFreelancer(Long freelancerId, FreelancerRightSideBar request) {
		Freelancer freelancer = freelancerRepository.findById(freelancerId)
				.orElseThrow(() -> new ResourceNotFoundException("Freelancer", freelancerId));
		FreelancerProfile profile = getOrCreateProfile(freelancer);
		return save(profile.getId(), request);
	}

	@Override
	public FreelancerRightSideBar update(Long freelancerId, Long id, FreelancerRightSideBarDTO requestDTO) {
		Freelancer freelancer = freelancerRepository.findById(freelancerId).orElseThrow(() -> new ResourceNotFoundException("Freelancer", freelancerId));
		FreelancerProfile profile = freelancer.getFreelancerProfiles();
		FreelancerRightSideBar rightSideBar = freelancerRightSideBarRepository
											  .findByIdAndFreelancerProfileId(id, profile.getId())
											  .orElseThrow(() -> new ResourceNotFoundException("Freelancer Right Side Bar", id));
		rightSideBar.setStartPrice(requestDTO.getStartPrice());
		rightSideBar.setViewCount(requestDTO.getViewCount());
		return freelancerRightSideBarRepository.save(rightSideBar);
	}
	
	@Override
	public FreelancerRightSideBar incrementViewCount(Long clientId, Long freelancerId, Long sideBarId) {
	    // Validate client exists
	    clientRepository.findById(clientId)
	        .orElseThrow(() -> new ResourceNotFoundException("Client", clientId));

	    // Get freelancer and their profile
	    Freelancer freelancer = freelancerRepository.findById(freelancerId)
	        .orElseThrow(() -> new ResourceNotFoundException("Freelancer", freelancerId));

	    FreelancerProfile profile = freelancer.getFreelancerProfiles();

	    FreelancerRightSideBar rightSideBar = freelancerRightSideBarRepository
	        .findByIdAndFreelancerProfileId(sideBarId, profile.getId())
	        .orElseThrow(() -> new ResourceNotFoundException("Freelancer Right Side Bar", sideBarId));

	    // Increment view count automatically — client doesn't set this
	    rightSideBar.setViewCount(rightSideBar.getViewCount() + 1);

	    return freelancerRightSideBarRepository.save(rightSideBar);
	}

	@Override
	public List<FreelancerRightSideBar> getRightSideBar(Long id) {
		if(!freelancerProfileRepository.existsById(id)) {
			throw new ResourceNotFoundException("Freelancer Profile", id);
		}
		return freelancerRightSideBarRepository.findByFreelancerProfileId(id);
	}

	// Client View
	@Override
	public FreelancerRightSideBarClientViewDTO freelancerRightSideBarClientView(Long id) {
		FreelancerRightSideBar freelancerRightSideBar = getByFreelancerRightSideBar(id);
		FreelancerRightSideBarClientViewDTO freelancerRightSideBarClientViewDTO = new FreelancerRightSideBarClientViewDTO();
		freelancerRightSideBarClientViewDTO.setId(freelancerRightSideBar.getId());
		freelancerRightSideBarClientViewDTO.setStartPrice(freelancerRightSideBar.getStartPrice());
		freelancerRightSideBarClientViewDTO.setViewCount(freelancerRightSideBar.getViewCount());
		return freelancerRightSideBarClientViewDTO;
	}

	// Freelancer View
	@Transactional
	@Override
	public FreelancerRightSideBar me(Long id) {
		Freelancer freelancer = freelancerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Freelancer", id));
		
		FreelancerProfile profile = getOrCreateProfile(freelancer);
		
		FreelancerRightSideBar rightSideBar = freelancerRightSideBarRepository
				.findFirstByFreelancerProfileId(profile.getId())
				.orElseGet(() -> {
					FreelancerRightSideBar newRightSideBar = new FreelancerRightSideBar();
					newRightSideBar.setFreelancerProfile(profile);
					newRightSideBar.setViewCount(0L);
					profile.setRightSideCard(newRightSideBar);
					return freelancerRightSideBarRepository.save(newRightSideBar);
				});
	
		return rightSideBar;
	}

	private FreelancerProfile getOrCreateProfile(Freelancer freelancer) {
		FreelancerProfile profile = freelancer.getFreelancerProfiles();
		if (profile != null) {
			return profile;
		}

		profile = new FreelancerProfile();
		profile.setFreelancer(freelancer);
		freelancer.setFreelancerProfiles(profile);
		return freelancerProfileRepository.save(profile);
	}
}
