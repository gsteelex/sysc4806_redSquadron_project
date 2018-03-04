package learningOutcomes.repositories;

import learningOutcomes.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testFindById() {
        Category category = new Category();

        categoryRepository.save(category);

        assertTrue(categoryRepository.findById(category.getId()).isPresent());
        assertEquals(category, categoryRepository.findById(category.getId()).get());
    }

}
