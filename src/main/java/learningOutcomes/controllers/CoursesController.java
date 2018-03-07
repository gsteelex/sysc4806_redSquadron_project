package learningOutcomes.controllers;

import learningOutcomes.Course;
import learningOutcomes.LearningOutcome;
import learningOutcomes.aspects.CourseRequestValidated;
import learningOutcomes.aspects.ProgramNameValidated;
import learningOutcomes.controllers.requestModels.CourseRequest;
import learningOutcomes.repositories.CourseRepository;
import learningOutcomes.repositories.LearningOutcomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


/**
 * Controller for the "/courses" and "/courses/{id}" paths to interact with domain objects.
 */
@RestController
@RequestMapping(value="/courses")
public class CoursesController {

    private CourseRepository courseRepository;
    private LearningOutcomeRepository learningOutcomeRepository;

    @Autowired
    public CoursesController(CourseRepository courseRepository, LearningOutcomeRepository learningOutcomeRepository) {
        this.courseRepository = courseRepository;
        this.learningOutcomeRepository = learningOutcomeRepository;
    }


    /**
     * Controller endpoint to list all Courses.
     * @return a JSON array of Program objects when the response is mapped by Spring
     */
    @RequestMapping(method= RequestMethod.GET)
    public Iterable<Course> getAllCourses() {
        return courseRepository.findAll();
    }


    /**
     * Controller endpoint to create a Course. LearningOutcomes are optional, but name and year is validated by middleware.
     * May respond with SC_NOT_FOUND when courses are provided and a LearningOutcome does not exist.
     * @param response used to send error responses
     * @param courseRequest the Course to create
     * @return a Program object when the response is mapped by Spring.
     * @throws IOException
     */
    @CourseRequestValidated
    @RequestMapping(method= RequestMethod.POST)
    public Course createCourse(HttpServletResponse response, @RequestBody CourseRequest courseRequest) throws IOException {
        Course course = new Course(courseRequest.getName(), courseRequest.getYear());

        if (courseRequest.getLearningOutcomes() == null) {
            //if no courses are provided (optional attribute), create with empty course list
            course = new Course(courseRequest.getName(), courseRequest.getYear());

        } else {

            //for each course requested, determine if the course exists
            for (Integer learningOutcomeId: courseRequest.getLearningOutcomes()) {
                Optional<LearningOutcome> learningOutcome = learningOutcomeRepository.findById(learningOutcomeId);

                if (learningOutcome.isPresent()) {
                    course.addLearningOutcome(learningOutcome.get());

                } else {
                    //if the course was not found in the database, respond with an error
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "could not find LearningOutcome with id: " + learningOutcomeId);
                    //must return null to propagate error response mapping by Spring
                    return null;
                }
            }
        }

        //save to the database
        courseRepository.save(course);

        return course;
    }
}
