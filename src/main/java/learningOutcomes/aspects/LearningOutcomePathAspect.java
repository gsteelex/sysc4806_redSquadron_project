package learningOutcomes.aspects;

import learningOutcomes.LearningOutcome;
import learningOutcomes.repositories.LearningOutcomeRepository;
import learningOutcomes.controllers.requestModels.LearningOutcomeRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@ControllerAdvice
@Aspect
@Configuration
public class LearningOutcomePathAspect {

    public LearningOutcomeRepository loRepository;

    @Autowired
    public LearningOutcomePathAspect (LearningOutcomeRepository loRepository) {
        this.loRepository = loRepository;
    }

    @Around("@annotation(LearningOutcomeRequestValidated) && (args(response, loRequest,..) || args(*, response, loRequest,..))")
    public Object validateLearningOutcomeRequest(ProceedingJoinPoint pjp, LearningOutcomeRequest loRequest, HttpServletResponse response) throws Throwable {
        if (loRequest.getName() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "name must be supplied");

            return null;

        }

        else {

            return pjp.proceed();
        }
    }

    @Around("@annotation(LearningOutcomeExistsValidated) && args(id,loId,response,.. )")
    public Object validateLearningOutcomeExists(ProceedingJoinPoint pjp, Integer id, Integer loId, HttpServletResponse response) throws Throwable {

        Optional<LearningOutcome> learningOutcome = loRepository.findById(loId);

        if (learningOutcome.isPresent()) {
            return pjp.proceed();

        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "learning outcome not found with id: " + loId);
            return null;
        }
    }

}