package vn.hoidanit.jobhunter.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.hoidanit.jobhunter.domain.res.RestUploadFile;
import vn.hoidanit.jobhunter.service.FileService;
import vn.hoidanit.jobhunter.service.error.StorageException;

@RestController
@RequestMapping("/api/v1")
public class FileController {
  @Value("${hoidanit.upload-file.base-uri}")
  private String basePath;

  private final FileService fileService;

  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @PostMapping("/files")
  public ResponseEntity<RestUploadFile> upload(
      @RequestParam("file") MultipartFile file, @RequestParam("folder") String folder)
      throws URISyntaxException, IOException, StorageException {
    if (file == null || file.isEmpty()) {
      throw new StorageException("File is empty");
    }
    String originFileName = file.getOriginalFilename();
    List<String> allowedExtensions = Arrays.asList(".pdf", ".jpeg", ".png", "jpg", ".docx", ".doc");
    boolean isValid = allowedExtensions.stream().anyMatch(item -> originFileName.endsWith(item));
    if (!isValid) {
      throw new StorageException(
          "File format is not valid, please choose another file or convert to proper format");
    }
    fileService.createUploadFolder(basePath + folder);
    String fileName = fileService.store(file, folder, basePath);
    RestUploadFile res = new RestUploadFile();
    res.setFileName(fileName);
    res.setCreatedAt(Instant.now());
    return ResponseEntity.status(HttpStatus.CREATED).body(res);
  }
}
