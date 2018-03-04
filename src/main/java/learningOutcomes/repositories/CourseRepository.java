package learningOutcomes.repositories;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepository extends CrudRepository<learningOutcomes.Course, Integer> {
}