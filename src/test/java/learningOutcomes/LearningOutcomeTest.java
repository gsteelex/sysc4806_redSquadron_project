package learningOutcomes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LearningOutcomeTest {

    LearningOutcome lo;
	Category cat;

    @Before
    public void setUp() throws Exception {
        lo = new LearningOutcome();
        cat = new Category("TestCategory");
    }

	@Test
    public void setValuesTest() throws Exception {
		lo.setId(1);
		lo.setName("TestName");
		lo.setCategory(cat);
		Assert.assertEquals((Integer) 1, lo.getId());
		Assert.assertEquals("TestName", lo.getName());
		Assert.assertEquals("TestCategory", lo.getCategory().getName());
	}

	@Test
	public void equalsTest() throws Exception {
    	LearningOutcome lo2 = new LearningOutcome();
    	Category cat2 = new Category("TestCategory");
    	lo2.setId(1);
    	lo2.setCategory(cat2);
    	lo2.setName("TestName");
    	lo.setId(1);
    	lo.setName("TestName");
    	lo.setCategory(cat);
		Assert.assertEquals(true, lo2.equals(lo));
	}
}