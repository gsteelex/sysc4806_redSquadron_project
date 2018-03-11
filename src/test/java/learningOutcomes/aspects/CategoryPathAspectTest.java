package learningOutcomes.aspects;

import learningOutcomes.Category;
import learningOutcomes.controllers.requestModels.CategoryRequest;
import learningOutcomes.repositories.CategoryRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import javax.servlet.http.HttpServletResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoryPathAspectTest {

    private static final String NAME_ERROR = "name must be supplied";

    @MockBean
    private ProceedingJoinPoint mockPJP;
    @MockBean
    private HttpServletResponse mockResponse;

    @Autowired
    private CategoryRepository categoryRepository;

    private CategoryPathAspect pathAspect;
    private Category category;

    @Before
    public void setUp() {
        pathAspect = new CategoryPathAspect(categoryRepository);

        category = new Category();
        categoryRepository.save(category);
    }

    @Test
    public void testValidateCategoryRequestBadName() throws Throwable {
        CategoryRequest request = new CategoryRequest();

        Object result = pathAspect.validateCategoryRequest(mockPJP, request, mockResponse);

        assertNull(result);
        Mockito.verify(mockResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, NAME_ERROR);
    }

    @Test
    public void testValidateCategoryRequest() throws Throwable {
        CategoryRequest request = new CategoryRequest("test");
        Object success = new Object();

        when(mockPJP.proceed()).thenReturn(success);

        Object result = pathAspect.validateCategoryRequest(mockPJP, request, mockResponse);

        assertEquals(success, result);
    }

    @Test
    public void testValidateCategoryExists_DoesNotExist() throws Throwable {
        Object result = pathAspect.validateCategoryExists(mockPJP, 77326426, mockResponse);

        Mockito.verify(mockResponse).sendError(HttpServletResponse.SC_NOT_FOUND, "category not found with id: " + 77326426);
        assertNull(result);
    }

    @Test
    public void testValidateCategoryExists() throws Throwable {
        Object success = new Object();
        when(mockPJP.proceed()).thenReturn(success);

        Object result = pathAspect.validateCategoryExists(mockPJP, category.getId(), mockResponse);

        assertEquals(success, result);
    }

}
