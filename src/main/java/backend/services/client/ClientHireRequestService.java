package backend.services.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import backend.dtos.client.CreateHireRequestDTO;
import backend.dtos.client.ProjectRequirementDTO;
import backend.dtos.client.ProjectRequirementProposalResponse;
import backend.dtos.freelancer_client.HireRequestDTO;
import backend.models.freelancer_client.HireRequest;
import backend.models.freelancer_client.Project;

public interface ClientHireRequestService {
	HireRequest createHireRequest(Authentication auth, CreateHireRequestDTO dto) throws IOException;
	HireRequest cancelHireRequest(Authentication auth, Long hireRequestId);
    List<HireRequest> getMyHireRequests(Authentication auth);
    List<HireRequestDTO> getMyHireRequestDtos(Authentication auth);
    List<Project> getMyProjects(Authentication auth);
    ResponseEntity<byte[]> downloadRequirementFile(Authentication auth, Long requestId);
    HireRequest uploadRequirementFile(Authentication auth, Long requestId, MultipartFile file) throws IOException;
    ResponseEntity<byte[]> downloadDeliveryFileForClient(Authentication auth, Long projectId);
    Project approveDelivery(Authentication auth, Long projectId);
    Project requestRevision(Authentication auth, Long projectId, String revisionMessage);
    Project proposeProjectPrice(Authentication auth, Long requestId, BigDecimal proposedPrice);
    Project acceptProjectPrice(Authentication auth, Long requestId, BigDecimal agreedPrice);
    ProjectRequirementProposalResponse updateProjectRequirement(Authentication auth, Long projectId, ProjectRequirementDTO dto) throws IOException;
    HireRequest rejectProjectPrice(Authentication auth, Long requestId);
}
