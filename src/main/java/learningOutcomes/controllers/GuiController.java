package learningOutcomes.controllers;

import learningOutcomes.Program;
import learningOutcomes.repositories.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class GuiController {

    private ProgramRepository repo;

    @Autowired
    public GuiController(ProgramRepository repo){
        this.repo = repo;
    }

    @GetMapping("/")
    public String showPrograms(Model model){
        List<Program> programs =  new ArrayList<>();
        Iterable<Program> it=repo.findAll();

        it.forEach(programs::add);
        model.addAttribute("programs", programs);
        return "main";
    }


    @GetMapping("/admin")
    public String showAdminPage(Model model){
        List<Program> programs =  new ArrayList<>();
        Iterable<Program> it=repo.findAll();

        it.forEach(programs::add);
        model.addAttribute("programs", programs);
        return "admin";
    }

    @GetMapping("/showPrograms/{id}")
    public String showProgram(Model model, @PathVariable("id") Integer id){
        Optional<Program> program = repo.findById(id);
        model.addAttribute("program",program.get());
        return "program";
    }



}
