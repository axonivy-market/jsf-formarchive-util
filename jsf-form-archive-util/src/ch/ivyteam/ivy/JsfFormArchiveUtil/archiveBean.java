package ch.ivyteam.ivy.JsfFormArchiveUtil;

import java.io.ByteArrayInputStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import ch.ivyteam.ivy.environment.Ivy;

/**
 * Bean that saves the submitted Form in case documents
 */
@ManagedBean
@RequestScoped
public class archiveBean {

  String documentName = "";

  public String getContent()
  {
    return "";
  }

  public void setContent(String content) {
    if (!content.isEmpty()) {
      documentName = Ivy.wfTask().getName();
      if(documentName.isEmpty())  {
        documentName = "Form"+Ivy.wfCase().documents().getAll().size();
      }
      documentName = documentName+".bmp";
      byte[] imagedata = java.util.Base64.getDecoder().decode(content.substring(content.indexOf(",") + 1));
      Ivy.wfCase().documents().getAll().removeIf(doc->doc.getName().endsWith(documentName));
      Ivy.wfCase().documents().add(documentName).write()
              .withContentFrom(new ByteArrayInputStream(imagedata));
    }
  }
}
