package learningOutcomes.controllers;

import learningOutcomes.Category;
import learningOutcomes.LearningOutcome;
import learningOutcomes.aspects.LearningOutcomeExistsValidated;
import learningOutcomes.aspects.LearningOutcomeRequestValidated;
import learningOutcomes.controllers.requestModels.LearningOutcomeRequest;
import learningOutcomes.repositories.LearningOutcomeRepository;
import learningOutcomes.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import learningOutcomes.repositories.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value="/categories/")
public class LearningOutcomeController {

    private CategoryRepository categoryRepository;
    private LearningOutcomeRepository learningOutcomeRepository;

    @Autowired
    public LearningOutcomeController(CategoryRepository categoryRepository, LearningOutcomeRepository learningOutcomeRepository) {
        this.categoryRepository = categoryRepository;
        this.learningOutcomeRepository = learningOutcomeRepository;
    }

    @RequestMapping(value="/{categoryId}/learningOutcomes", method=RequestMethod.GET)
    public Iterable<LearningOutcome> getAllLearningOutcomes(@PathVariable("categoryId") Integer categoryId) {

        Iterable<LearningOutcome> allLearningOutcomes = learningOutcomeRepository.findAll();
        List<LearningOutcome> learningOutcomes = new ArrayList<>();

        for (LearningOutcome outcome: allLearningOutcomes) {
            if (outcome.getCategory() != null && categoryId.equals(outcome.getCategory())) {
                learningOutcomes.add(outcome);
            }
        }

        return learningOutcomes;
    }

    @LearningOutcomeRequestValidated
    @RequestMapping(value="/{categoryId}/learningOutcomes", method = RequestMethod.POST)
    public LearningOutcome createLearningOutcome(@PathVariable("categoryId") Integer categoryId, HttpServletResponse response, @RequestBody LearningOutcomeRequest learningOutcomeRequest) throws IOException {

        LearningOutcome learningOutcome = new LearningOutcome();
        learningOutcome.setName(learningOutcomeRequest.getName());

        if (learningOutcomeRequest.getCategory() != null) {

            Optional<Category> category = categoryRepository.findById(categoryId);

            if (category.isPresent()) {
                learningOutcome.setCategory(category.get().getId());
                learningOutcomeRepository.save(learningOutcome);
                Category categoryToUpdate = category.get();
                categoryToUpdate.addLearningOutcome(learningOutcome);
                categoryRepository.save(categoryToUpdate);

            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "could not find category with id: " + categoryId);
                return null;
            }
        }



        return learningOutcome;
    }

    @LearningOutcomeExistsValidated
    @RequestMapping(value="/{categoryId}/learningOutcomes/{loId}", method = RequestMethod.GET)
    public LearningOutcome getLearningOutcomeById(@PathVariable("categoryId") Integer categoryId, @PathVariable("loId") Integer loId, HttpServletResponse response) {

        return learningOutcomeRepository.findById(loId).get();
    }

    @RequestMapping(value="/{categoryId}/learningOutcomes/{loId}", method = RequestMethod.DELETE)
    public LearningOutcome deleteLearningOutcomeById(@PathVariable("categoryId") Integer categoryId, @PathVariable("loId") Integer loId, HttpServletResponse response) {
        Optional<LearningOutcome> learningOutcome = learningOutcomeRepository.findById(loId);
        Optional<Category> category = categoryRepository.findById(categoryId);

        if (learningOutcome.isPresent() && category.isPresent()) {
            LearningOutcome learningOutcomeToReturn = learningOutcome.get();
            Category removeFrom = category.get();
            removeFrom.removeLearningOutcome(learningOutcomeToReturn);
            categoryRepository.save(removeFrom);
            learningOutcomeRepository.delete(learningOutcome.get());
            return learningOutcomeToReturn;

        } else {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return null;
        }
    }

    @LearningOutcomeRequestValidated
    @LearningOutcomeExistsValidated
    @RequestMapping(value="/{categoryId}/learningOutcomes/{loId}", method = RequestMethod.PATCH)
    public LearningOutcome updateLearningOutcomeById(@PathVariable("categoryId") Integer categoryId, @PathVariable("loId") Integer loId, HttpServletResponse response, @RequestBody LearningOutcomeRequest learningOutcomeRequest) throws IOException {
        Optional<LearningOutcome> learningOutcome = learningOutcomeRepository.findById(loId);

        LearningOutcome learningOutcomeToModify = learningOutcome.get();
        learningOutcomeToModify.setName(learningOutcomeRequest.getName());


        if (learningOutcomeRequest.getCategory() != null) {
            int requestCategory = learningOutcomeRequest.getCategory();

            Optional<Category> category = categoryRepository.findById(requestCategory);

            if (category.isPresent()) {
                if (requestCategory != learningOutcomeToModify.getCategory()) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "changing category field is not permitted");
                    return null;
                }

            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "could not find category with id: " + requestCategory);
                return null;
            }
        }

        learningOutcomeRepository.save(learningOutcomeToModify);

        return learningOutcomeToModify;
    }

}