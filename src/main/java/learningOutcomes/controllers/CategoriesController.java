package learningOutcomes.controllers;

import learningOutcomes.Category;
import learningOutcomes.LearningOutcome;
import learningOutcomes.aspects.CategoryExistsValidated;
import learningOutcomes.aspects.CategoryNameValidated;
import learningOutcomes.controllers.requestModels.CategoryRequest;
import learningOutcomes.repositories.LearningOutcomeRepository;
import learningOutcomes.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import learningOutcomes.repositories.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value="/categories")
public class CategoriesController {

    private CategoryRepository categoryRepository;
    private LearningOutcomeRepository learningOutcomeRepository;

    @Autowired
    public CategoriesController(CategoryRepository categoryRepository, LearningOutcomeRepository learningOutcomeRepository) {
        this.categoryRepository = categoryRepository;
        this.learningOutcomeRepository = learningOutcomeRepository;
    }

    @RequestMapping(method=RequestMethod.GET)
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();

    }

    @CategoryNameValidated
    @RequestMapping(method = RequestMethod.POST)
    public Category createCategory(HttpServletResponse response, @RequestBody CategoryRequest categoryRequest) throws IOException {
        Category category;

        if (categoryRequest.getLearningOutcomes() == null) {
            category = new Category(categoryRequest.getName());

        } else {
            ArrayList<LearningOutcome> outcomes = new ArrayList<>();

            for (Integer learningOutcomeId: categoryRequest.getLearningOutcomes()) {
                Optional<LearningOutcome> outcome = learningOutcomeRepository.findById(learningOutcomeId);

                if (outcome.isPresent()) {
                    outcomes.add(outcome.get());

                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "could not find learning outcome with id: " + learningOutcomeId);
                    return null;
                }
            }

            category = new Category(categoryRequest.getName(), outcomes);
        }

        categoryRepository.save(category);

        return category;
    }

    @CategoryExistsValidated
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public Category getCategoryById(@PathVariable("id") Integer id, HttpServletResponse response) throws IOException {
        Optional<Category> category = categoryRepository.findById(id);
        return category.get();
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public Category deleteCategoryById(@PathVariable("id") Integer id, HttpServletResponse response) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            Category categoryToReturn = category.get();
            categoryRepository.delete(category.get());
            return categoryToReturn;

        } else {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return null;
        }
    }

    @CategoryNameValidated
    @CategoryExistsValidated
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public Category updateCategoryById(@PathVariable("id") Integer id, HttpServletResponse response, @RequestBody CategoryRequest categoryRequest) throws IOException {
        Optional<Category> category = categoryRepository.findById(id);

        Category categoryToModify = category.get();
        categoryToModify.setName(categoryRequest.getName());


        if (categoryRequest.getLearningOutcomes() != null) {
            ArrayList<LearningOutcome> outcomes = new ArrayList<>();

            for (Integer learningOutcomeId: categoryRequest.getLearningOutcomes()) {
                Optional<LearningOutcome> outcome = learningOutcomeRepository.findById(learningOutcomeId);

                if (outcome.isPresent()) {
                    outcomes.add(outcome.get());

                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "could not find learning outcome with id: " + learningOutcomeId);
                    return null;
                }
            }

            categoryToModify.setLearningOutcomes(outcomes);
        }

        categoryRepository.save(categoryToModify);

        return categoryToModify;
    }

}