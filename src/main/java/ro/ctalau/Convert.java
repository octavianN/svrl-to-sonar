package ro.ctalau;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;


public class Convert {

  private static final String CHECKED_DOCUMENT_PATH = "paper.xml";
  
  private static final XPath xpath = XPathFactory.newInstance().newXPath();

  private Document document;
  
  
  public static void main(String[] args) throws IOException, XPathExpressionException {
    String svrlFileName = args[0];
    
    SvrlFile svrlFile = new SvrlFile(svrlFileName);
    
    // Find failed asserts.
    List<SvrlIssue> failedAsserts = svrlFile.findFailedAsserts();

    // Create the converter
    Convert convert = new Convert(svrlFile.findCheckedFilePath());
    
    List<SonarIssue> issues = failedAsserts.stream()
      .map(convert::failedAssertToSonarIssue)
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
    
    String issuesJson = new ObjectMapper().writeValueAsString(ImmutableMap.of("issues", issues));
    System.out.println(issuesJson);
  }

  public Convert(String checkedFilePath) throws IOException {
    document = XmlFileUtil.createXmlFile(checkedFilePath).getDocument();
  }
  
  public SonarIssue failedAssertToSonarIssue(SvrlIssue failedAssert) {
    SonarIssue issue = null;
    try {
      String expr = failedAssert.getLocationXPath();
      Node matchingNode = (Node) xpath.compile(expr).evaluate(document, XPathConstants.NODE);
      
      SonarIssueLocation location = new SonarIssueLocation(
          failedAssert.getMessage(), 
          CHECKED_DOCUMENT_PATH, 
          XmlFile.nodeLocation(matchingNode));
      
      issue = new SonarIssue("id", failedAssert.getRole(), 
          location);
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
    return issue;
  }
}
