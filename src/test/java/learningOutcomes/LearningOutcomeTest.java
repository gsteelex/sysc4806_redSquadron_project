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
        cat.setId(10);
    }

	@Test
    public void setValuesTest() throws Exception {
		lo.setId(1);
		lo.setName("TestName");
		lo.setCategory(cat.getId());
		Assert.assertEquals((Integer) 1, lo.getId());
		Assert.assertEquals("TestName", lo.getName());
		Assert.assertEquals(cat.getId(), lo.getCategory());
	}

	@Test
	public void equalsTest() throws Exception {
    	LearningOutcome lo2 = new LearningOutcome();
    	lo2.setId(1);
    	lo2.setCategory(cat.getId());
    	lo2.setName("TestName");
    	lo.setId(1);
    	lo.setName("TestName");
    	lo.setCategory(cat.getId());
		Assert.assertEquals(true, lo2.equals(lo));
	}
}