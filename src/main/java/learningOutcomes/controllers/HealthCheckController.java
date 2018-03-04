package learningOutcomes.controllers;

import learningOutcomes.HealthCheck;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Basic controller for a health-check endpoint
 */
@RestController
public class HealthCheckController {

    @RequestMapping(value="/healthCheck", method= RequestMethod.GET)
    public HealthCheck healthCheck() {
        return new HealthCheck(true);
    }

}
