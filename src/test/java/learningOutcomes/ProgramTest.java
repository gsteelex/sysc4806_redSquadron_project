package learningOutcomes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProgramTest {

    Course course;
    Program program;

    @Before
    public void setUp() throws Exception {
        course = new Course();
        program = new Program();
    }

	@Test
    public void setValuesTest() throws Exception {
		program.setId(1);
		program.setName("TestName");
		//Assert.assertEquals(1, program.getId());
		Assert.assertEquals("TestName", program.getName());
	}
	
    @Test
    public void addCourseTest() throws Exception {
        Assert.assertEquals(0, program.getSize());
        program.addCourse(course);
        Assert.assertEquals(1, program.getSize());
        Assert.assertEquals("Default", program.getCourse(0).getName());
    }

    @Test
    public void removeCourseTest() throws Exception {
        program.addCourse(course);
        Assert.assertEquals(1, program.getSize());
        program.removeCourseIndex(0);
        Assert.assertEquals(0, program.getSize());
    }

}