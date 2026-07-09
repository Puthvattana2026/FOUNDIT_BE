package backend.mappers;

import org.mapstruct.Mapper;

import backend.dtos.ekyc.EkycRequestDTO;
import backend.dtos.ekyc.EkycResponseDTO;
import backend.models.ekyc.EkycForm;

@Mapper(componentModel = "spring")
public interface EkycMapper {
	public EkycForm toEkycForm(EkycRequestDTO ekycRequestDTO);
	public EkycResponseDTO toEkycResponseDTO(EkycForm ekycForm);
}
