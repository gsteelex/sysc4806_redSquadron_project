package learningOutcomes.controllers;

import learningOutcomes.Program;
import learningOutcomes.repositories.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GuiController {

    private ProgramRepository repo;

    @Autowired
    public GuiController(ProgramRepository repo){
        this.repo = repo;
    }

    @GetMapping("/showPrograms")
    public String showPrograms(Model model){
        List<Program> programs =  new ArrayList<>();
        Iterable<Program> it=repo.findAll();

        it.forEach(programs::add);
        model.addAttribute("programs", programs);
        return "programs";
    }
}
