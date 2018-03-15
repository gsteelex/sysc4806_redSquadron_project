package learningOutcomes.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import learningOutcomes.Course;
import learningOutcomes.repositories.CourseRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProgramsControllerTest {

    private static final String PROGRAMS_BASE_PATH = "/programs";
    private static final String NAME = "name123";
    private static final String UPDATED_NAME = "updated name 456";

    @Autowired
    private CourseRepository courseRepository;
    private Course course;

    @Autowired
    private MockMvc mockMvc;


    private String createProgram() throws Exception {
        MvcResult postResult = mockMvc.perform(
                post(PROGRAMS_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"courses\": [" + course.getId() + "]}")
        )
                .andDo(print())
                .andReturn();

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(postResult.getResponse().getContentAsString()).getAsJsonObject();
        String programId = jsonObject.get("id").getAsString();

        return programId;
    }

    @Before
    public void setUp() {
        course = new Course();
        courseRepository.save(course);
    }

    @Test
    public void testCreateProgram_BadRequest() throws Exception {
        mockMvc.perform(
                post(PROGRAMS_BASE_PATH)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateProgram_NoCourses() throws Exception {
        mockMvc.perform(
                post(PROGRAMS_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\"}")
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.courses").exists())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.courses").isEmpty());
    }

    @Test
    public void testCreateProgram_CourseNotFound() throws Exception {
        MvcResult postResult = mockMvc.perform(
                post(PROGRAMS_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"courses\": [431]}")
        )
                .andDo(print())
                .andExpect(status().isNotFound())
        .andReturn();

        assertEquals("could not find course with id: 431", postResult.getResponse().getErrorMessage());
    }

    @Test
    public void testCreateProgram_CourseFound() throws Exception {
        mockMvc.perform(
                post(PROGRAMS_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"courses\": [" + course.getId() + "]}")
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.courses").exists())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.courses").isNotEmpty())
                .andExpect(jsonPath("$.courses[0].id").value(course.getId()));
    }

    @Test
    public void testGetAllPrograms() throws Exception {
        mockMvc.perform(
                post(PROGRAMS_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"courses\": [" + course.getId() + "]}")
        )
                .andDo(print());

        mockMvc.perform(
                get(PROGRAMS_BASE_PATH)
        )
                .andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    public void testGetProgramById_NotFound() throws Exception {
        mockMvc.perform(
                get(PROGRAMS_BASE_PATH + "/" + 15616)
        )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testGetProgramById() throws Exception {
        String programId = createProgram();

        mockMvc.perform(
                get(PROGRAMS_BASE_PATH + "/" + programId)
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.courses").exists())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.courses").isNotEmpty())
                .andExpect(jsonPath("$.courses[0].id").value(course.getId()));
    }

    @Test
    public void testDeleteProgramById_ProgramDidNotExist() throws Exception {
        mockMvc.perform(
                delete(PROGRAMS_BASE_PATH + "/" + 15616)
        )
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void testDeleteProgramById() throws Exception {
        String programId = createProgram();

        mockMvc.perform(
                delete(PROGRAMS_BASE_PATH + "/" + programId)
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.courses").exists())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.courses").isNotEmpty())
                .andExpect(jsonPath("$.courses[0].id").value(course.getId()));

        //make sure it does not delete the course, which could be under multiple programs
        assertTrue(courseRepository.findById(course.getId()).isPresent());
    }

    @Test
    public void testUpdateProgramById_NotFound() throws Exception {
        mockMvc.perform(
                patch(PROGRAMS_BASE_PATH + "/" + 724729)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"courses\": [" + course.getId() + "]}")
        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateProgramById_BadRequest() throws Exception {
        String programId = createProgram();

        mockMvc.perform(
                patch(PROGRAMS_BASE_PATH + "/" + programId)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateProgramById_EmptyCourses() throws Exception {
        String programId = createProgram();

        mockMvc.perform(
                patch(PROGRAMS_BASE_PATH + "/" + programId)
                        .contentType("application/json")
                        .content("{\"name\": \"" + UPDATED_NAME + "\", \"courses\": []}")
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(jsonPath("$.courses").exists())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.courses").isEmpty());
    }

    @Test
    public void testUpdateProgramById_CourseDoesNotExist() throws Exception {
        String programId = createProgram();

        MvcResult result = mockMvc.perform(
                patch(PROGRAMS_BASE_PATH + "/" + programId)
                        .contentType("application/json")
                        .content("{\"name\": \"" + UPDATED_NAME + "\", \"courses\": [684]}")
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("could not find course with id: 684", result.getResponse().getErrorMessage());
    }

    @Test
    public void testUpdateProgramById_CoursesUntouched() throws Exception {
        String programId = createProgram();

        mockMvc.perform(
                patch(PROGRAMS_BASE_PATH + "/" + programId)
                        .contentType("application/json")
                        .content("{\"name\": \"" + UPDATED_NAME + "\"}")
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(jsonPath("$.courses").exists())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.courses").isNotEmpty());
    }

    @Test
    public void testUpdateProgramById_CourseExists() throws Exception {
        String programId = createProgram();

        mockMvc.perform(
                patch(PROGRAMS_BASE_PATH + "/" + programId)
                        .contentType("application/json")
                        .content("{\"name\": \"" + UPDATED_NAME + "\", \"courses\": [" + course.getId() + "]}")
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(jsonPath("$.courses").exists())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.courses").isNotEmpty());
    }

}
