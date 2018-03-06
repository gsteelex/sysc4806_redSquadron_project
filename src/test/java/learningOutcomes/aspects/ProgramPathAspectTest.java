package learningOutcomes.aspects;

import learningOutcomes.Program;
import learningOutcomes.controllers.requestModels.ProgramRequest;
import learningOutcomes.repositories.ProgramRepository;
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
public class ProgramPathAspectTest {

    private static final String NAME_ERROR = "name must be supplied";

    @MockBean
    private ProceedingJoinPoint mockPJP;
    @MockBean
    private HttpServletResponse mockResponse;

    @Autowired
    private ProgramRepository programRepository;

    private ProgramPathAspect pathAspect;
    private Program program;

    @Before
    public void setUp() {
        pathAspect = new ProgramPathAspect(programRepository);

        program = new Program();
        programRepository.save(program);
    }

    @Test
    public void testValidateProgramRequestBadName() throws Throwable {
        ProgramRequest request = new ProgramRequest();

        Object result = pathAspect.validateProgramRequest(mockPJP, request, mockResponse);

        assertNull(result);
        Mockito.verify(mockResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, NAME_ERROR);
    }

    @Test
    public void testValidateProgramRequest() throws Throwable {
        ProgramRequest request = new ProgramRequest("test");
        Object success = new Object();

        when(mockPJP.proceed()).thenReturn(success);

        Object result = pathAspect.validateProgramRequest(mockPJP, request, mockResponse);

        assertEquals(success, result);
    }

    @Test
    public void testValidateProgramExists_DoesNotExist() throws Throwable {
        Object result = pathAspect.validateProgramExists(mockPJP, 77326426, mockResponse);

        Mockito.verify(mockResponse).sendError(HttpServletResponse.SC_NOT_FOUND, "program not found with id: " + 77326426);
        assertNull(result);
    }

    @Test
    public void testValidateProgramExists() throws Throwable {
        Object success = new Object();
        when(mockPJP.proceed()).thenReturn(success);

        Object result = pathAspect.validateProgramExists(mockPJP, program.getId(), mockResponse);

        assertEquals(success, result);
    }

}
