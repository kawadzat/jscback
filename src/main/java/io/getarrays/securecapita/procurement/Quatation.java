package io.getarrays.securecapita.procurement;

import jakarta.persistence.Lob;

public class Quatation {

    private String name;
    private String type;
    // @Column(name = "profileImage", nullable = false, columnDefinition =
    // "BINARY(256)", length = 256)
    @Lob
    private byte[] profileImage;

}
