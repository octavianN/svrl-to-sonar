package ro.ctalau;

import java.util.Map;

import com.google.common.collect.ImmutableMap;


public class SonarIssue {
  private static Map<String, String> severityMapping = ImmutableMap.<String, String>builder()
      .put("FATAL", "BLOCKER")
      .put("ERROR", "CRITICAL")
      .put("WARN", "MINOR")
      
      .put("CAUTION", "MINOR")
      .put("INFO", "MINOR")
      .put("HINT", "MINOR")
      .put("TRACE", "MINOR")
      .put("DEBUG", "MINOR")
      .build();
  
  public String engineId = "Schematron";
  public String type = "CODE_SMELL";
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
