package learningOutcomes;

import javax.persistence.*;

@Entity
public class LearningOutcome {

    @Id
    @GeneratedValue
    private Integer id = null;
    private String name;

    private Integer category;

    public LearningOutcome() {
        this("Default", null);
    }

    public LearningOutcome(String n, Integer c) {
        name = n;
        category = c;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategory() { return category; }

    public void setCategory(Integer category) { this.category = category; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LearningOutcome)) return false;
        LearningOutcome b = (LearningOutcome) o;
        if (!name.equals(b.getName())) return false;
        if (category == null && b.getCategory() != null || category != null && b.getCategory() == null) return false;
        if (category == null && b.getCategory() == null) return true;
        if (!category.equals(b.getCategory())) return false;
        return true;
    }
}