package com.hit.compiler.util;

import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil {
  private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

  public static final Path RESOURCES_PATH = CURRENT_FOLDER.resolve(Paths.get("src/main/resources"));

  //Zip a folder or multiple folder
  // Example zipFolder(["upload/xxx/xxx", "upload/xxx/xxx"])
  public static byte[] zipFolder(String... sourceFolderPaths) throws IOException {
    List<File> fileList = new ArrayList<>();
    for (String pathFolder : sourceFolderPaths) {
      File sourceFolder = new File(RESOURCES_PATH.resolve(pathFolder).toString());
      fileList.add(sourceFolder);
    }

    if (CollectionUtils.isNotEmpty(fileList)) {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
      try {
        for (File fileFolder : fileList) {
          addToZip(fileFolder.getParentFile().getPath(), zipOutputStream, fileFolder);
        }
        zipOutputStream.close();
        return byteArrayOutputStream.toByteArray();
      } catch (IOException ioe) {
        throw new IOException("Something went wrong, file cannot be compressed", ioe);
      }
    }
    return null;
  }

  //Zip a file or multiple file
  // Example zipFileByPath(["upload/xxx/fileName.xxx", "upload/xxx/fileName.xxx"])
  public static byte[] zipFileByPath(String... filePaths) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
    try {
      for (String pathFile : filePaths) {
        File file = getFileByPath(pathFile);
        addToZip(file.getParentFile().getPath(), zipOutputStream, file);
      }
      zipOutputStream.close();
      return byteArrayOutputStream.toByteArray();
    } catch (IOException ioe) {
      throw new IOException("Something went wrong, file cannot be compressed", ioe);
    }
  }

  //Save file upload to Resources
  //Example: saveFile("xxx", "upload/xxx/xxx", file)
  public static String saveFile(String newFileName, String uploadPath, MultipartFile multipartFile) throws IOException {
    Path path = RESOURCES_PATH.resolve(Paths.get(uploadPath));
    if (!Files.exists(path)) {
      Files.createDirectories(path);
    }
    String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
    String fileType = fileName.substring(fileName.lastIndexOf("."));
    Path filePath;
    try (InputStream inputStream = multipartFile.getInputStream()) {
      filePath = path.resolve(newFileName + fileType);
      Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException ioe) {
      throw new IOException("Could not save file: " + fileName, ioe);
    }
    return filePath.toString();
  }

  //Check folder in Resources
  //Example: isFolderNotExists("upload/xxx/xxx")
  public static boolean isFolderNotExists(String folderPath) {
    Path path = RESOURCES_PATH.resolve(Paths.get(folderPath));
    return !Files.exists(path);
  }

  public static String formatPath(String folder, Long subjectId, Long lessonId) {
    return folder + "/" + subjectId + "_" + lessonId;
  }

  public static String formatPath(String folder, Long subjectId, Long lessonId, String studentId) {
    return folder + "/" + subjectId + "_" + lessonId + "/" + studentId;
  }

  public static String buildFileName(Long subjectId, Long lessonId, String fileType) {
    return subjectId + "_" + lessonId + fileType;
  }

  public static String buildFileName(String studentId, String fileType) {
    return studentId + fileType;
  }

  //Tạo và thêm các entry là file hoặc folder vào zip
  private static void addToZip(String basePath, ZipOutputStream zos, File toAdd) throws IOException {
    if (toAdd.isDirectory()) {
      // Nếu file là folder thì lấy tất cả các file và tiếp tục add
      File[] files = toAdd.listFiles();
      if (files != null) {
        for (File file : files) {
          addToZip(basePath, zos, file);
        }
      }
    } else {
      // Bỏ phần basePath để lấy name file
      String name = toAdd.getPath().substring(basePath.length() + 1);
      ZipEntry entry = new ZipEntry(name);
      zos.putNextEntry(entry);
      // Copy file vào entry trong zip output vừa put
      Files.copy(Paths.get(toAdd.getPath()), zos);
      zos.closeEntry();
    }
  }

  // Lấy ra file ở trong thư mục resources theo đường dẫn
  private static File getFileByPath(String pathFile) {
    Path path = RESOURCES_PATH.resolve(Paths.get(pathFile));
    return path.toFile();
  }

  @SneakyThrows
  public static File convertMultipartToFile(MultipartFile file) {
    File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(file.getBytes());
    fos.close();
    return convFile;
  }

  @SneakyThrows
  public static File writeToFile(String content, String fileName) {
    Path path = Paths.get(fileName);
    Files.write(path, content.getBytes());
    return path.toFile();
  }

  public static void zipFile(List<File> files, final File targetZipFile) throws IOException {
    try {
      FileOutputStream   fos = new FileOutputStream(targetZipFile);
      ZipOutputStream zos = new ZipOutputStream(fos);
      byte[] buffer = new byte[128];
      for (File currentFile : files) {
        if (!currentFile.isDirectory()) {
          ZipEntry entry = new ZipEntry(currentFile.getName());
          FileInputStream fis = new FileInputStream(currentFile);
          zos.putNextEntry(entry);
          int read = 0;
          while ((read = fis.read(buffer)) != -1) {
            zos.write(buffer, 0, read);
          }
          zos.closeEntry();
          fis.close();
        }
      }
      zos.close();
      fos.close();
    } catch (FileNotFoundException e) {
      System.out.println("File not found : " + e);
    }

  }

}
