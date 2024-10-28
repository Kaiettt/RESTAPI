package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.res.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class JobService {
  private final JobRepository jobRepository;
  private final SkillRepository skillRepository;

  public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
    this.jobRepository = jobRepository;
    this.skillRepository = skillRepository;
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
}
