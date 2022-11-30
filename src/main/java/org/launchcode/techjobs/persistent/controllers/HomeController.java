package org.launchcode.techjobs.persistent.controllers;

import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Job;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.launchcode.techjobs.persistent.models.data.JobRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */


//Added an autowired employerRepository. User selects an employer when they create a job.
// the employer data from the employerRepository gets added to the form template. They can select an employer from the drop down
//    select html in the add.html template.
@Controller
public class HomeController {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private JobRepository jobRepository;

    @RequestMapping("")
    public String index(Model model) {

        model.addAttribute("title", "TechJobs");
        List jobs = (List<Job>) jobRepository.findAll();
        model.addAttribute("jobs", jobs);

        return "index";
    }

//    renders the add job form, at add.html
    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("title", "Add Job");
        model.addAttribute(new Job());


        List employers = (List<Employer>) employerRepository.findAll();
        model.addAttribute("employers", employers);

        List skills = (List<Skill>) skillRepository.findAll();
        model.addAttribute("skills", skills);

        return "add";
    }

//    after they add the name, employer, and skills, this handler processes that information. Checks validation of the Job Model.
//    Will only query database for skills if valid.
//    and requests an integer type of employerId and requests a list of type Integer skills.
//    searches the employer and skill repository. if it doesnt find, it returns the user to add job form.
//    If no errors, it looks for the employerId, if it doesnt find one, it makes a new Employer object and adds that new employer to the
//    jobRepository under the generic parameter name for this handler, newJob. Same on skill, looks for the skill theyre trying to add
//    if not found it makes a new skill and adds to the skills list. then it sets the skills list onto the generic newJob.

//    @RequestParam List<Integer> skills passes skills into the add.html form control group that iterates over a list of skills,
//    and renders a checkbox for each added skill. To get the skills data from a list of ids,
//    I used the CrudRepository method findAllById(skills).
    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                    Errors errors, Model model, @RequestParam int employerId, @RequestParam List<Integer> skills) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            List employers = (List<Employer>) employerRepository.findAll();
            List<Skill> skillObs = (List<Skill>) skillRepository.findAll();
            model.addAttribute("employers", employers);
            model.addAttribute("skills", skillObs);
            return "add";
        }

        Employer employer = employerRepository.findById(employerId).orElse(new Employer());
        newJob.setEmployer(employer);

//        List<Skill> skillObs = (List<Skill>) skillRepository.findAllById(skills);
//        newJob.setSkills(skillObs);

        List <Skill> newSkill = (List<Skill>) skillRepository.findAllById(skills);
        newJob.setSkills((List<Skill>) newSkill);
//        List<Skill> cannot be converted to a String.
//        line 78 cant be converted to a STRING idk how to fix? newJob is type Job. setSkills is a List<Skill> type, in class Job

        jobRepository.save(newJob);
        return "redirect:./";

    }

//    searches jobRepository by job id so user can view. if not found it returns user to index.
    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {
        Optional<Job> optJob = jobRepository.findById(jobId);
        if (optJob.isPresent()) {
            Job job = (Job) optJob.get();
            model.addAttribute("job", job);
            return "view";
        } else {

            return "redirect:./";
        }
    }
}


