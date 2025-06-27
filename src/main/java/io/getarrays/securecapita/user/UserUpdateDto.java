package io.getarrays.securecapita.user;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserUpdateDto {
    private List<Long> departmentIds;
}
