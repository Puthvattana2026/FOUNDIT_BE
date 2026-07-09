package backend.services.client.setting;

import org.springframework.security.core.Authentication;

import backend.dtos.freelancer.setting.SettingDTO;

public interface ClientSettingService {
    void changePassword(Authentication auth, SettingDTO request);
}
