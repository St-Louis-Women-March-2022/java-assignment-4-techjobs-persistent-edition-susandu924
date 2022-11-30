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


//Added a private EmployerRepository object with @autowired annotation, which allows AbstractEntity to
//handle data related to the Ids/primary keys of subclasses???
@Controller
@RequestMapping("employers")
public class EmployerController {

    @Autowired
    private EmployerRepository employerRepository;


//    @GetMapping sends a request to index for a list of employers. added index method, that responds with a list of employers. Created a List object named employers, and added it to the
//    model form at the template, employers/index with a list of all the employers in the database.
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

//    Checks to see if the Employer object is valid and if there are any errors, if so, it sends the user
//    back to the add employer page to try again. if not, then it adds a new employer
//    to the employerRepository database.
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

//    displays an individual employer by the employer id
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
