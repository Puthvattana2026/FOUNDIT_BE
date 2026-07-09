package backend.dtos.admin;

import backend.enums.admin.AccountReportStatus;
import lombok.Data;

@Data
public class AccountReportUpdateDTO {
    private AccountReportStatus status;
    private String adminNote;
}
