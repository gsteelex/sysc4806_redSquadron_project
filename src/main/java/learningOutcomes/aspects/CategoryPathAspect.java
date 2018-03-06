package learningOutcomes.aspects;

import learningOutcomes.Category;
import learningOutcomes.repositories.CategoryRepository;
import learningOutcomes.controllers.requestModels.CategoryRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class CategoryPathAspect {

    public CategoryRepository categoryRepository;

    @Autowired
    public CategoryPathAspect (CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Around("@annotation(CategoryNameValidated) && (args(response, categoryRequest,..) || args(*, response, categoryRequest,..))")
    public Object validateCategoryRequest(ProceedingJoinPoint pjp, CategoryRequest categoryRequest, HttpServletResponse response) throws Throwable {
        if (categoryRequest.getName() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "name must be supplied");
            return null;
        } else {
            return pjp.proceed();
        }
    }

    @Around("@annotation(CategoryExistsValidated) && args(id,response,.. )")
    public Object validateCategoryExists(ProceedingJoinPoint pjp, Integer id, HttpServletResponse response) throws Throwable {

        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            return pjp.proceed();

        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "category not found with id: " + id);
            return null;
        }
    }

}