package netlife.devmasters.booking.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserLoginDto {
    private String username;
    private String password;
    private String rol;
}
