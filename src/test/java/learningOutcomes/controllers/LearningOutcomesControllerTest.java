package learningOutcomes.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import learningOutcomes.Category;
import learningOutcomes.repositories.CategoryRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LearningOutcomesControllerTest {
    private static final String CATEGORY_BASE_PATH = "/categories";
    private static final String LEARNING_OUTCOME_BASE_PATH = "/learningOutcomes";
    private static final String NAME = "learningOutcomeNameTest";
    private static final String UPDATED_NAME = "updatedNameTest";

    @Autowired
    private LearningOutcomeRepository learningOutcomeRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    private Category category;

    @Autowired
    private MockMvc mockMvc;

    private String createLearningOutcome() throws Exception {
        MvcResult postResult = mockMvc.perform(
                post(CATEGORY_BASE_PATH + "/" + category.getId() + LEARNING_OUTCOME_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"category\": " + category.getId() + "}")
        )
                .andDo(print())
                .andReturn();

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(postResult.getResponse().getContentAsString()).getAsJsonObject();
        String outcomeId = jsonObject.get("id").getAsString();

        return outcomeId;
    }

    @Before
    public void setUp() {
        category = new Category();
        categoryRepository.save(category);
    }

    @Test
    public void testCreateLearningOutcome_BadRequest() throws Exception {

        mockMvc.perform(
                post(CATEGORY_BASE_PATH + "/" + category.getId() + LEARNING_OUTCOME_BASE_PATH)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateLearningOutcome_CategoryNotFound() throws Exception {

        MvcResult postResult = mockMvc.perform(
                post(CATEGORY_BASE_PATH + "/" + category.getId() + LEARNING_OUTCOME_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"category\": 431}")
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("could not find category with id: 431", postResult.getResponse().getErrorMessage());
    }

    @Test
    public void testCreateLearningOutcome_CategoryFound() throws Exception {

        mockMvc.perform(
                post(CATEGORY_BASE_PATH + "/" + category.getId() + LEARNING_OUTCOME_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"category\": " + category.getId() + "}")
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.category").exists())
                .andExpect(jsonPath("$.category.id").value(category.getId()));
    }

    @Test
    public void testGetAllLearningOutcomes() throws Exception {
        mockMvc.perform(
                post(CATEGORY_BASE_PATH + "/" + category.getId() + LEARNING_OUTCOME_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"category\": " + category.getId() + "}")
        )
                .andDo(print());

        mockMvc.perform(
                get(CATEGORY_BASE_PATH + "/" + category.getId() + LEARNING_OUTCOME_BASE_PATH)
        )
                .andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].name").value(NAME));
    }

    @Test
    public void testGetLearningOutcomeById_NotFound() throws Exception {

        mockMvc.perform(
                get(CATEGORY_BASE_PATH + "/" + category.getId() + LEARNING_OUTCOME_BASE_PATH + "/" + 12345)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetLearningOutcomeById() throws Exception {

        String outcomeId = createLearningOutcome();

        mockMvc.perform(
                get(CATEGORY_BASE_PATH + "/" + category.getId() + LEARNING_OUTCOME_BASE_PATH + "/" + outcomeId)
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.category").exists())
                .andExpect(jsonPath("$.category.id").value(category.getId()));
    }

    @Test
    public void testDeleteLearningOutcomeById_LearningOutcomeDidNotExist() throws Exception {

        mockMvc.perform(
                delete(CATEGORY_BASE_PATH + "/" + category.getId() + LEARNING_OUTCOME_BASE_PATH + "/" + 12345)
        )
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void testDeleteLearningOutcomeById() throws Exception {

        String outcomeId = createLearningOutcome();

        mockMvc.perform(
                delete(CATEGORY_BASE_PATH + "/" + category.getId() + LEARNING_OUTCOME_BASE_PATH + "/" + outcomeId)
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.category").exists())
                .andExpect(jsonPath("$.category.id").value(category.getId()));

        assertTrue(!learningOutcomeRepository.findById(Integer.parseInt(outcomeId)).isPresent());
    }

    @Test
    public void testUpdateLearningOutcomeById_NotFound() throws Exception {

        mockMvc.perform(
                patch(CATEGORY_BASE_PATH + "/" + category.getId() + LEARNING_OUTCOME_BASE_PATH + "/" + 724729)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"category\": " + category.getId() + "}")
        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateLearningOutcomeById_BadRequest() throws Exception {

        String outcomeId = createLearningOutcome();

        mockMvc.perform(
                patch(CATEGORY_BASE_PATH + "/" + category.getId() + LEARNING_OUTCOME_BASE_PATH + "/" + outcomeId)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateLearningOutcomeById_CategoryUnchanged() throws Exception {

        String outcomeId = createLearningOutcome();

        mockMvc.perform(
                patch(CATEGORY_BASE_PATH + "/" + category.getId() + LEARNING_OUTCOME_BASE_PATH + "/" + outcomeId)
                        .contentType("application/json")
                        .content("{\"name\": \"" + UPDATED_NAME + "\", \"category\": " + category.getId() + "}")
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(jsonPath("$.category").exists())
                .andExpect(jsonPath("$.category.id").value(category.getId()));
    }

    @Test
    public void testUpdateLearningOutcomeById_InvalidCategory() throws Exception {

        String outcomeId = createLearningOutcome();

        MvcResult result = mockMvc.perform(
                patch(CATEGORY_BASE_PATH + "/" + category.getId() + LEARNING_OUTCOME_BASE_PATH + "/" + outcomeId)
                        .contentType("application/json")
                        .content("{\"name\": \"" + UPDATED_NAME + "\", \"category\": 684}")
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("could not find category with id: 684", result.getResponse().getErrorMessage());
    }

    @Test
    public void testUpdateLearningOutcomeById_CategoryChanged() throws Exception {

        String outcomeId = createLearningOutcome();
        Category category2 = new Category();
        categoryRepository.save(category2);

        MvcResult result = mockMvc.perform(
                patch(CATEGORY_BASE_PATH + "/" + category.getId() + LEARNING_OUTCOME_BASE_PATH + "/" + outcomeId)
                        .contentType("application/json")
                        .content("{\"name\": \"" + UPDATED_NAME + "\", \"category\": [" + category2.getId() + "]}")
        )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

        assertEquals("changing category field is not permitted", result.getResponse().getErrorMessage());
    }
}
