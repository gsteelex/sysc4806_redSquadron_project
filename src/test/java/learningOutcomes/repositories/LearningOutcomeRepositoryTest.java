package learningOutcomes.repositories;

import learningOutcomes.LearningOutcome;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@DataJpaTest
public class LearningOutcomeRepositoryTest {

    @Autowired
    private LearningOutcomeRepository learningOutcomeRepository;

    @Test
    public void testFindById() {
        LearningOutcome learningOutcome = new LearningOutcome();

        learningOutcomeRepository.save(learningOutcome);

        assertTrue(learningOutcomeRepository.findById(learningOutcome.getId()).isPresent());
        assertEquals(learningOutcome, learningOutcomeRepository.findById(learningOutcome.getId()).get());
    }

}
