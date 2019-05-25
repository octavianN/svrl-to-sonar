package ro.ctalau;

import org.sonarsource.analyzer.commons.xml.XmlTextRange;

public class SonarIssueLocation {
  public String message;
  public String filePath;
  public XmlTextRange textRange;
  
  public SonarIssueLocation(String message, String filePath, XmlTextRange textRange) {
    this.message = message.replaceAll("\\s+", " ").trim();
    this.filePath = filePath;
    this.textRange = textRange;
  }
}
