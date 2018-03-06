package learningOutcomes.controllers.requestModels;

import java.util.*;
import learningOutcomes.LearningOutcome;

public class CategoryRequest {

    private String name;
    private ArrayList<Integer> outcomes;

    public CategoryRequest() {

    }

    public CategoryRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getLearningOutcomes() {
        return outcomes;
    }

    public void setLearningOutcomes(ArrayList<Integer> outcomes) {
        this.outcomes = outcomes;
    }
}
