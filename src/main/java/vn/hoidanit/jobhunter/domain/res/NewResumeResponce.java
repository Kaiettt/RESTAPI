package vn.hoidanit.jobhunter.domain.res;

import java.time.Instant;

public class NewResumeResponce {
    private long id;
    private Instant createAt;
    private String createBy;
    public NewResumeResponce() {
    }
    public NewResumeResponce(long id, Instant createAt, String createBy) {
        this.id = id;
        this.createAt = createAt;
        this.createBy = createBy;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Instant getCreateAt() {
        return createAt;
    }
    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
  
    
}
