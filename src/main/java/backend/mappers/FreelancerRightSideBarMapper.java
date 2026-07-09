package backend.mappers;

import org.mapstruct.Mapper;

import backend.dtos.freelancer.profile.me.FreelancerRightSideBarDTO;
import backend.models.freelancer.profile.FreelancerRightSideBar;

@Mapper(componentModel = "spring")
public interface FreelancerRightSideBarMapper {
	FreelancerRightSideBar toFreelancerRightSideBar(FreelancerRightSideBarDTO freelancerRightSideBarDTO);
	FreelancerRightSideBarDTO toFreelancerRightSideBarDTO(FreelancerRightSideBar freelancerRightSideBar);
}
