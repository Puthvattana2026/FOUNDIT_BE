package backend.repositories.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.models.admin.AdminSetting;

@Repository
public interface AdminSettingRepository extends JpaRepository<AdminSetting, Long> {
}
