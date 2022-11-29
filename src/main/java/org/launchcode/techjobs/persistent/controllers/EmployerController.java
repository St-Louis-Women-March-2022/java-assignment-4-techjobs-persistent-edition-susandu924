package org.launchcode.techjobs.persistent.controllers;

import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("employers")
public class EmployerController {

    @Autowired
    private EmployerRepository employerRepository;

//    two handlers with missing information. Your task here is to make use of the EmployerRepository class in these handlers.
    //    Add an index method that responds at /employers with a list of all employers in the database.
//    This method should use the template employers/index.
//    To figure out the name of the model attribute you should use to pass employers into the view, review this template.
//    @GetMapping("")
//    public String index(Model model){
//        model.addAttribute("Employers", "employers");
//        List employers = new ArrayList<Employer>();
//        employerRepository.findAll(employers);
//        model.addAttribute("employers", employers);
//        return "employers/index";
//    }

    @GetMapping("")
    public String index (Model model){
        model.addAttribute("title", "All Employers");
        List employers = (List<Employer>) employerRepository.findAll();
        model.addAttribute("employers", employers);
        return "employers/index";
    }


    @GetMapping("add")
    public String displayAddEmployerForm(Model model) {
        model.addAttribute("employer", new Employer());
        return "employers/add";
    }

    @PostMapping("add")
    public String processAddEmployerForm(@ModelAttribute @Valid Employer newEmployer,
                                    Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Employer");
//            model.addAttribute(newEmployer);
            return "employers/add";
        }
//        model.addAttribute(newEmployer);
        employerRepository.save(newEmployer);

        return "redirect:../";
    }

    @GetMapping("view/{employerId}")
    public String displayViewEmployer(Model model, @PathVariable int employerId) {

        Optional<Employer> optEmployer = employerRepository.findById(employerId);
        if (optEmployer.isPresent()) {
            Employer employer = (Employer) optEmployer.get();
            model.addAttribute("employer", "Employers");
            return "employers";
        } else {
            return "redirect:../";
        }
    }
}
