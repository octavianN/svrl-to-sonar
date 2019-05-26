package ro.ctalau;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.sonarsource.analyzer.commons.xml.XmlFile;

public class XmlFileUtil {

  public static XmlFile createXmlFile(String fileName) throws IOException {
    byte[] fileBytes = Files.readAllBytes(Paths.get(fileName));
    XmlFile file = XmlFile.create(new String(fileBytes, StandardCharsets.UTF_8));
    return file;
  }
}
