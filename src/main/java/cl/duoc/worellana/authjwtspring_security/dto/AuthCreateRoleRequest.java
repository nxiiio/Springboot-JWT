package cl.duoc.worellana.authjwtspring_security.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class AuthCreateRoleRequest {
    @Size(max = 2, message = "The user cannot have more than 2 roles")
    private List<String> roles;
}
