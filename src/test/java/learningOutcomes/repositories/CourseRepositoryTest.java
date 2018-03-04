package learningOutcomes.repositories;

import learningOutcomes.Course;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@DataJpaTest
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void testFindById() {
        Course course = new Course();

        courseRepository.save(course);

        assertTrue(courseRepository.findById(course.getId()).isPresent());
        assertEquals(course, courseRepository.findById(course.getId()).get());
    }

}
