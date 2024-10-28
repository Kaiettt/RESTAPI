package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.res.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.service.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;
    public JobController(JobService jobService){
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    public ResponseEntity<Job> createSkill(@RequestBody Job postJob) {    
         return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.handleSaveJob(postJob));
    }
    @GetMapping("/jobs")
    @ApiMessage("Fetch All Jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.FetchAllJobs(spec, pageable));
    }
}
