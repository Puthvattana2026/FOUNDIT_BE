package backend.services.ekyc;

import java.io.IOException;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import backend.dtos.ekyc.EkycRequestDTO;
import backend.dtos.ekyc.EkycResponseDTO;
import backend.models.ekyc.EkycForm;

public interface EkycService {
	//getId
	EkycForm getById(Long id);
	//create
	EkycForm save(EkycForm saved);
	//update
	EkycForm step1_update(Long id, EkycRequestDTO step1);
	//update
	EkycForm step2_update_idcard(Long id, MultipartFile frontId, MultipartFile backId) throws IOException;
	//update
	EkycForm step3_update(Long id, EkycRequestDTO step3);
	//getALL
	Optional<EkycResponseDTO> Review(Long id);
}
