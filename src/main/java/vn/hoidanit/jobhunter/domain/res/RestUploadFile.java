package vn.hoidanit.jobhunter.domain.res;

import java.time.Instant;

public class RestUploadFile {

  private String fileName;
  private Instant createdAt;

  public RestUploadFile() {}

  public RestUploadFile(String fileName, Instant createdAt) {
    this.fileName = fileName;
    this.createdAt = createdAt;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
