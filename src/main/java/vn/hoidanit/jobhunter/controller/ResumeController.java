package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.request.NewResumeRequest;
import vn.hoidanit.jobhunter.domain.res.NewResumeResponce;
import vn.hoidanit.jobhunter.domain.res.RestFetchResume;
import vn.hoidanit.jobhunter.domain.res.RestUpdateResumeResponce;
import vn.hoidanit.jobhunter.domain.res.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.service.annotation.ApiMessage;
import vn.hoidanit.jobhunter.service.error.HandleNumber;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;
import vn.hoidanit.jobhunter.service.error.RemoteEntityNotFound;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;
    public ResumeController(ResumeService resumeService){
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create Resume")
    public ResponseEntity<NewResumeResponce> createNewResume(@RequestBody NewResumeRequest newResume) throws RemoteEntityNotFound {    
         return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.handleSaveResume(newResume));
    }

    
  @GetMapping("/resumes/{id}")
  @ApiMessage("Get Resume")
  public ResponseEntity<RestFetchResume> getResume(@PathVariable("id") String id) throws IdInvalidException,RemoteEntityNotFound {
    if (!HandleNumber.isNumberic(id)) {
      throw new IdInvalidException("The provided ID must be a numeric value.");
    }

    Long realId = Long.valueOf(id);

    // Validate if the ID is within an acceptable range
    if (realId >= 1500) {
      throw new IdInvalidException("Invalid ID. The ID must be less than 1500.");
    }
    return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.getResumeById(realId));
  }
    @PutMapping("/resumes")
    @ApiMessage("Update Resume")
    public ResponseEntity<RestUpdateResumeResponce> updateUser(@RequestBody Resume updatedResume)
            throws RemoteEntityNotFound {
        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.handleUpdateResume(updatedResume));
    }
    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete Resume")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") String id)
            throws IdInvalidException, RemoteEntityNotFound {
        // Check if the ID is numeric
        if (!HandleNumber.isNumberic(id)) {
            throw new IdInvalidException("The provided ID must be a numeric value.");
        }
        Long realId = Long.valueOf(id);

        // Validate if the ID is within an acceptable range
        if (realId >= 1500) {
            throw new IdInvalidException("Invalid ID. The ID must be less than 1500.");
        }

        // Call the service to handle user deletion
        this.resumeService.DeleteResumeById(realId);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/resumes")
  @ApiMessage("Fetch All Resumes")
  public ResponseEntity<ResultPaginationDTO> getAllSkills(
      @Filter Specification<Resume> spec, Pageable pageable) throws RemoteEntityNotFound {
    return ResponseEntity.status(HttpStatus.OK)
        .body(this.resumeService.FetchAllReumes(spec, pageable));
  }

  @GetMapping("/resumes/by-user")
  public ResponseEntity<ResultPaginationDTO> getResumesByUser(Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(this.resumeService.fetResumesByUser(pageable));
  }
  
}
