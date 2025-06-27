package io.getarrays.securecapita.laptoplist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Laptop {
    private Long id;
    private String manufacturer;
    private String model;
    // Add other fields as needed

    public static Laptop parseDeviceString(String deviceStr) {
        String manufacturer = "";
        String model = "";

        if (deviceStr != null && !deviceStr.trim().isEmpty()) {
            String[] parts = deviceStr.split(" - ", 2);
            manufacturer = parts[0];
            model = parts.length > 1 ? parts[1] : "";
        }

        return Laptop.builder()
                .manufacturer(manufacturer)
                .model(model)
                .build();
    }
} 