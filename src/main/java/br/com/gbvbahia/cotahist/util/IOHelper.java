package br.com.gbvbahia.cotahist.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IOHelper {

  public static List<String> getFilesNameLike(String[] possibleFiles, String like) {
    List<String> files = new ArrayList<>();
    for (String fName : possibleFiles) {
      if (StringUtils.contains(StringUtils.upperCase(fName), like)) {
        files.add(fName);
      }
    }
    return files;
  }

  public static void zipAndMoveFileToFolder(String file, String folderName) {
    File toZip = new FileSystemResource(file).getFile();
    try {
      File zip = Compactator.zip(toZip, toZip.getName());
      File dir = new File(toZip.getAbsoluteFile().getParent(), folderName);
      dir.mkdir();
      FileUtils.moveFileToDirectory(zip, dir, true);
      FileUtils.deleteQuietly(toZip);
    } catch (IOException io) {
      String error = String.format("Error trying to ZIP the file: %s", file);
      log.error(error, io);
    }
  }
}
