package learningOutcomes.aspects;

import learningOutcomes.Program;
import learningOutcomes.repositories.ProgramRepository;
import learningOutcomes.controllers.requestModels.ProgramRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Class to define advice for REST controller methods.
 */
@ControllerAdvice
@Aspect
@Configuration
public class ProgramPathAspect {

    private ProgramRepository programRepository;

    @Autowired
    public ProgramPathAspect(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    /**
     * Advice method to validate an incoming ProgramRequest has a name field.
     * @param pjp proceeding join point used to continue request handling chain
     * @param programRequest the request body to validate
     * @param response the response that can be used to send an error
     * @return the result of the request
     * @throws Throwable
     */
    @Around("@annotation(ProgramNameValidated) && (args(response, programRequest,..) || args(*, response, programRequest,..))")
    public Object validateProgramRequest(ProceedingJoinPoint pjp, ProgramRequest programRequest, HttpServletResponse response) throws Throwable {
        if (programRequest.getName() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "name must be supplied");
            //need to return null here for Spring to correctly return the error response
            return null;
        } else {
            //need to return the value from farther down the chain (eventually the controller) for response body
            return pjp.proceed();
        }
    }

    /**
     * Advice method to validate that a program with a specific ID exists.
     * @param pjp proceeding join point used to continue request handling chain
     * @param id the ID of the Program to look for
     * @param response the response that can be used to send an error
     * @return the result of the request
     * @throws Throwable
     */
    @Around("@annotation(ProgramExistsValidated) && args(id,response,.. )")
    public Object validateProgramExists(ProceedingJoinPoint pjp, Integer id, HttpServletResponse response) throws Throwable {

        Optional<Program> program = programRepository.findById(id);

        if (program.isPresent()) {
            //need to return the value from farther down the chain (eventually the controller) for response body
            return pjp.proceed();

        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "program not found with id: " + id);
            //need to return null here for Spring to correctly return the error response
            return null;
        }
    }
}
