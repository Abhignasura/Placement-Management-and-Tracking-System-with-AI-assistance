package com.example.demo.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.ApiException;
import com.example.demo.model.Activity;
import com.example.demo.model.Job;
import com.example.demo.model.Recruiter;
import com.example.demo.model.User;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.RecruiterRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ActivityService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final RecruiterRepository recruiterRepository;
    private final ActivityService activityService; // ✅ Added

    public AdminController(JobRepository jobRepository,
                           UserRepository userRepository,
                           ApplicationRepository applicationRepository, StudentRepository studentRepository,
                           RecruiterRepository recruiterRepository, ActivityService activityService) {

        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.recruiterRepository = recruiterRepository;
        this.activityService = activityService; //✅ Added
    }

    // 11️⃣ APPROVE JOB
    @PostMapping("/approve-job/{jobId}")
    public String approveJob(@PathVariable Integer jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ApiException("Job not found"));

        job.setApproved(true);
        jobRepository.save(job);

        return "Job Approved Successfully";
    }
    
    @GetMapping("/placement-percentage")
    public Object placementPercentage() {

        long totalStudents = studentRepository.count();
        long selectedStudents = applicationRepository.countByStatus("SELECTED");

        double percentage = (double) selectedStudents / totalStudents * 100;

        Map<String, Object> result = new HashMap<>();
        result.put("totalStudents", totalStudents);
        result.put("selectedStudents", selectedStudents);
        result.put("placementPercentage", percentage);

        return result;
    }
    
    @GetMapping("/branch-stats")
    public Map<String, Long> branchStats(){

    Map<String, Long> stats = new HashMap<>();

    stats.put("CSE",
            studentRepository.countByBranch("CSE"));

    stats.put("ECE",
            studentRepository.countByBranch("ECE"));

    stats.put("IT",
            studentRepository.countByBranch("IT"));

    stats.put("MECH",
            studentRepository.countByBranch("MECH"));

    return stats;
    }
    
    @GetMapping("/company-stats")
    public Object companyStats() {

        List<Recruiter> recruiters = recruiterRepository.findAll();
        Map<String, Integer> stats = new HashMap<>();

        for (Recruiter r : recruiters) {

            String company = r.getCompanyName();
            stats.put(company, stats.getOrDefault(company, 0) + 1);
        }

        return stats;
    }
    
    @GetMapping("/active-jobs")
    public Object activeJobs() {

        long active = jobRepository
            .countByApprovedTrueAndApplicationDeadlineAfter(LocalDate.now());

        return Map.of("activeJobs", active);
    }
    
    @GetMapping("/selected-students")
    public Object selectedStudents() {

        return applicationRepository.findByStatus("SELECTED");
    }
    
    @GetMapping("/deactivated-users")
    public Object deactivatedUsers() {

        return userRepository.findByIsActiveFalse();
    }
    
    @GetMapping("/pending-jobs")
    public List<Job> pendingJobs(){

        return jobRepository.findByApprovedFalse();

    }
    
    @GetMapping("/users")
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @PostMapping("/deactivate-user/{userId}")
    public String deactivateUser(@PathVariable Integer userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found"));

        user.setIsActive(false);

        userRepository.save(user);

        return "User Deactivated";
    }
    
    @GetMapping("/activities")
    public List<Activity> getActivities(){
        return activityService.getRecentActivities();
    }
    
}