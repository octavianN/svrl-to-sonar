package ro.ctalau;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.sonarsource.analyzer.commons.xml.XmlTextRange;

public class ConvertTest {

  
  @Test
  public void testIssueGeneration() throws Exception {
    SvrlFile svrlFile = new SvrlFile("test/simple/paper.svrl");
    
    List<SvrlIssue> issues = svrlFile.findFailedAsserts();
    assertEquals(1, issues.size());
    assertEquals("It is recommended to have at least 30 words!", issues.get(0).getMessage());
    
    Convert convert = new Convert(svrlFile.findCheckedFilePath());
    SonarIssue sonarIssue = convert.failedAssertToSonarIssue(issues.get(0));
    
    assertEquals("MINOR", sonarIssue.severity);
    
    XmlTextRange textRange = sonarIssue.primaryLocation.textRange;
    assertEquals(23, textRange.getStartLine());
    assertEquals(2, textRange.getStartColumn());
    assertEquals(26, textRange.getEndLine());
    assertEquals(13, textRange.getEndColumn());
  }
}
