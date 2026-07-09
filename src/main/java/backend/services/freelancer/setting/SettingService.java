package backend.services.freelancer.setting;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import backend.dtos.freelancer.setting.SettingDTO;
import backend.models.freelancer.setting.Setting;

public interface SettingService {
    Setting createSetting(Authentication auth, MultipartFile file);
    Setting updateAvatar(Authentication auth, Long settingId, MultipartFile file);
    Setting uploadBankQr(Authentication auth, MultipartFile file);
    Setting getMySetting(Authentication auth);
    void changePassword(Authentication auth, SettingDTO request);
}
