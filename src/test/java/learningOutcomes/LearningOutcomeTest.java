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
}