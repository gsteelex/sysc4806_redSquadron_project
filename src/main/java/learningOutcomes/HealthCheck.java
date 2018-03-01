package learningOutcomes;

public class HealthCheck {

    private boolean healthy;

    public HealthCheck(boolean healthy) {
        this.healthy = healthy;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }
}
