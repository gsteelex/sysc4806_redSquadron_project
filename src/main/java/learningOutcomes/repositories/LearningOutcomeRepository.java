package learningOutcomes.repositories;

import learningOutcomes.LearningOutcome;
import org.springframework.data.repository.CrudRepository;


public interface LearningOutcomeRepository extends CrudRepository<LearningOutcome, Integer> {
}