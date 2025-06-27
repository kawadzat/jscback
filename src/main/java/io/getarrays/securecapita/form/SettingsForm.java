package io.getarrays.securecapita.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (https://getarrays.io)
 * @since 3/17/2023
 */

@Getter
@Setter
public class SettingsForm {
    @NotNull(message = "Enabled cannot be null or empty")
    private Boolean enabled;
    @NotNull(message = "Not Locked cannot be null or empty")
    private Boolean notLocked;
}
