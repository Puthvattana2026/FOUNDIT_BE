package backend.repositories.freelancer_client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.models.freelancer_client.ProjectRequirementProposal;

@Repository
public interface ProjectRequirementProposalRepository extends JpaRepository<ProjectRequirementProposal, Long> {
}
