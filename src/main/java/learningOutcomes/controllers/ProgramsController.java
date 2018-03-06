package learningOutcomes.controllers;


import learningOutcomes.*;
import learningOutcomes.aspects.ProgramExistsValidated;
import learningOutcomes.aspects.ProgramNameValidated;
import learningOutcomes.controllers.requestModels.ProgramRequest;
import learningOutcomes.repositories.CourseRepository;
import learningOutcomes.repositories.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the "/programs" and "/programs/{id}" paths to interact with domain objects.
 */
@RestController
@RequestMapping(value="/programs")
public class ProgramsController {

    private ProgramRepository programRepository;
    private CourseRepository courseRepository;

    @Autowired
    public ProgramsController(ProgramRepository programRepository, CourseRepository courseRepository) {
        this.programRepository = programRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Controller endpoint to list all Programs.
     * @return a JSON array of Program objects when the response is mapped by Spring
     */
    @RequestMapping(method= RequestMethod.GET)
    public Iterable<Program> getAllPrograms() {
        return programRepository.findAll();
    }

    /**
     * Controller endpoint to create a Program. Courses are optional, but name is validated by middleware.
     * May respond with SC_NOT_FOUND when courses are provided and a course does not exist.
     * @param response used to send error responses
     * @param programRequest the Program to create
     * @return a Program object when the response is mapped by Spring.
     * @throws IOException
     */
    @ProgramNameValidated
    @RequestMapping(method= RequestMethod.POST)
    public Program createProgram(HttpServletResponse response, @RequestBody ProgramRequest programRequest) throws IOException {
        Program program;

        if (programRequest.getCourses() == null) {
            //if no courses are provided (optional attribute), create with empty course list
            program = new Program(programRequest.getName());

        } else {
            List<Course> courses = new ArrayList<>();

            //for each course requested, determine if the course exists
            for (Integer courseId: programRequest.getCourses()) {
                Optional<Course> course = courseRepository.findById(courseId);

                if (course.isPresent()) {
                    courses.add(course.get());

                } else {
                    //if the course was not found in the database, respond with an error
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "could not find course with id: " + courseId);
                    //must return null to propagate error response mapping by Spring
                    return null;
                }
            }

            program = new Program(programRequest.getName(), courses);
        }

        //save to the database
        programRepository.save(program);

        return program;
    }

    /**
     * Controller method to fetch a program by ID.
     * @param id the ID of the Program to fetch
     * @param response this is included as it is requred to be findable by the advice mapped to "@ProgramExistsValidated"
     * @return the fetched Program
     * @throws IOException
     */
    @ProgramExistsValidated
    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    public Program getProgramById(@PathVariable("id") Integer id, HttpServletResponse response) throws IOException {
        Optional<Program> program = programRepository.findById(id);
        return program.get();
    }

    /**
     * Controller method to delete a program by its ID, returns SC_NO_CONTENT when no Program existed
     * @param id the ID of the Program to fetch
     * @param response used to send an error response
     * @return the deleted Program or an empty body
     */
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public Program deleteProgramById(@PathVariable("id") Integer id, HttpServletResponse response) {
        Optional<Program> program = programRepository.findById(id);

        if (program.isPresent()) {
            Program programToReturn = program.get();
            programRepository.delete(program.get());
            return programToReturn;

        } else {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            //null for empty response body
            return null;
        }
    }

    /**
     * Controller method to update a specific Program. Courses are optional, but name is validated by middleware.
     * Returns SC_NOT_FOUND error when a course provided does not exist.
     * @param id the ID of the Program to update
     * @param response used to send an error response
     * @param programRequest the body of the HTTP request, the values to update the Program to have
     * @return the updated program
     * @throws IOException
     */
    @ProgramNameValidated
    @ProgramExistsValidated
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public Program updateProgramById(@PathVariable("id") Integer id, HttpServletResponse response, @RequestBody ProgramRequest programRequest) throws IOException {
        Optional<Program> program = programRepository.findById(id);

        //fetch program and modify the name, name has been validated as an attribute by middleware
        Program programToModify = program.get();
        programToModify.setName(programRequest.getName());


        //check if courses (optional attribute) have been provided
        if (programRequest.getCourses() != null) {
            List<Course> courses = new ArrayList<>();

            //for each course requested, determine if the course exists
            for (Integer courseId: programRequest.getCourses()) {
                Optional<Course> course = courseRepository.findById(courseId);

                if (course.isPresent()) {
                    courses.add(course.get());

                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "could not find course with id: " + courseId);
                    //must return null to propagate error response mapping by Spring
                    return null;
                }
            }

            programToModify.setCourses(courses);
        }

        programRepository.save(programToModify);

        return programToModify;
    }

}
