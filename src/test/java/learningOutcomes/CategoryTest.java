package learningOutcomes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CategoryTest {

    Category cat;
    LearningOutcome lo;

    @Before
    public void setUp() throws Exception {
        cat = new Category();
		lo = new LearningOutcome();
    }

	@Test
    public void setValuesTest() throws Exception {
		cat.setId(1);
		cat.setName("TestName");
		Assert.assertEquals((Integer) 1, cat.getId());
		Assert.assertEquals("TestName", cat.getName());
	}
	
    @Test
    public void addLearningOutcomeTest() throws Exception {
        Assert.assertEquals(0, cat.getSize());
        cat.addLearningOutcome(lo);
        Assert.assertEquals(1, cat.getSize());
        Assert.assertEquals("Default", cat.getLearningOutcome(0).getName());
    }

    @Test
    public void removeLearningOutcomeByIndexTest() throws Exception {
        cat.addLearningOutcome(lo);
        Assert.assertEquals(1, cat.getSize());
        cat.removeLearningOutcomeIndex(0);
        Assert.assertEquals(0, cat.getSize());
    }

    @Test
    public void removeLearningOutcomeByReferenceTest() throws Exception {
        cat.addLearningOutcome(lo);
        Assert.assertEquals(1, cat.getSize());
        cat.removeLearningOutcome(lo);
        Assert.assertEquals(0, cat.getSize());
    }

    @Test
    public void categoryEqualsTest() throws Exception {
        cat.setId(1);
        cat.setName("TestName");
        Category cat2 = new Category("TestName");
        cat2.setId(1);
        cat.addLearningOutcome(lo);
        cat2.addLearningOutcome(lo);
        Assert.assertTrue(cat.equals(cat2));
    }

}