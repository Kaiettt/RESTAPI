package vn.hoidanit.jobhunter.domain.res;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import vn.hoidanit.jobhunter.util.constant.ResumeStateEnum;

public class RestUpdateResumeResponce {
    private long id;
    @Enumerated(EnumType.STRING)
    private ResumeStateEnum status;
    public RestUpdateResumeResponce(long id, ResumeStateEnum status) {
        this.id = id;
        this.status = status;
    }
    public RestUpdateResumeResponce() {
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public ResumeStateEnum getStatus() {
        return status;
    }
    public void setStatus(ResumeStateEnum status) {
        this.status = status;
    }
}
