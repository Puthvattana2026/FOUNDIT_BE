package backend.mappers;

import java.util.Base64;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import backend.dtos.freelancer.gig.GigCoverImagesDTO;
import backend.dtos.freelancer.gig.GigRequestDTO;
import backend.dtos.freelancer.gig.GigResponseDTO;
import backend.enums.freelancer.ProjectStatusEnum;
import backend.models.freelancer.gig.Gig;
import backend.models.freelancer.gig.GigCoverImage;
import backend.models.freelancer_client.Project;

@Mapper(componentModel = "spring")
public interface GigMapper {
    Gig toGigRequestDTO(GigRequestDTO gigRequestDto);

    @Mapping(target = "coverImages", source = "galleryCoverImages")
    @Mapping(target = "gigId", source = "id")
    @Mapping(target = "freelancerId", source = "freelancer.id")
    @Mapping(target = "freelancerName", source = "freelancer.username")
    @Mapping(target = "views", source = "viewCount")
    @Mapping(target = "orders", expression = "java(countOrders(gigResponse))")
    @Mapping(target = "rating", expression = "java(averageRating(gigResponse))")
    @Mapping(target = "reviews", expression = "java(countReviews(gigResponse))")
    @Mapping(target = "status", expression = "java(gigResponse.getStatus() != null ? gigResponse.getStatus().name().toLowerCase() : null)")
    GigResponseDTO toGigResponse(Gig gigResponse);

    GigCoverImagesDTO toGigCoverImagesDTO(GigCoverImage image);

    default String map(byte[] value) {
        return value == null ? null : Base64.getEncoder().encodeToString(value);
    }

    default Long countOrders(Gig gig) {
        if (gig == null || gig.getProjects() == null) {
            return 0L;
        }

        return gig.getProjects().stream()
                .filter(project -> project.getStatus() != ProjectStatusEnum.CANCELLED)
                .count();
    }

    default Long countReviews(Gig gig) {
        if (gig == null || gig.getProjects() == null) {
            return 0L;
        }

        return gig.getProjects().stream()
                .filter(project -> project.getRating() != null)
                .count();
    }

    default Double averageRating(Gig gig) {
        if (gig == null || gig.getProjects() == null) {
            return 0.0;
        }

        return gig.getProjects().stream()
                .map(Project::getRating)
                .filter(rating -> rating != null)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
}
