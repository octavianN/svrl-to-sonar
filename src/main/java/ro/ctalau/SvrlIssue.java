package ro.ctalau;

import org.w3c.dom.Element;

public class SvrlIssue {

  
  
  private Element failedAssert;

  public SvrlIssue(Element failedAssert) {
    this.failedAssert = failedAssert;
  }
  
  public String getRole() {
    String attribute = failedAssert.getAttribute("role");
	return attribute == "" ? "error" :  attribute;
  }
  
  public String getMessage() {
    return failedAssert.getTextContent().replaceAll("\\s+", " ").trim();
  }
  
  public String getLocationXPath() {
    String expr = failedAssert.getAttribute("location");
    // Get rid of namespaces - the default XPath implementation cannot handle namespaces well.
    return expr.replaceAll("\\*:([a-zA-Z0-9]+)", "*[local-name()='$1']");
  }
}
