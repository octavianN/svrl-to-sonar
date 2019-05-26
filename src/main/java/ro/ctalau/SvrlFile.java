package ro.ctalau;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SvrlFile {
  
  private static final XPath xpath = XPathFactory.newInstance().newXPath();
  private XmlFile svrlFile;

  
  public SvrlFile(String svrlFileName) throws IOException {
    svrlFile = XmlFileUtil.createXmlFile(svrlFileName);
  }
  
  public List<SvrlIssue> findFailedAsserts() throws XPathExpressionException {
    XPathExpression findFailedAsserts = xpath.compile(
        "//*[local-name() = 'failed-assert']");
    List<Node> failedAsserts = XmlFile.asList((NodeList) 
        findFailedAsserts.evaluate(svrlFile.getDocument(), XPathConstants.NODESET));
    
    return failedAsserts.stream()
        .map(failedAssert -> new SvrlIssue((Element) failedAssert))
        .collect(Collectors.toList());
  }


  public String findCheckedFilePath() throws XPathExpressionException {
    XPathExpression findActivePatternDocument = xpath.compile(
        "//*[local-name() = 'active-pattern']/@document");
    String checkedFile = (String) findActivePatternDocument
        .evaluate(svrlFile.getDocument(), XPathConstants.STRING);
    String checkedFilePath = checkedFile.replace("file:", "");
    return checkedFilePath;
  }

}
