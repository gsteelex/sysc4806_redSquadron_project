package learningOutcomes.repositories;

import learningOutcomes.Category;
import org.springframework.data.repository.CrudRepository;


public interface CategoryRepository extends CrudRepository<Category, Integer> {
}
