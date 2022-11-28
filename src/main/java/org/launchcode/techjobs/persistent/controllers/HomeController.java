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

/**
 * Created by LaunchCode
 */
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

        List skills = (List<Skill>) skillRepository.findAll();
        model.addAttribute("skills", skills);
        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("Employer", "Add Job");
        List employers = (List<Employer>) employerRepository.findAll();
        model.addAttribute("employers", employers);
        model.addAttribute(new Job());
        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                    Errors errors, Model model, @RequestParam int employerId, @RequestParam List<Integer> skills) {

        if (errors.hasErrors()) {
//       ?     model.addAttribute("title", "Add Job");
            List employers = (List<Employer>) employerRepository.findAll();
            model.addAttribute("employers", employers);
            List skills1 = (List<Skill>) skillRepository.findAll();
            model.addAttribute("skills", skills1);
            return "add";
        }
        Employer employer = employerRepository.findById(employerId).orElse(new Employer());
        newJob.setEmployer(employer);
        model.addAttribute("employers", employerRepository.save(employer));

//        List<Integer>  = new List;
//        Skill skill = skillRepository.findById(skills).orElse(new Skill());
//        newJob.setSkills(String.valueOf(skill));
//        model.addAttribute("skills", skillRepository.save(skill));

//        List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
//        newJob.setSkills(skillObjs.toString());

        jobRepository.save(newJob);
        return "redirect:";
    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {
        model.addAttribute("title", jobId);
        return "view";
    }

//    @GetMapping("add")
//    public String displayAddSkillForm(Model model) {
//        model.addAttribute("skills", "Add Skills");
//        List skills = (List<Skill>) skillRepository.findAll();
//        model.addAttribute("skills", skills);
//        model.addAttribute(new Skill());
//        return "add";
//    }

//    @PostMapping("add")
//    public String processAddSkillForm(@ModelAttribute @Valid Skill newSkill,
//                                    Errors errors, Model model, @RequestParam int skillId, @RequestParam List<Integer> skills) {
//
//        if (errors.hasErrors()) {
//            return "add";
//        }
//        Skill skill = skillRepository.findById(skillId).orElse(new Skill());
//        newSkill.setDescription(String.valueOf(skill));
//        model.addAttribute("skills", skillRepository.save(skill));
//        List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
//        newSkill.setDescription(skillObjs.toString());
//
//        skillRepository.save(newSkill);
//        return "redirect:";
//    }


    @GetMapping("view/{skillId}")
    public String displayViewSkill(Model model, @PathVariable int skillId) {
        model.addAttribute("title", skillId);
        return "view";
    }

}
