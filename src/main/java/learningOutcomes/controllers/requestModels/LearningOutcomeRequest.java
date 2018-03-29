package learningOutcomes.controllers.requestModels;

import java.util.List;

public class LearningOutcomeRequest {

    private String name;
    private Integer category;

    public LearningOutcomeRequest() {

    }

    public LearningOutcomeRequest(String name, Integer category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
}
