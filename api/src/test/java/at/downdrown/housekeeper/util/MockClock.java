package at.downdrown.housekeeper.util;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

/**
 * A mock clock that let's you set the time.
 * This is useful if you want to test classes that use the {@code java.time} API.
 *
 * For example you can use {@code LocalDateTime.now(mockClock)} to control what time should be set.
 *
 * @author Manfred Huber
 */
public class MockClock extends Clock {

    private Instant instant;
    private ZoneId zone = ZoneId.systemDefault();

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public MockClock withZone(final ZoneId zone) {
        this.zone = zone;
        return this;
    }

    @Override
    public Instant instant() {
        if (instant != null) {
            return instant.atZone(zone).toInstant();
        } else {
            return Instant.now();
        }
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public void reset() {
        setInstant(null);
    }
}
