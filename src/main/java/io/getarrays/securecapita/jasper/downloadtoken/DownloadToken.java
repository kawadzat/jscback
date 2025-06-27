package io.getarrays.securecapita.jasper.downloadtoken;

import io.getarrays.securecapita.asserts.model.Station;
import io.getarrays.securecapita.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "download_tokens")
public class DownloadToken {
    @Id
    private UUID tokenID;

    private boolean isUsed;
    private boolean assertStat;

    private boolean singleStation;

    @Transient
    private boolean valid;

    @ManyToOne
    private User creator;

    private Timestamp createdDate;

    private Timestamp expiryDate;

    private Timestamp usedDate;
}
