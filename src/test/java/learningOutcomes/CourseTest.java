package learningOutcomes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;

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
		course.setId(1);
		course.setYear(3);
		course.setName("TestName");
		Assert.assertEquals((Integer) 1, course.getId());
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
    public void removeLearningOutcomeByIndexTest() throws Exception {
        course.addLearningOutcome(lo);
        Assert.assertEquals(1, course.getSize());
        course.removeLearningOutcomeIndex(0);
        Assert.assertEquals(0, course.getSize());
    }

    @Test
    public void removeLearningOutcomeByReferenceTest() throws Exception {
        course.addLearningOutcome(lo);
        Assert.assertEquals(1, course.getSize());
        course.removeLearningOutcome(lo);
        Assert.assertEquals(0, course.getSize());
    }

    @Test
    public void courseEqualsTest() throws Exception {
        course.setId(1);
        course.setYear(3);
        course.setName("TestName");
        Course course2 = new Course("TestName", 3);
        course2.setId(1);
        course.addLearningOutcome(lo);
        course2.addLearningOutcome(lo);
        Assert.assertTrue(course.equals(course2));
    }

    @Test
    public void setOutcomesTest() {
        course.setOutcomes(null);
        assertNull(course.getLearningOutcomes());
    }

}