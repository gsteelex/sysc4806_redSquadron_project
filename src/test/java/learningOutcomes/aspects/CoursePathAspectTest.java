package learningOutcomes.aspects;

import learningOutcomes.controllers.requestModels.CourseRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CoursePathAspectTest {

    private static final String NAME_ERROR = "name must be supplied";
    private static final String YEAR_ERROR = "year must be supplied";

    @MockBean
    private ProceedingJoinPoint mockPJP;
    @MockBean
    private HttpServletResponse mockResponse;

    private CoursePathAspect pathAspect;

    @Before
    public void setUp() {
        pathAspect = new CoursePathAspect();
    }

    @Test
    public void testValidateCourseRequest_BadName() throws Throwable {
        CourseRequest request = new CourseRequest(null, 5, null);

        Object result = pathAspect.validateCourseRequest(mockPJP, request, mockResponse);

        assertNull(result);
        Mockito.verify(mockResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, NAME_ERROR);
    }

    @Test
    public void testValidateCourseRequest_BadYear() throws Throwable {
        CourseRequest request = new CourseRequest("test", null, null);

        Object result = pathAspect.validateCourseRequest(mockPJP, request, mockResponse);

        assertNull(result);
        Mockito.verify(mockResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, YEAR_ERROR);
    }

    @Test
    public void testValidateCourseRequest() throws Throwable {
        CourseRequest request = new CourseRequest("test", 5, null);
        Object success = new Object();

        when(mockPJP.proceed()).thenReturn(success);

        Object result = pathAspect.validateCourseRequest(mockPJP, request, mockResponse);

        assertEquals(success, result);
    }

}
