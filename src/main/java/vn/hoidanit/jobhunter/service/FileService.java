package vn.hoidanit.jobhunter.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vn.hoidanit.jobhunter.service.error.StorageException;

@Service
public class FileService {
  public void createUploadFolder(String folder) throws URISyntaxException {
    URI uri = new URI(folder);
    Path path = Paths.get(uri);
    File tmpDir = new File(path.toString());
    if (!tmpDir.isDirectory()) {
      try {
        Files.createDirectory(tmpDir.toPath());
        System.out.println(">>> CREATE NEW DIRECTORY SUCCESSFUL, PATH = " + tmpDir.toPath());
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println(">>> SKIP MAKING DIRECTORY, ALREADY EXISTS");
    }
  }

  public String store(MultipartFile file, String folder, String basePath)
      throws URISyntaxException, IOException {
    @SuppressWarnings("deprecation")
    String standardFileName = URLEncoder.encode(file.getOriginalFilename()); // handle ??
    String finalName = System.currentTimeMillis() + "-" + standardFileName;
    URI uri = new URI(basePath + folder + "/" + finalName);
    Path path = Paths.get(uri);
    try (InputStream inputStream = file.getInputStream()) {
      Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
    }
    return finalName;
  }
  public long getFileLength(String folder,String filename)throws URISyntaxException, IOException, StorageException{
    URI uri = new URI(folder + "/" + filename);
    Path path = Paths.get(uri);
    File tmpDir = new File(path.toString());
    if(tmpDir.isDirectory() || !tmpDir.exists()){
      return 0;
    }
    return tmpDir.length();
  }
  public InputStreamResource getResource(String folder,String filename) throws URISyntaxException, IOException{
    URI uri = new URI(folder + "/" + filename);
    Path path = Paths.get(uri);
    File file = new File(path.toString());
    return new InputStreamResource(new FileInputStream(file));
  }
}