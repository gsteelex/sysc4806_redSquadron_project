package learningOutcomes.controllers.requestModels;

import java.util.List;

/**
 * Class to capture the structure of the create Course request body.
 */
public class CourseRequest {

    private String name;
    private Integer year;
    private List<Integer> learningOutcomes;

    public CourseRequest() {
        //needs to be empty for Spring. No defaults as learningOutcomes is optional
    }

    public CourseRequest(String name, Integer year, List<Integer> learningOutcomes) {
        this.name = name;
        this.year = year;
        this.learningOutcomes = learningOutcomes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Integer> getLearningOutcomes() {
        return learningOutcomes;
    }

    public void setLearningOutcomes(List<Integer> learningOutcomes) {
        this.learningOutcomes = learningOutcomes;
    }
}
