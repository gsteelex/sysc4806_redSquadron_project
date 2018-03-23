package learningOutcomes.aspects;

import learningOutcomes.LearningOutcome;
import learningOutcomes.controllers.requestModels.LearningOutcomeRequest;
import learningOutcomes.repositories.LearningOutcomeRepository;
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
public class LearningOutcomePathAspectTest {

    private static final String NAME_ERROR = "name must be supplied";

    @MockBean
    private ProceedingJoinPoint mockPJP;
    @MockBean
    private HttpServletResponse mockResponse;

    @Autowired
    private LearningOutcomeRepository outcomeRepository;

    private LearningOutcomePathAspect pathAspect;
    private LearningOutcome learningOutcome;

    @Before
    public void setUp() {
        pathAspect = new LearningOutcomePathAspect(outcomeRepository);
        learningOutcome = new LearningOutcome();
        outcomeRepository.save(learningOutcome);
    }

    @Test
    public void testValidateLearningOutcomeRequestBadName() throws Throwable {
        LearningOutcomeRequest request = new LearningOutcomeRequest();
        Object result = pathAspect.validateLearningOutcomeRequest(mockPJP, request, mockResponse);
        assertNull(result);
        Mockito.verify(mockResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, NAME_ERROR);
    }

    @Test
    public void testValidateLearningOutcomeRequest() throws Throwable {
        LearningOutcomeRequest request = new LearningOutcomeRequest("test", 1);
        Object success = new Object();

        when(mockPJP.proceed()).thenReturn(success);

        Object result = pathAspect.validateLearningOutcomeRequest(mockPJP, request, mockResponse);

        assertEquals(success, result);
    }

    @Test
    public void testValidateLearningOutcomeExists_DoesNotExist() throws Throwable {
        Object result = pathAspect.validateLearningOutcomeExists(mockPJP, 77326426, mockResponse);

        Mockito.verify(mockResponse).sendError(HttpServletResponse.SC_NOT_FOUND, "learning outcome not found with id: " + 77326426);
        assertNull(result);
    }

    @Test
    public void testValidateLearningOutcomeExists() throws Throwable {
        Object success = new Object();
        when(mockPJP.proceed()).thenReturn(success);

        Object result = pathAspect.validateLearningOutcomeExists(mockPJP, learningOutcome.getId(), mockResponse);

        assertEquals(success, result);
    }


}
