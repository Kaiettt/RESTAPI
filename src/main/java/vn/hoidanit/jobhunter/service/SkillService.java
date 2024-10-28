package vn.hoidanit.jobhunter.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.res.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.service.error.EmailExistedException;

@Service
public class SkillService {
  private final SkillRepository skillRepository;

  public SkillService(SkillRepository skillRepository) {
    this.skillRepository = skillRepository;
  }

  public Skill.SkillResponce handleSaveSkill(Skill skill) throws EmailExistedException {
    if (this.skillRepository.findByName(skill.getName()) != null) {
      throw new EmailExistedException("Skill aready existed");
    }
    skill = this.skillRepository.save(skill);
    Skill.SkillResponce responce = new Skill.SkillResponce();
    responce.setName(skill.getName());
    return responce;
  }

  public ResultPaginationDTO FetchAllSKills(Specification spec, Pageable pageable) {
    Page<Skill> pageSKil = this.skillRepository.findAll(spec, pageable);
    ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

    meta.setPage(pageSKil.getNumber() + 1);
    meta.setPageSize(pageSKil.getNumberOfElements());
    meta.setPages(pageSKil.getTotalPages());
    meta.setTotal(pageSKil.getTotalElements());
    ResultPaginationDTO res = new ResultPaginationDTO();
    res.setMeta(meta);
    res.setResult(pageSKil.getContent());
    return res;
  }

  public Skill FetchSkillById(long id) {
    Optional<Skill> skillOptional = this.skillRepository.findById(id);
    if (skillOptional.isPresent()) {
      return skillOptional.get();
    }
    return null;
  }

  public Skill handleUpdateSkill(Skill skill) throws EntityNotFoundException {
    Optional<Skill> skillOptional = this.skillRepository.findById(skill.getId());
    if (skillOptional.isPresent()) {
      throw new EntityNotFoundException("Skill not found");
    }
    Skill dbSKill = skillOptional.get();
    dbSKill.setName(skill.getName());
    dbSKill = this.skillRepository.save(dbSKill);
    return dbSKill;
  }
}
