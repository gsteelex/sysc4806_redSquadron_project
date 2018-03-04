package learningOutcomes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class LearningOutcome {

    @Id
    @GeneratedValue
    private Integer id = null;
    private String name;

    @ManyToOne
    private Category category;

    public LearningOutcome() {
        this("Default", new Category());
    }

    public LearningOutcome(String n, Category c) {
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

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LearningOutcome)) return false;
        LearningOutcome b = (LearningOutcome) o;
        if (!name.equals(b.getName())) return false;
        if (!category.equals(b.getCategory())) return false;
        return true;
    }
}