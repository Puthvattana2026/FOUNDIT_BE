package backend.mappers;

import org.mapstruct.Mapper;

import backend.dtos.freelancer.profile.me.FreelancerExperienceDTO;
import backend.models.freelancer.profile.FreelancerExperience;

@Mapper(componentModel = "spring")
public interface FreelancerExperienceMapper {
	public FreelancerExperience toFreelancerExperiece(FreelancerExperienceDTO freelancerExperienceDTO);
	public FreelancerExperienceDTO toFreelancerExperienceDTO(FreelancerExperience freelancerExperiece);
}
