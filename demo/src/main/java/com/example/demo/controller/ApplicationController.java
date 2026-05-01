package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApplyRequest;
import com.example.demo.dto.JobViewDTO;
import com.example.demo.exception.ApiException;
import com.example.demo.model.Application;
import com.example.demo.model.InterviewSchedule;
import com.example.demo.model.Job;
import com.example.demo.model.Recruiter;
import com.example.demo.model.Round;
import com.example.demo.model.Student;
import com.example.demo.model.User;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.InterviewScheduleRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.RoundRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RecruiterRepository;
import com.example.demo.service.ApplicationService;

@RestController
@RequestMapping("/api/application")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final RoundRepository roundRepository;
    private final RecruiterRepository recruiterRepository;

    public ApplicationController(ApplicationService applicationService, InterviewScheduleRepository interviewScheduleRepository, JobRepository jobRepository, UserRepository userRepository, ApplicationRepository applicationRepository, StudentRepository studentRepository, RoundRepository roundRepository,
    		RecruiterRepository recruiterRepository) {
        this.applicationService = applicationService;
        this.interviewScheduleRepository = interviewScheduleRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.studentRepository  = studentRepository;
        this.roundRepository = roundRepository;
        this.recruiterRepository = recruiterRepository;
    }

    @PostMapping("/apply")
    public String apply(@RequestBody ApplyRequest request) {

        return applicationService.apply(request.getJobId());
    }
    
    @GetMapping("/my-interviews/{applicationId}")
    public Object viewMyInterviews(@PathVariable Integer applicationId) {

        return interviewScheduleRepository.findByApplicationId(applicationId);
    }
    
    @GetMapping("/available-jobs")
    public Object viewAvailableJobs() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found"));

        Student student = studentRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new ApiException("Student not found"));

        List<Job> jobs =
                jobRepository.findByApprovedTrueAndApplicationDeadlineAfter(LocalDate.now())
                .stream()
                .filter(job -> student.getCgpa() >= job.getMinCgpa())
                .toList();

        List<JobViewDTO> result = jobs.stream().map(job -> {

            Recruiter recruiter = recruiterRepository
                    .findById(job.getRecruiterId())
                    .orElseThrow();

            return new JobViewDTO(
                    job.getJobId(),
                    job.getTitle(),
                    recruiter.getCompanyName(),
                    job.getMinCgpa(),
                    job.getApplicationDeadline().toString()
            );

        }).toList();

        return result;
    }
    
    @GetMapping("/my-applications")
    public Object viewMyApplications() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found"));

        Student student = studentRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new ApiException("Student not found"));

        return applicationService.getMyApplications(student.getStudentId());
    }
    
    @GetMapping("/application-details/{applicationId}")
    public Object viewApplicationDetails(@PathVariable Integer applicationId) {

        Map<String, Object> response = new HashMap<>();

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApiException("Application not found"));

        List<Round> rounds = roundRepository.findByApplicationId(applicationId);

        response.put("application", application);
        response.put("rounds", rounds);

        return response;
    }
    
    @GetMapping("/my-all-interviews")
    public Object viewAllMyInterviews() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found"));

        Student student = studentRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new ApiException("Student not found"));

        List<Application> applications =
                applicationRepository.findByStudentId(student.getStudentId());

        List<Map<String,Object>> result = new ArrayList<>();

        for(Application app : applications){

            List<InterviewSchedule> schedules =
                    interviewScheduleRepository.findByApplicationId(app.getApplicationId());

            Job job = jobRepository.findById(app.getJobId())
                    .orElseThrow();

            Recruiter recruiter =
                    recruiterRepository.findById(job.getRecruiterId()).orElseThrow();

            for(InterviewSchedule s : schedules){

                Map<String,Object> row = new HashMap<>();

                row.put("companyName", recruiter.getCompanyName());
                row.put("roundName", s.getRoundName());
                row.put("date", s.getInterviewDate());
                row.put("time", s.getInterviewTime());
                row.put("mode", s.getMode());

                result.add(row);
            }
        }

        return result;
    }
    
    @GetMapping("/dashboard")
    public Object studentDashboard() {

        Map<String, Object> dashboard = new HashMap<>();

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found"));

        Student student = studentRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new ApiException("Student not found"));

        List<Application> applications =
                applicationRepository.findByStudentId(student.getStudentId());

        dashboard.put("applications", applications);

        List<Job> availableJobs =
                jobRepository.findByApprovedTrueAndApplicationDeadlineAfter(LocalDate.now());

        dashboard.put("availableJobs", availableJobs);

        return dashboard;
    }
    
    @GetMapping("/profile")
    public Student getProfile(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found"));

        Student student = studentRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new ApiException("Student not found"));

        return student;
    }
  
}