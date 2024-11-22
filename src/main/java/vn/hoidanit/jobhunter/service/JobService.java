package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.res.RestUpdateResumeResponce;
import vn.hoidanit.jobhunter.domain.res.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.service.error.RemoteEntityNotFound;

@Service
public class JobService {
  private final JobRepository jobRepository;
  private final SkillRepository skillRepository;
  private final CompanyRepository companyRepository;
  public JobService(CompanyRepository companyRepository,JobRepository jobRepository, SkillRepository skillRepository) {
    this.jobRepository = jobRepository;
    this.skillRepository = skillRepository;
    this.companyRepository = companyRepository;
  }

  public Job handleSaveJob(Job job) {
    if (job.getSkills() != null) {
      List<Skill> listSkill = new ArrayList<>();
      for (Skill skill : job.getSkills()) {
        if (this.skillRepository.findById(skill.getId()).isPresent()) {
          listSkill.add(this.skillRepository.findById(skill.getId()).get());
        }
      }
      job.setSkills(listSkill);
    }
    return this.jobRepository.save(job);
  }


  public Job handleUpdateJob(Job updatedJob) throws RemoteEntityNotFound{
    Job jobDb = this.getJobById(updatedJob.getId());
    if(jobDb == null){
      throw new RemoteEntityNotFound("Job not found");
    }

    if(updatedJob.getSkills() != null){
      List<Long> skillId = updatedJob.getSkills()
        .stream().map(x -> x.getId())
        .collect(Collectors.toList());
      List<Skill> skills = this.skillRepository.findByIdIn(skillId);
      jobDb.setSkills(skills);
    }
    if(updatedJob.getCompany() != null){
      Optional<Company> company = this.companyRepository.findById(updatedJob.getCompany().getId());
      if(company.isPresent()){
        jobDb.setCompany(company.get());
      }
    }

    jobDb.setName(updatedJob.getName());
    jobDb.setSalary(updatedJob.getSalary());
    jobDb.setQuantity(updatedJob.getQuantity());
    jobDb.setLocation(updatedJob.getLocation());
    jobDb.setLevel(updatedJob.getLevel());
    jobDb.setStartDate(updatedJob.getStartDate());
    jobDb.setEndDate(updatedJob.getEndDate());
    jobDb.setActive (updatedJob.isActive());


    return this.jobRepository.save(jobDb);
  }
  public ResultPaginationDTO FetchAllJobs(Specification spec, Pageable pageable) {
    Page<Skill> pageJob = this.jobRepository.findAll(spec, pageable);
    ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

    meta.setPage(pageJob.getNumber() + 1);
    meta.setPageSize(pageJob.getNumberOfElements());
    meta.setPages(pageJob.getTotalPages());
    meta.setTotal(pageJob.getTotalElements());
    ResultPaginationDTO res = new ResultPaginationDTO();
    res.setMeta(meta);
    res.setResult(pageJob.getContent());
    return res;
  }

    public Job getJobById(long id) {
        Optional<Job> jobOptional = this.jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            return jobOptional.get();
        }
        return null;
    }
}
