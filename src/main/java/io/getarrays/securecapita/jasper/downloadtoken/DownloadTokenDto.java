package io.getarrays.securecapita.jasper.downloadtoken;


import java.util.UUID;

public record DownloadTokenDto(UUID token) {
    public DownloadTokenDto(DownloadToken downloadToken) {
        this(downloadToken.getTokenID());
    }
}
