package learningOutcomes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CourseTest {

    Course course;
    LearningOutcome lo;

    @Before
    public void setUp() throws Exception {
        lo = new LearningOutcome();
        course = new Course();
    }

	@Test
    public void setValuesTest() throws Exception {
		course.setId(1L);
		course.setYear(3);
		course.setName("TestName");
		Assert.assertEquals(1L, course.getId());
		Assert.assertEquals(3, course.getYear());
		Assert.assertEquals("TestName", course.getName());
	}
	
    @Test
    public void addLearningOutcomeTest() throws Exception {
        Assert.assertEquals(0, course.getSize());
        course.addLearningOutcome(lo);
        Assert.assertEquals(1, course.getSize());
        Assert.assertEquals("Default", course.getLearningOutcome(0).getName());
    }

    @Test
    public void removeLearningOutcomeTest() throws Exception {
        course.addLearningOutcome(lo);
        Assert.assertEquals(1, course.getSize());
        course.removeLearningOutcomeIndex(0);
        Assert.assertEquals(0, course.getSize());
    }

}