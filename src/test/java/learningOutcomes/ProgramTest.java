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
		Assert.assertEquals((Integer) 1, program.getId());
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
    public void removeCourseByIndexTest() throws Exception {
        program.addCourse(course);
        Assert.assertEquals(1, program.getSize());
        program.removeCourseIndex(0);
        Assert.assertEquals(0, program.getSize());
    }

    @Test
    public void removeCourseByReferenceTest() throws Exception {
        program.addCourse(course);
        Assert.assertEquals(1, program.getSize());
        program.removeCourse(course);
        Assert.assertEquals(0, program.getSize());
    }

    @Test
    public void programEqualsTest() throws Exception {
        program.setId(1);
        program.setName("TestName");
        Program program2 = new Program("TestName");
        program2.setId(1);
        program.addCourse(course);
        program2.addCourse(course);
        Assert.assertTrue(program.equals(program2));
    }

    @Test
    public void testHasCourseWithId_CourseNotFound() {
        Assert.assertFalse(program.hasCourseWithId(256));
    }

    @Test
    public void testHasCourseWithId_CourseFound() {
        int courseId = 1;
        Course course = new Course();
        course.setId(courseId);
        program.addCourse(course);

        Assert.assertTrue(program.hasCourseWithId(courseId));
    }

}