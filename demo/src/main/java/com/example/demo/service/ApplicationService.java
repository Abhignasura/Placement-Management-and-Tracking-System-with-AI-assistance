package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ApiException;
import com.example.demo.model.Application;
import com.example.demo.model.Job;
import com.example.demo.model.Recruiter;
import com.example.demo.model.Student;
import com.example.demo.model.User;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.dto.ApplicationStatusDTO;
import com.example.demo.repository.RecruiterRepository;
import com.example.demo.service.ActivityService;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final RecruiterRepository recruiterRepository;
    private final ActivityService activityService;

    public ApplicationService(ApplicationRepository applicationRepository,
                              StudentRepository studentRepository,
                              JobRepository jobRepository,
                              UserRepository userRepository,
                              RecruiterRepository recruiterRepository,
                              ActivityService activityService) {

        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.recruiterRepository = recruiterRepository;
        this.activityService = activityService;
    }

    public String apply(Integer jobId) {

        // 1️⃣ Get logged-in user email from SecurityContext
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found"));

        // 2️⃣ Get student from user
        Student student = studentRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new ApiException("Student record not found"));

        // 3️⃣ Get job
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ApiException("Job not found"));
        if (!Boolean.TRUE.equals(job.getApproved())) {
            throw new ApiException("Job not yet approved by admin");
        }

        // 4️⃣ Check deadline
        if (job.getApplicationDeadline().isBefore(LocalDate.now())) {
            throw new ApiException("Application deadline passed");
        }

        // 5️⃣ Check CGPA eligibility
        if (student.getCgpa() < job.getMinCgpa()) {
            throw new ApiException("You are not eligible for this job");
        }

        // 6️⃣ Check duplicate
        if (applicationRepository
                .findByStudentIdAndJobId(student.getStudentId(), jobId)
                .isPresent()) {
            throw new ApiException("You have already applied to this job");
        }

        // 7️⃣ Save
        Application application = new Application();
        application.setStudentId(student.getStudentId());
        application.setJobId(jobId);
        application.setStatus("APPLIED");

        applicationRepository.save(application);

        activityService.log("📄 " + student.getRollNo() + " applied for " + job.getTitle());

        return "Applied Successfully";
    }
    
    public List<ApplicationStatusDTO> getMyApplications(Integer studentId) {

        List<Application> applications = applicationRepository.findByStudentId(studentId);

        List<ApplicationStatusDTO> result = new ArrayList<>();

        for(Application app : applications){

            Job job = jobRepository.findById(app.getJobId())
                    .orElseThrow(() -> new ApiException("Job not found"));

            Recruiter recruiter = recruiterRepository.findById(job.getRecruiterId())
                    .orElseThrow(() -> new ApiException("Recruiter not found"));

            ApplicationStatusDTO dto = new ApplicationStatusDTO();

            dto.setApplicationId(app.getApplicationId());
            dto.setStatus(app.getStatus());
            dto.setJobTitle(job.getTitle());
            dto.setCompanyName(recruiter.getCompanyName());

            result.add(dto);
        }

        return result;
    }
}