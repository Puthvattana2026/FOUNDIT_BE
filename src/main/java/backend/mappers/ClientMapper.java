package backend.mappers;

import java.util.Base64;

import backend.dtos.client.profile.ProfileResponse;
import backend.models.client.profile.Profile;

public interface ClientMapper {

	public Profile toFreelancerProfile(ProfileResponse freelancerProfileDTO);
	public ProfileResponse toFreelancerProfileDTO(Profile freelancerProfile);
    
	default String map(byte[] value) {
		return value == null ? null : Base64.getEncoder().encodeToString(value);
	}
}
