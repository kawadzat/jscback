package io.getarrays.securecapita.resource;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto {
    @NotNull
    @NotEmpty
    private String token;
    @NotNull
    @NotEmpty
    private String confirmPassword;
    @NotNull
    @NotEmpty
    private String password;

}
