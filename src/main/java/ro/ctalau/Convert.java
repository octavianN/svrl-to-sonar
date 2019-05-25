package ro.ctalau;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.XmlTextRange;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;


public class Convert {

  private static final XPath xpath = XPathFactory.newInstance().newXPath();
  
  
  public static void main(String[] args) throws IOException, XPathExpressionException {
    String svrlFileName = "/home/ctalau/repo/markupuk-2019-paper/bin/tmp/paper.svrl";
    
    XmlFile svrlFile = createXmlFile(svrlFileName);
    
    // Find the file that was checked.
    XPathExpression findActivePatternDocument = xpath.compile(
        "//*[local-name() = 'active-pattern']/@document");
    String checkedFile = (String) findActivePatternDocument
        .evaluate(svrlFile.getDocument(), XPathConstants.STRING);
    String checkedFilePath = checkedFile.replace("file:", "");
    
    
    // Find failed asserts.
    XPathExpression findFailedAsserts = xpath.compile(
        "//*[local-name() = 'failed-assert']");
    List<Node> failedAsserts = XmlFile.asList((NodeList) 
        findFailedAsserts.evaluate(svrlFile.getDocument(), XPathConstants.NODESET));
    
    XmlFile file = createXmlFile(checkedFilePath);
    Document document = file.getDocument();

    List<SonarIssue> issues = failedAsserts.stream()
      .map(failedAssert -> failedAssertToSonarIssue(failedAssert, document))
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
    
    String issuesJson = new ObjectMapper().writeValueAsString(ImmutableMap.of("issues", issues));
    System.out.println(issuesJson);
  }

  
  private static SonarIssue failedAssertToSonarIssue(Node failedAssert, Document document) {
    Element failedAssertElement = (Element)failedAssert;
    String expr = failedAssertElement.getAttribute("location");
    // Get rid of namespaces - the default XPath implementation cannot handle namespaces well.
    expr = expr.replaceAll("\\*:([a-zA-Z0-9]+)", "*[local-name()='$1']");
    
    SonarIssue issue = null;
    try {
      Node matchingNode = (Node) xpath.compile(expr).evaluate(document, XPathConstants.NODE);
      XmlTextRange nodeLocation = XmlFile.nodeLocation(matchingNode);
      
      SonarIssueLocation location = new SonarIssueLocation(
          failedAssertElement.getTextContent(), "paper.xml", nodeLocation);
      issue = new SonarIssue("id", failedAssertElement.getAttribute("role"), 
          location);
    } catch (XPathExpressionException e) {
    }
    return issue;
  }


  private static XmlFile createXmlFile(String fileName) throws IOException {
    byte[] fileBytes = Files.readAllBytes(Paths.get(fileName));
    XmlFile file = XmlFile.create(new String(fileBytes, StandardCharsets.UTF_8));
    return file;
  }
}
