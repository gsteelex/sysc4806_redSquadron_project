package learningOutcomes.controllers;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import learningOutcomes.LearningOutcome;
import learningOutcomes.repositories.LearningOutcomeRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CoursesControllerTest {

    private static final String COURSES_BASE_PATH = "/courses";
    private static final String NAME = "name123";
    private static final Integer YEAR = 2018;

    @Autowired
    private LearningOutcomeRepository learningOutcomeRepository;
    private LearningOutcome learningOutcome;

    @Autowired
    private MockMvc mockMvc;


    private String createCourse() throws Exception {
        MvcResult postResult = mockMvc.perform(
                post(COURSES_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"year\": \"" + YEAR + "\", \"learningOutcomes\":[" + learningOutcome.getId() + "]}")
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
        learningOutcome = new LearningOutcome();
        learningOutcomeRepository.save(learningOutcome);
    }

    @Test
    public void testCreateCourse_BadRequest() throws Exception {
        mockMvc.perform(
                post(COURSES_BASE_PATH)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateCourse_NoLearningOutcomes() throws Exception {
        mockMvc.perform(
                post(COURSES_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"year\": \"" + YEAR + "\"}")
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.year").exists())
                .andExpect(jsonPath("$.year").value(YEAR))
                .andExpect(jsonPath("$.learningOutcomes").exists())
                .andExpect(jsonPath("$.learningOutcomes").isArray())
                .andExpect(jsonPath("$.learningOutcomes").isEmpty());
    }

    @Test
    public void testCreateCourse_LearningOutcomeNotFound() throws Exception {
        MvcResult postResult = mockMvc.perform(
                post(COURSES_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"year\": \"" + YEAR + "\", \"learningOutcomes\":[76534]}")
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("could not find LearningOutcome with id: 76534", postResult.getResponse().getErrorMessage());
    }

    @Test
    public void testCreateCourse_LearningOutcomeFound() throws Exception {
        mockMvc.perform(
                post(COURSES_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"year\": \"" + YEAR + "\", \"learningOutcomes\":[" + learningOutcome.getId() + "]}")
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.year").exists())
                .andExpect(jsonPath("$.year").value(YEAR))
                .andExpect(jsonPath("$.learningOutcomes").exists())
                .andExpect(jsonPath("$.learningOutcomes").isArray())
                .andExpect(jsonPath("$.learningOutcomes[0].id").exists())
                .andExpect(jsonPath("$.learningOutcomes[0].id").value(learningOutcome.getId()));
    }

    @Test
    public void testGetAllCourses() throws Exception {
        createCourse();

        mockMvc.perform(
                get(COURSES_BASE_PATH)
        ).andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].name").value(NAME))
                .andExpect(jsonPath("$[0].year").exists())
                .andExpect(jsonPath("$[0].year").value(YEAR))
                .andExpect(jsonPath("$[0].learningOutcomes").exists())
                .andExpect(jsonPath("$[0].learningOutcomes").isArray())
                .andExpect(jsonPath("$[0].learningOutcomes[0].id").exists());
    }

    @Test
    public void testDeleteCourseById_CourseExists() throws Exception {
        String courseId = createCourse();

        mockMvc.perform(
                delete(COURSES_BASE_PATH + "/" + courseId)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"year\": \"" + YEAR + "\", \"learningOutcomes\":[" + learningOutcome.getId() + "]}")
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.year").exists())
                .andExpect(jsonPath("$.year").value(YEAR))
                .andExpect(jsonPath("$.learningOutcomes").exists())
                .andExpect(jsonPath("$.learningOutcomes").isArray())
                .andExpect(jsonPath("$.learningOutcomes[0].id").exists())
                .andExpect(jsonPath("$.learningOutcomes[0].id").value(learningOutcome.getId()));

    }

    @Test
    public void testDeleteCourseById_CourseDoesNotExist() throws Exception {
        mockMvc.perform(
                delete(COURSES_BASE_PATH + "/" + 4846)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"year\": \"" + YEAR + "\", \"learningOutcomes\":[" + learningOutcome.getId() + "]}")
        )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteCourseById_LearningOutcomeNotDeleted() throws Exception {
        String courseId = createCourse();

        mockMvc.perform(
                delete(COURSES_BASE_PATH + "/" + courseId)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"year\": \"" + YEAR + "\", \"learningOutcomes\":[" + learningOutcome.getId() + "]}")
        )
                .andDo(print());

        assertTrue(learningOutcomeRepository.findById(learningOutcome.getId()).isPresent());
    }
}
