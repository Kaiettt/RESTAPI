package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.request.NewResumeRequest;
import vn.hoidanit.jobhunter.domain.res.NewResumeResponce;
import vn.hoidanit.jobhunter.domain.res.RestFetchResume;
import vn.hoidanit.jobhunter.domain.res.RestUpdateResumeResponce;
import vn.hoidanit.jobhunter.domain.res.ResultPaginationDTO;
import vn.hoidanit.jobhunter.mapper.ResumeMapper;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.service.error.RemoteEntityNotFound;
import vn.hoidanit.jobhunter.util.SecurityUtil;
@Service
public class ResumeService {
    private final FilterSpecificationConverter filterSpecificationConverter;
    private final FilterParser filterParser;
    private final ResumeRepository resumeRepository;
    private final JobService jobService;
    private final UserService userService;
    public ResumeService(FilterSpecificationConverter filterSpecificationConverter,FilterParser filterParser,ResumeRepository resumeRepository,JobService jobService, UserService userService) {
        this.jobService = jobService;
        this.filterSpecificationConverter = filterSpecificationConverter;
        this.filterParser = filterParser;
        this.resumeRepository = resumeRepository;
        this.userService = userService;
    }
    public NewResumeResponce handleSaveResume(NewResumeRequest newResumeRequest) throws RemoteEntityNotFound{
        Job  job = jobService.getJobById(newResumeRequest.getJob().getId());
        if(job == null){
            throw new RemoteEntityNotFound("Job not found");
        }
        User user = userService.getUserById(newResumeRequest.getUser().getId());
        if(userService.getUserById(newResumeRequest.getUser().getId()) == null){
            throw new RemoteEntityNotFound("User not found");
        }
        Resume newCreatedResume = 
            Resume.builder()
                .email(newResumeRequest.getEmail())
                .url(newResumeRequest.getUrl())
                .status(newResumeRequest.getStatus())
                .user(user)
                .job(job)
                .build();
        
        Resume newResume = this.resumeRepository.save(newCreatedResume);

        return ResumeMapper.INSTANCE.resumeToResumeResponse(newResume);

    }
    public RestFetchResume getResumeById(long id) throws RemoteEntityNotFound {
        Optional<Resume> resumeOptional = this.resumeRepository.findById(id);
        if (!resumeOptional.isPresent()) {
            throw new RemoteEntityNotFound("Resume Not FOUND");
        }
        Resume dbResume = resumeOptional.get();
    
        // Map Resume's Job and User to RestFetchResume's JobResume and UserResume
        RestFetchResume.JobResume jobResume = new RestFetchResume.JobResume(
            dbResume.getJob().getId(),
            dbResume.getJob().getName()
        );
    
        RestFetchResume.UserResume userResume = new RestFetchResume.UserResume(
            dbResume.getUser().getId(),
            dbResume.getUser().getName()
        );
    
        // Create RestFetchResume using mapped fields
        RestFetchResume res = new RestFetchResume(
            dbResume.getJob().getCompany().getName(),
            dbResume.getId(),
            dbResume.getEmail(),
            dbResume.getUrl(),
            dbResume.getStatus(),
            dbResume.getCreatedAt(),
            dbResume.getUpdatedAt(),
            dbResume.getCreatedBy(),
            dbResume.getUpdatedBy(),
            jobResume,
            userResume
        );
    
        return res;
    }
    
    public void DeleteResumeById(long id) throws RemoteEntityNotFound{
        Optional<Resume> resumeOptional = this.resumeRepository.findById(id);
        if(!resumeOptional.isPresent()){
            throw new RemoteEntityNotFound("Resume Not FOUND");
        }
        this.resumeRepository.deleteById(id);
    }
    public ResultPaginationDTO FetchAllReumes(Specification spec, Pageable pageable) throws RemoteEntityNotFound{
        Page<Resume> resumePage = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(resumePage.getTotalPages());
        meta.setTotal(resumePage.getTotalElements());
        ResultPaginationDTO res = new ResultPaginationDTO();
        res.setMeta(meta);
        List<Resume> resumes = resumePage.getContent();
        List<RestFetchResume> resumesRes = new ArrayList();
        for (Resume resume : resumes) {
            RestFetchResume resumeResponce = this.getResumeById(resume.getId());
            resumesRes.add(resumeResponce);
        }
        res.setResult(resumesRes);
        return res;
    }
    public RestUpdateResumeResponce handleUpdateResume(Resume updatedResume) throws RemoteEntityNotFound{
        Optional<Resume> optionalResume = this.resumeRepository.findById(updatedResume.getId());
        if(!optionalResume.isPresent()){
            throw new RemoteEntityNotFound("Resume not found");
        }
        Resume dbResume = optionalResume.get();
        dbResume.setStatus(updatedResume.getStatus());
        dbResume = this.resumeRepository.save(dbResume);
        RestUpdateResumeResponce res = new RestUpdateResumeResponce();
        res.setId(dbResume.getId());
        res.setStatus(dbResume.getStatus());
        return res;
    }

    public ResultPaginationDTO fetResumesByUser(Pageable pageable){
        String email =
        SecurityUtil.getCurrentUserLogin().isPresent()
            ? SecurityUtil.getCurrentUserLogin().get()
            : "";
        String filter = "email='"+email+"'";
        FilterNode node = filterParser.parse(filter);
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> resumePage = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(resumePage.getTotalPages());
        meta.setTotal(resumePage.getTotalElements());
        ResultPaginationDTO res = new ResultPaginationDTO();
        res.setMeta(meta);
        List<Resume> resumes = resumePage.getContent();
        // List<RestFetchResume> resumesRes = new ArrayList();
        // for (Resume resume : resumes) {
        //     RestFetchResume resumeResponce = 
        //     resumesRes.add(resumeResponce);
        // }
        List<NewResumeResponce> resumeRes =  ResumeMapper.INSTANCE.listResumeToListResumeResponse(resumes);
        res.setResult(resumeRes);
        return res;
    }
}
