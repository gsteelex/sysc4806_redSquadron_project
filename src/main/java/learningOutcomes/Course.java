package learningOutcomes;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;

@Entity
public class Course {



    @Id
    @GeneratedValue
    private Integer id = null;

    private String name;
	
	private int year;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<LearningOutcome> outcomes;
	
	public Course() {
        this("Default", 1);
    }
	
    public Course(String name, int year) {
        this(name, year, new ArrayList<LearningOutcome>());
    }
	public Course(String name, int year, ArrayList<LearningOutcome> outcomes) {
        this.name = name;
		this.year = year;
        this.outcomes = outcomes;
    }
	
	public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
	public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public LearningOutcome getLearningOutcome(int index) {
        return outcomes.get(index);
    }
    public List<LearningOutcome> getLearningOutcomes() { return outcomes; }
    public int getSize() { return outcomes.size(); }

    public void addLearningOutcome(LearningOutcome c) {
        outcomes.add(c);
    }

    public void removeLearningOutcome(LearningOutcome b) {
        outcomes.remove(b);
    }

    public void removeLearningOutcomeIndex(int index) { outcomes.remove(index); }

    @Override
    public String toString() {
        return String.format(
                "Course[id=%i, name='%s', size='%i']",
                id, name, outcomes.size());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Course)) return false;
        Course ab = (Course) o;
        if (!this.name.equals(ab.getName())) return false;
		if (this.year != ab.getYear()) return false;
        if (outcomes.size() != ab.getSize()) return false;
        for (int i = 0; i < outcomes.size(); i++) {
            if (!outcomes.get(i).equals(ab.getLearningOutcome(i))) return false;
        }
        return true;
    }
}