package learningOutcomes.aspects;


import learningOutcomes.Course;
import learningOutcomes.controllers.requestModels.CourseRequest;
import learningOutcomes.repositories.CourseRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Class to define advice for REST controller methods under path "/courses".
 */
@ControllerAdvice
@Aspect
@Configuration
public class CoursePathAspect {

    private CourseRepository courseRepository;

    @Autowired
    public CoursePathAspect(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Advice method to validate an incoming CourseRequest has name and year fields.
     * @param pjp proceeding join point used to continue request handling chain
     * @param courseRequest the request body to validate
     * @param response the response that can be used to send an error
     * @return the result of the request
     * @throws Throwable
     */
    @Around("@annotation(CourseRequestValidated) && (args(response, courseRequest,..) || args(*, response, courseRequest,..))")
    public Object validateCourseRequest(ProceedingJoinPoint pjp, CourseRequest courseRequest, HttpServletResponse response) throws Throwable {
        if (courseRequest.getName() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "name must be supplied");
            //need to return null here for Spring to correctly return the error response
            return null;

        } else if (courseRequest.getYear() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "year must be supplied");
            //need to return null here for Spring to correctly return the error response
            return null;

        } else {
            //need to return the value from farther down the chain (eventually the controller) for response body
            return pjp.proceed();
        }
    }

    /**
     * Advice method to validate a Course with the given ID exists.
     * @param pjp proceeding join point used to continue request handling chain
     * @param id the ID of the Course to validate
     * @param response the response that can be used to send an error
     * @return the result of the request
     * @throws Throwable
     */
    @Around("@annotation(CourseExistsValidated) && (args(id, response,..))")
    public Object validateCourseExists(ProceedingJoinPoint pjp, Integer id, HttpServletResponse response) throws Throwable {
        Optional<Course> course = courseRepository.findById(id);

        if (course.isPresent()) {
            //need to return the value from farther down the chain (eventually the controller) for response body
            return pjp.proceed();

        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "course not found with id: " + id);
            //need to return null here for Spring to correctly return the error response
            return null;
        }
    }
}
