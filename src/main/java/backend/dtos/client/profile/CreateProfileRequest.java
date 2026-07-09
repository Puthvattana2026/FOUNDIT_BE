package backend.dtos.client.profile;

import lombok.Data;

@Data
public class CreateProfileRequest {
    private String workLocation;
    private String about;
}
