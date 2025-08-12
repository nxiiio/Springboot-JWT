package cl.duoc.worellana.authjwtspring_security.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegisterRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Valid
    AuthCreateRoleRequest roleRequest;
}
