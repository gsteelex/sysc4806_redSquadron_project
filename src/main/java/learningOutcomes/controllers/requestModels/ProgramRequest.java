package learningOutcomes.controllers.requestModels;

import java.util.List;

/**
 * Class to capture the request made to create or update a  program.
 */
public class ProgramRequest {

    private String name;
    private List<Integer> courses;

    public ProgramRequest() {
        //empty so Spring will not end up with default values, as courses is optional in requests
    }

    public ProgramRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getCourses() {
        return courses;
    }

    public void setCourses(List<Integer> courses) {
        this.courses = courses;
    }
}
