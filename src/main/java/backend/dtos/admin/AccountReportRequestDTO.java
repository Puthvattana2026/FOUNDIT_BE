package backend.dtos.admin;

import lombok.Data;

@Data
public class AccountReportRequestDTO {
    private String subject;
    private String message;
}
