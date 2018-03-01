package learningOutcomes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HealthCheckTest {

    @Test
    public void testHealthCheck() {
        HealthCheck healthCheck = new HealthCheck(false);

        assertEquals(false, healthCheck.isHealthy());
    }

    @Test
    public void testSetHealthy() {
        HealthCheck healthCheck = new HealthCheck(false);
        healthCheck.setHealthy(true);

        assertEquals(true, healthCheck.isHealthy());
    }

}
