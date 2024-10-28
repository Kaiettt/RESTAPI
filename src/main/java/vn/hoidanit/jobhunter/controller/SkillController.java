package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.res.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.service.annotation.ApiMessage;
import vn.hoidanit.jobhunter.service.error.EmailExistedException;
import vn.hoidanit.jobhunter.service.error.HandleNumber;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
  private final SkillService skillService;

  public SkillController(SkillService skillService) {
    this.skillService = skillService;
  }

  @PostMapping("/skills")
  public ResponseEntity<Skill.SkillResponce> createSkill(@Valid @RequestBody Skill PostSkill)
      throws EmailExistedException {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(this.skillService.handleSaveSkill(PostSkill));
  }

  @GetMapping("/skills")
  @ApiMessage("Fetch All Skills")
  public ResponseEntity<ResultPaginationDTO> getAllSkills(
      @Filter Specification<Skill> spec, Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(this.skillService.FetchAllSKills(spec, pageable));
  }

  @GetMapping("/skills/{id}")
  public ResponseEntity<Skill> getSkill(@PathVariable("id") String id) throws IdInvalidException {
    if (!HandleNumber.isNumberic(id)) {
      throw new IdInvalidException("The provided ID must be a numeric value.");
    }

    Long realId = Long.valueOf(id);

    // Validate if the ID is within an acceptable range
    if (realId >= 1500) {
      throw new IdInvalidException("Invalid ID. The ID must be less than 1500.");
    }
    Skill skill = this.skillService.FetchSkillById(realId);
    return ResponseEntity.status(HttpStatus.OK).body(skill);
  }

  @PutMapping("/skills")
  @ApiMessage("Update Skills")
  public ResponseEntity<Skill> updateSkill(@RequestBody Skill updatedSkill)
      throws EntityNotFoundException {
    return ResponseEntity.status(HttpStatus.OK)
        .body(this.skillService.handleUpdateSkill(updatedSkill));
  }
}
