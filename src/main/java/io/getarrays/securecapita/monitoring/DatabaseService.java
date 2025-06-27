package io.getarrays.securecapita.monitoring;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public class DatabaseService implements HealthIndicator {
   private final String   DATABASE_SERVICE="DatabaseService";
    @Override
    public Health health() {
if(isDatabaseHealthGood()){
    return Health.up().withDetail(DATABASE_SERVICE,"service up").build();

}
        return Health.down().withDetail(DATABASE_SERVICE,"service is down").build();

        }
    private boolean isDatabaseHealthGood() {
        return true;
    }
}