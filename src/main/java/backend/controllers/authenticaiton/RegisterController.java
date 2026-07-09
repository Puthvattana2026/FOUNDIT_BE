package backend.controllers.authenticaiton;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.dtos.authentication.RegisterRequestDTO;
import backend.exceptions.ErrorResponseException;
import backend.mappers.RegisterMapper;
import backend.models.authentication.Register;
import backend.services.authentication.RegisterService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@CrossOrigin(value = "${frontend.url}")
@RequiredArgsConstructor
public class RegisterController {
	
	private final RegisterMapper mapper;
	private final RegisterService registerServiceImpl;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody(required = false) RegisterRequestDTO registerDto){
		if(registerDto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponseException(HttpStatus.BAD_REQUEST, "Request body is required"));
		}
		Register registerUser = mapper.toRegister(registerDto);
		
		registerUser = registerServiceImpl.register(registerUser);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(mapper.toRegisterResponseDto(registerUser));
 	}

}
