package learningOutcomes;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;

@Entity
public class Program {



    @Id
    @GeneratedValue
    private Integer id = null;

    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Course> courses;
	
	public Program() {
        this("Default");
    }
	
    public Program(String name) {
        this(name, new ArrayList<Course>());
    }
	
	public Program(String name, ArrayList<Course> courses) {
        this.name = name;
        this.courses = courses;
    }

    

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Course getCourse(int index) {
        return courses.get(index);
    }
    public List<Course> getCourses() { return courses; }
    public int getSize() { return courses.size(); }

    public void addCourse(Course c) {
        courses.add(c);
    }

    public void removeCourse(Course b) {
        courses.remove(b);
    }

    public void removeCourseIndex(int index) { courses.remove(index); }

    public void printProgram() {
        System.out.println("Program " + name + " contains " + courses.size() + " courses(s).");
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            System.out.println(c.toString());
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Program[id=%i, name='%s', size='%i']",
                id, name, courses.size());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Program)) return false;
        Program ab = (Program) o;
        if (!this.name.equals(ab.getName())) return false;
        if (courses.size() != ab.getSize()) return false;
        for (int i = 0; i < courses.size(); i++) {
            if (!courses.get(i).equals(ab.getCourse(i))) return false;
        }
        return true;
    }

}