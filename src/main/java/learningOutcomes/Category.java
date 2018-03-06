package learningOutcomes;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@Entity
public class Category {



    @Id
    @GeneratedValue
    private Integer id = null;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<LearningOutcome> outcomes;
	
	public Category() {
        this("Default");
    }
	
    public Category(String name) {
        this(name, new ArrayList<LearningOutcome>());
    }
	public Category(String name, ArrayList<LearningOutcome> outcomes) {
        this.name = name;
        this.outcomes = outcomes;
    }
	
	public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LearningOutcome getLearningOutcome(int index) {
        return outcomes.get(index);
    }
    public List<LearningOutcome> getLearningOutcomes() { return outcomes; }
    public void setLearningOutcomes(List<LearningOutcome> outcomes) {
        this.outcomes = outcomes;
    }
    public int getSize() { return outcomes.size(); }

    public void addLearningOutcome(LearningOutcome c) {
        outcomes.add(c);
    }

    public void removeLearningOutcome(LearningOutcome b) {
        outcomes.remove(b);
    }

    public void removeLearningOutcomeIndex(int index) { outcomes.remove(index); }

    public void printCategory() {
        System.out.println("Category " + name + " contains " + outcomes.size() + " outcomes(s).");
        for (int i = 0; i < outcomes.size(); i++) {
            LearningOutcome c = outcomes.get(i);
            System.out.println(c.toString());
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Category[id=%i, name='%s', size='%i']",
                id, name, outcomes.size());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Category)) return false;
        Category ab = (Category) o;
        if (!this.name.equals(ab.getName())) return false;
        if (outcomes.size() != ab.getSize()) return false;
        for (int i = 0; i < outcomes.size(); i++) {
            if (!outcomes.get(i).equals(ab.getLearningOutcome(i))) return false;
        }
        return true;
    }
}