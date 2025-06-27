package io.getarrays.securecapita.asserts.dto;

import io.getarrays.securecapita.asserts.model.AssertEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StationAssertsDto {

    private Long id;

    private String name;

    private int assertCount;

    private List<AssertEntity> asserts;

}
