package backend.mappers;

import org.mapstruct.Mapper;

import backend.dtos.authentication.RegisterRequestDTO;
import backend.dtos.authentication.RegisterResponseDTO;
import backend.models.authentication.Register;
	
@Mapper(componentModel = "spring")
public interface RegisterMapper {
	public Register toRegister (RegisterRequestDTO registerDto);
	public RegisterResponseDTO toRegisterResponseDto (Register register);
}
