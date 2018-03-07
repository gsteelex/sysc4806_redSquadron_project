package learningOutcomes.controllers.requestModels;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CourseRequestTest {

    private static final String NAME = "name123";
    private static final Integer YEAR = 2018;


    @Test
    public void testCourseRequest() {
        CourseRequest request = new CourseRequest();

        assertNull(request.getName());
        assertNull(request.getYear());
        assertNull(request.getLearningOutcomes());
    }

    @Test
    public void testCourseRequest_AttributesSupplied() {
        CourseRequest request = new CourseRequest(NAME, YEAR, new ArrayList<>());

        assertEquals(NAME, request.getName());
        assertEquals(YEAR, request.getYear());
        assertNotNull(request.getLearningOutcomes());
        assertEquals(0, request.getLearningOutcomes().size());
    }

    @Test
    public void testSetName() {
        CourseRequest request = new CourseRequest();
        request.setName(NAME);
        assertEquals(NAME, request.getName());
    }

    @Test
    public void testSetYear() {
        CourseRequest request = new CourseRequest();
        request.setYear(YEAR);
        assertEquals(YEAR, request.getYear());
    }

    @Test
    public void testSetLearningOutcomes() {
        CourseRequest request = new CourseRequest();
        request.setLearningOutcomes(new ArrayList<>());
        assertNotNull(request.getLearningOutcomes());
        assertEquals(0, request.getLearningOutcomes().size());
    }

}
