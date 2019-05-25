package ro.ctalau;

import java.util.Map;

import com.google.common.collect.ImmutableMap;


public class SonarIssue {
  private static Map<String, String> severityMapping = ImmutableMap.<String, String>builder()
      .put("FATAL", "Blocker")
      .put("ERROR", "Critical")
      .put("WARN", "Minor")
      
      .put("CAUTION", "Minor")
      .put("INFO", "Minor")
      .put("HINT", "Minor")
      .put("TRACE", "Minor")
      .put("DEBUG", "Minor")
      .build();
  
  public String engineId = "Schematron";
  public String type = "CODE SMELL";
  public int effortMinutes = 5;
  
  public String severity;
  public String ruleId;
  public SonarIssueLocation primaryLocation;
  
  
  public SonarIssue(String ruleId, String schematronSeverity, SonarIssueLocation primaryLocation) {
    this.ruleId = ruleId;
    this.severity = severityMapping.get(schematronSeverity.toUpperCase());
    this.primaryLocation = primaryLocation;
  }
}
