package learningOutcomes.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import learningOutcomes.Category;
import learningOutcomes.LearningOutcome;
import learningOutcomes.aspects.CategoryPathAspect;
import learningOutcomes.repositories.CategoryRepository;
import learningOutcomes.repositories.LearningOutcomeRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CategoriesControllerTest {

    private static final String CATEGORY_BASE_PATH = "/categories";
    private static final String NAME = "categoryNameTest";
    private static final String UPDATED_NAME = "updatedNameTest";

    @Autowired
    private LearningOutcomeRepository learningOutcomeRepository;
    private LearningOutcome outcome;

    @Autowired
    private MockMvc mockMvc;


    private String createCategory() throws Exception {
        MvcResult postResult = mockMvc.perform(
                post(CATEGORY_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"learningOutcomes\": [" + outcome.getId() + "]}")
        )
                .andDo(print())
                .andReturn();

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(postResult.getResponse().getContentAsString()).getAsJsonObject();
        String categoryId = jsonObject.get("id").getAsString();

        return categoryId;
    }

    @Before
    public void setUp() {
        outcome = new LearningOutcome();
        learningOutcomeRepository.save(outcome);
    }

    @Test
    public void testCreateCategory_BadRequest() throws Exception {

        mockMvc.perform(
                post(CATEGORY_BASE_PATH)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateCategory_NoLearningOutcomes() throws Exception {

        mockMvc.perform(
                post(CATEGORY_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\"}")
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.learningOutcomes").exists())
                .andExpect(jsonPath("$.learningOutcomes").isArray())
                .andExpect(jsonPath("$.learningOutcomes").isEmpty());
    }

    @Test
    public void testCreateCategory_LearningOutcomeNotFound() throws Exception {

        MvcResult postResult = mockMvc.perform(
                post(CATEGORY_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"learningOutcomes\": [431]}")
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("could not find learning outcome with id: 431", postResult.getResponse().getErrorMessage());
    }

    @Test
    public void testCreateCategory_LearningOutcomeFound() throws Exception {

        mockMvc.perform(
                post(CATEGORY_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"learningOutcomes\": [" + outcome.getId() + "]}")
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.learningOutcomes").exists())
                .andExpect(jsonPath("$.learningOutcomes").isArray())
                .andExpect(jsonPath("$.learningOutcomes").isNotEmpty())
                .andExpect(jsonPath("$.learningOutcomes[0].id").value(outcome.getId()));
    }

    @Test
    public void testGetAllCategories() throws Exception {
        mockMvc.perform(
                post(CATEGORY_BASE_PATH)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"learningOutcomes\": [" + outcome.getId() + "]}")
        )
                .andDo(print());

        mockMvc.perform(
                get(CATEGORY_BASE_PATH)
        )
                .andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].name").isString());
    }

    @Test
    public void testGetCategoryById_NotFound() throws Exception {

        mockMvc.perform(
                get(CATEGORY_BASE_PATH + "/" + 12345)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetCategoryById() throws Exception {

        String categoryId = createCategory();

        mockMvc.perform(
                get(CATEGORY_BASE_PATH + "/" + categoryId)
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.learningOutcomes").exists())
                .andExpect(jsonPath("$.learningOutcomes").isArray())
                .andExpect(jsonPath("$.learningOutcomes").isNotEmpty())
                .andExpect(jsonPath("$.learningOutcomes[0].id").value(outcome.getId()));
    }

    @Test
    public void testDeleteCategoryById_CategoryDidNotExist() throws Exception {

        mockMvc.perform(
                delete(CATEGORY_BASE_PATH + "/" + 12345)
        )
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void testDeleteCategoryById() throws Exception {

        String categoryId = createCategory();

        mockMvc.perform(
                delete(CATEGORY_BASE_PATH + "/" + categoryId)
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.learningOutcomes").exists())
                .andExpect(jsonPath("$.learningOutcomes").isArray())
                .andExpect(jsonPath("$.learningOutcomes").isNotEmpty())
                .andExpect(jsonPath("$.learningOutcomes[0].id").value(outcome.getId()));

        assertTrue(!learningOutcomeRepository.findById(outcome.getId()).isPresent());
    }

    @Test
    public void testUpdateCategoryById_NotFound() throws Exception {

        mockMvc.perform(
                patch(CATEGORY_BASE_PATH + "/" + 724729)
                        .contentType("application/json")
                        .content("{\"name\": \"" + NAME + "\", \"learningOutcomes\": [" + outcome.getId() + "]}")
        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateCategoryById_BadRequest() throws Exception {

        String categoryId = createCategory();

        mockMvc.perform(
                patch(CATEGORY_BASE_PATH + "/" + categoryId)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateCategoryById_EmptyCourses() throws Exception {

        String categoryId = createCategory();

        mockMvc.perform(
                patch(CATEGORY_BASE_PATH + "/" + categoryId)
                        .contentType("application/json")
                        .content("{\"name\": \"" + UPDATED_NAME + "\", \"learningOutcomes\": []}")
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(jsonPath("$.learningOutcomes").exists())
                .andExpect(jsonPath("$.learningOutcomes").isArray())
                .andExpect(jsonPath("$.learningOutcomes").isEmpty());
    }

    @Test
    public void testUpdateCategoryById_LearningOutcomeDoesNotExist() throws Exception {

        String categoryId = createCategory();

        MvcResult result = mockMvc.perform(
                patch(CATEGORY_BASE_PATH + "/" + categoryId)
                        .contentType("application/json")
                        .content("{\"name\": \"" + UPDATED_NAME + "\", \"learningOutcomes\": [684]}")
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("could not find learning outcome with id: 684", result.getResponse().getErrorMessage());
    }

    @Test
    public void testUpdateCategoryById_LearningOutcomesUntouched() throws Exception {

        String categoryId = createCategory();

        mockMvc.perform(
                patch(CATEGORY_BASE_PATH + "/" + categoryId)
                        .contentType("application/json")
                        .content("{\"name\": \"" + UPDATED_NAME + "\"}")
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(jsonPath("$.learningOutcomes").exists())
                .andExpect(jsonPath("$.learningOutcomes").isArray())
                .andExpect(jsonPath("$.learningOutcomes").isNotEmpty());
    }

    @Test
    public void testUpdateCategoryById_LearningOutcomeExists() throws Exception {

        String categoryId = createCategory();

        mockMvc.perform(
                patch(CATEGORY_BASE_PATH + "/" + categoryId)
                        .contentType("application/json")
                        .content("{\"name\": \"" + UPDATED_NAME + "\", \"learningOutcomes\": [" + outcome.getId() + "]}")
        )
                .andDo(print())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(jsonPath("$.learningOutcomes").exists())
                .andExpect(jsonPath("$.learningOutcomes").isArray())
                .andExpect(jsonPath("$.learningOutcomes").isNotEmpty());
    }

}
