package learningOutcomes.repositories;

import org.springframework.data.repository.CrudRepository;


public interface CourseRepository extends CrudRepository<learningOutcomes.Course, Integer> {
}