package vn.hoidanit.jobhunter.mapper;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.res.NewResumeResponce;
@Mapper
public interface  ResumeMapper {
    ResumeMapper INSTANCE = Mappers.getMapper(ResumeMapper.class);

    NewResumeResponce resumeToResumeResponse(Resume resume);

    List<NewResumeResponce> listResumeToListResumeResponse(List<Resume> resumes);
}