package io.getarrays.securecapita.asserts.Confirm;

import java.sql.Timestamp;

public record ConfirmAssertResponseDto(Long id, String asset, String serial, String checkedByName, String checkedByEmail, Timestamp updatedDate) {
}
