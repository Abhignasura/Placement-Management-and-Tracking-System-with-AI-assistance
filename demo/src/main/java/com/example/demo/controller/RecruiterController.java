package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApplicantDTO;
import com.example.demo.dto.CreateJobRequest;
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
import com.example.demo.repository.RecruiterRepository;
import com.example.demo.repository.RoundRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.StudentRepository;

@RestController
@RequestMapping("/api/recruiter")
public class RecruiterController {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final RecruiterRepository recruiterRepository;
    private final RoundRepository roundRepository;  // 🔥 Added
    private final ApplicationRepository applicationRepository;
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final StudentRepository studentRepository;

    public RecruiterController(JobRepository jobRepository,
            UserRepository userRepository,
            RecruiterRepository recruiterRepository,
            RoundRepository roundRepository,
            ApplicationRepository applicationRepository, InterviewScheduleRepository interviewScheduleRepository,
            StudentRepository studentRepository) {

			this.jobRepository = jobRepository;
			this.userRepository = userRepository;
			this.recruiterRepository = recruiterRepository;
			this.roundRepository = roundRepository;
			this.applicationRepository = applicationRepository;
			this.interviewScheduleRepository = interviewScheduleRepository;
			this.studentRepository = studentRepository;
			}

    // ✅ CREATE JOB
    @PostMapping("/create-job")
    public String createJob(@RequestBody CreateJobRequest request) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("Recruiter not found"));

        Recruiter recruiter = recruiterRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new ApiException("Recruiter profile not found"));

        Job job = new Job();
        job.setRecruiterId(recruiter.getRecruiterId());
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setMinCgpa(request.getMinCgpa());
        job.setApplicationDeadline(request.getApplicationDeadline());

        jobRepository.save(job);

        return "Job Created Successfully";
    }


    // ✅ UPDATE ROUND RESULT
    @PostMapping("/update-result")
    public String updateResult(@RequestParam Integer applicationId,
                               @RequestParam String roundName,
                               @RequestParam Integer roundOrder,
                               @RequestParam String result) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApiException("Application not found"));

        // 🚫 Prevent update if already finalized
        if ("REJECTED".equals(application.getStatus()) ||
            "SELECTED".equals(application.getStatus())) {
            throw new ApiException("Final status already decided");
        }

        // 🚫 Validate result
        if (!"PASS".equalsIgnoreCase(result) &&
            !"FAIL".equalsIgnoreCase(result)) {
            throw new ApiException("Invalid result value");
        }

        Round round = new Round();
        round.setApplicationId(applicationId);
        round.setRoundName(roundName);
        round.setRoundOrder(roundOrder);
        round.setResult(result.toUpperCase());

        roundRepository.save(round);

        // 🔥 AUTO STATUS UPDATE
        if ("FAIL".equalsIgnoreCase(result)) {
            application.setStatus("REJECTED");
        } else {
            application.setStatus("IN_PROGRESS");
        }

        applicationRepository.save(application);

        return "Round Updated Successfully";
    }
    
    @PostMapping("/final-select/{applicationId}")
    public String finalSelect(@PathVariable Integer applicationId) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApiException("Application not found"));

        if ("REJECTED".equals(application.getStatus())) {
            throw new ApiException("Cannot select rejected candidate");
        }

        application.setStatus("SELECTED");
        applicationRepository.save(application);

        return "Candidate Selected Successfully";
    }
    
    @GetMapping("/round-history/{applicationId}")
    public Object getRoundHistory(@PathVariable Integer applicationId) {

        return roundRepository.findByApplicationId(applicationId);
    }
    
    @PostMapping("/schedule-interview")
    public String scheduleInterview(@RequestParam Integer applicationId,
                                    @RequestParam String roundName,
                                    @RequestParam String date,
                                    @RequestParam String time,
                                    @RequestParam String mode,
                                    @RequestParam String location) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApiException("Application not found"));

        if ("REJECTED".equals(application.getStatus())) {
            throw new ApiException("Cannot schedule interview for rejected candidate");
        }

        InterviewSchedule schedule = new InterviewSchedule();
        schedule.setApplicationId(applicationId);
        schedule.setRoundName(roundName);
        schedule.setInterviewDate(LocalDate.parse(date));
        schedule.setInterviewTime(LocalTime.parse(time));
        schedule.setMode(mode.toUpperCase());
        schedule.setLocationOrLink(location);

        interviewScheduleRepository.save(schedule);

        return "Interview Scheduled Successfully";
    }
    
    @GetMapping("/applicants/{jobId}")
    public List<ApplicantDTO> viewApplicants(@PathVariable Integer jobId) {

        List<Application> applications = applicationRepository.findByJobId(jobId);

        List<ApplicantDTO> result = new ArrayList<>();

        for (Application app : applications) {

            Student student = studentRepository
                    .findById(app.getStudentId())
                    .orElseThrow(() -> new ApiException("Student not found"));

            Job job = jobRepository
                    .findById(app.getJobId())
                    .orElseThrow(() -> new ApiException("Job not found"));

            Recruiter recruiter = recruiterRepository
                    .findById(job.getRecruiterId())
                    .orElseThrow(() -> new ApiException("Recruiter not found"));

            ApplicantDTO dto = new ApplicantDTO();

            dto.setApplicationId(app.getApplicationId());
            dto.setStudentName(student.getName());
            dto.setBranch(student.getBranch());
            dto.setCgpa(student.getCgpa());
            dto.setJobTitle(job.getTitle());
            dto.setCompanyName(recruiter.getCompanyName());
            dto.setStatus(app.getStatus());

            result.add(dto);
        }

        return result;
    }
    
    @GetMapping("/job-stats/{jobId}")
    public Object jobStats(@PathVariable Integer jobId) {

        Map<String, Long> stats = new HashMap<>();

        stats.put("TOTAL_APPLICANTS",
        	    applicationRepository.countByJobId(jobId));

        stats.put("IN_PROGRESS",
                applicationRepository.countByJobIdAndStatus(jobId, "IN_PROGRESS"));

        stats.put("REJECTED",
                applicationRepository.countByJobIdAndStatus(jobId, "REJECTED"));

        stats.put("SELECTED",
                applicationRepository.countByJobIdAndStatus(jobId, "SELECTED"));

        return stats;
    }
    
    @GetMapping("/filter-applicants")
    public Object filterApplicants(@RequestParam Integer jobId,
                                   @RequestParam String status) {

        return applicationRepository.findByJobIdAndStatus(jobId, status.toUpperCase());
    }
    
    @GetMapping("/candidate-rounds/{applicationId}")
    public Object viewCandidateRounds(@PathVariable Integer applicationId) {

        return roundRepository.findByApplicationId(applicationId);
    }
    
    @GetMapping("/round-summary")
    public Object roundSummary(@RequestParam String roundName) {

        Map<String, Long> summary = new HashMap<>();

        summary.put("PASSED",
                roundRepository.countByRoundNameAndResult(roundName, "PASS"));

        summary.put("FAILED",
                roundRepository.countByRoundNameAndResult(roundName, "FAIL"));

        return summary;
    }
}