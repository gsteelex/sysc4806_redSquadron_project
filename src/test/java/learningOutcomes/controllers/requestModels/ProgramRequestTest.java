package learningOutcomes.controllers.requestModels;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class ProgramRequestTest {

    private static final String NAME = "test name";


    @Test
    public void testProgramRequest() {
        ProgramRequest programRequest = new ProgramRequest();

        assertNull(programRequest.getName());
        assertNull(programRequest.getCourses());
    }

    @Test
    public void testProgramRequest_NameSupplied() {
        ProgramRequest programRequest = new ProgramRequest(NAME);

        assertEquals(NAME, programRequest.getName());
        assertNull(programRequest.getCourses());
    }

    @Test
    public void testSetName() {
        ProgramRequest programRequest = new ProgramRequest();
        programRequest.setName(NAME);
        assertEquals(NAME, programRequest.getName());
    }

    @Test
    public void testSetCourses() {
        ProgramRequest programRequest = new ProgramRequest();
        programRequest.setCourses(new ArrayList<>());
        assertNotNull(programRequest.getCourses());
    }

}
