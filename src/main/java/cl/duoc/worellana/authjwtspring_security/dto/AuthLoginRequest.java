package cl.duoc.worellana.authjwtspring_security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthLoginRequest {
    @NotBlank
    String username;

    @NotBlank
    String password;
}
