package backend.services.impl.client.setting;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import backend.dtos.freelancer.setting.SettingDTO;
import backend.exceptions.ApiException;
import backend.exceptions.ResourceNotFoundException;
import backend.models.authentication.Client;
import backend.models.authentication.Register;
import backend.repositories.authentication.ClientRepository;
import backend.repositories.authentication.RegisterRepository;
import backend.services.client.setting.ClientSettingService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientSettingServiceImpl implements ClientSettingService {

    private final ClientRepository clientRepository;
    private final RegisterRepository registerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(Authentication auth, SettingDTO request) {
        if (auth == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (request == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Request body is required");
        }

        String email = auth.getName();
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        if (request.getCurrentPassword() == null || request.getCurrentPassword().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Current password is required");
        }

        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "New password is required");
        }

        Register register = client.getRegister();
        if (register == null) {
            throw new ResourceNotFoundException("Client login account not found");
        }

        String storedPassword = register.getPassword();
        if (!passwordEncoder.matches(request.getCurrentPassword(), storedPassword)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }

        if (passwordEncoder.matches(request.getNewPassword(), storedPassword)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "New password must be different from current password");
        }

        register.setPassword(passwordEncoder.encode(request.getNewPassword()));
        registerRepository.save(register);
    }
}
