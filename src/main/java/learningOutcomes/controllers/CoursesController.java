package learningOutcomes.controllers;

import learningOutcomes.Course;
import learningOutcomes.LearningOutcome;
import learningOutcomes.aspects.CourseExistsValidated;
import learningOutcomes.aspects.CourseRequestValidated;
import learningOutcomes.controllers.requestModels.CourseRequest;
import learningOutcomes.repositories.CourseRepository;
import learningOutcomes.repositories.LearningOutcomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Controller method to delete a Course by its ID, returns SC_NO_CONTENT when no Course existed
     * @param id the ID of the Course to delete
     * @param response used to send SC_NO_CONTENT
     * @return the deleted Course or null if the course did not exist
     */
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public Course deleteCourseById(@PathVariable("id") Integer id, HttpServletResponse response) {
        Optional<Course> course = courseRepository.findById(id);

        if (course.isPresent()) {
            Course courseToReturn = course.get();
            courseRepository.delete(course.get());
            return courseToReturn;

        } else {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            //null for empty response body
            return null;
        }
    }

    /**
     * Controller method to fetch a Course with a given ID.
     * @param id the ID of the Course to fetch
     * @param response not used in this method, but required to be referenced for access by the middleware
     * @return the Course found
     */
    @CourseExistsValidated
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public Course getCourseById(@PathVariable("id") Integer id, HttpServletResponse response) {
        return courseRepository.findById(id).get();
    }

    /**
     * Controller method to update a course with a given ID.
     * @param id the ID of the Course to update
     * @param response used to send an error response
     * @param courseRequest the body of the HTTP request, the values to update the Course to have
     * @return the updated Course
     */
    @CourseRequestValidated
    @CourseExistsValidated
    @RequestMapping(value="/{id}", method = RequestMethod.PATCH)
    public Course updateCourseById(@PathVariable("id") Integer id, HttpServletResponse response, @RequestBody CourseRequest courseRequest) throws IOException {
        Optional<Course> course = courseRepository.findById(id);

        //fetch course and modify the name and year, they have been validated as attributes by middleware
        Course courseToModify = course.get();
        courseToModify.setName(courseRequest.getName());
        courseToModify.setYear(courseRequest.getYear());

        //check if learningOutcomes (optional attribute) have been provided
        if (courseRequest.getLearningOutcomes() != null) {
            List<LearningOutcome> learningOutcomes = new ArrayList<>();

            //for each course requested, determine if the course exists
            for (Integer learningOutcomeId: courseRequest.getLearningOutcomes()) {
                Optional<LearningOutcome> learningOutcome = learningOutcomeRepository.findById(learningOutcomeId);

                if (learningOutcome.isPresent()) {
                    learningOutcomes.add(learningOutcome.get());

                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "could not find learningOutcome with id: " + learningOutcomeId);
                    //must return null to propagate error response mapping by Spring
                    return null;
                }
            }

            courseToModify.setOutcomes(learningOutcomes);
        }

        courseRepository.save(courseToModify);

        return courseToModify;
    }
}
