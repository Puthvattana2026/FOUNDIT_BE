package backend.services.freelancer.profile;

import java.util.List;

import backend.dtos.freelancer.profile.me.FreelancerReviewDTO;

public interface FreelancerReviewService {
	List<FreelancerReviewDTO> getByFreelancer(Long freelancerId);
	FreelancerReviewDTO create(Long freelancerId, Long clientId, String clientName, FreelancerReviewDTO request);
	void deleteOwnReview(Long freelancerId, Long reviewId);
}
