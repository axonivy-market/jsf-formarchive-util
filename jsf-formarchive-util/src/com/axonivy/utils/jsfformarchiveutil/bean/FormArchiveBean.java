package com.axonivy.utils.jsfformarchiveutil.bean;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.function.Predicate;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.apache.commons.lang3.StringUtils;

import com.axonivy.utils.jsfformarchiveutil.util.FacesContexts;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.exec.Sudo;
import ch.ivyteam.ivy.workflow.document.IDocument;
import ch.ivyteam.ivy.workflow.document.IDocumentService;

/**
 * Bean that saves the submitted Form in case documents
 */
@ManagedBean
@RequestScoped
public class FormArchiveBean {

	private static final String COMMA = ",";
	private static final String BMP_EXTENSION = ".bmp";
	private static final String LOGIC_CLOSE_METHOD = "#{logic.close}";

	public String getContent() {
		return EMPTY;
	}

	public void setContent(String content) {
		if (StringUtils.isNoneBlank(content)) {
			String documentName = Ivy.wfTask().getName();
			if (documentName.isEmpty()) {
				documentName = getDefaultDocumentName();
			}
			documentName = documentName + BMP_EXTENSION;
			byte[] imageData = Base64.getDecoder().decode(content.substring(content.indexOf(COMMA) + 1));
			getCurrentCaseDocuments().getAll().removeIf(filterByDocumentName(documentName));
			addNewDocToCurrentCase(documentName, imageData);
		}
	}

	private void addNewDocToCurrentCase(String documentName, byte[] imageData) {
		Sudo.run(() -> {
			getCurrentCaseDocuments().add(documentName).write().withContentFrom(new ByteArrayInputStream(imageData));
		});
	}

	private String getDefaultDocumentName() {
		return Ivy.cms().co("/Labels/Form") + getCurrentCaseDocuments().getAll().size();
	}

	private IDocumentService getCurrentCaseDocuments() {
		return Ivy.wfCase().documents();
	}

	private Predicate<? super IDocument> filterByDocumentName(String documentName) {
		return doc -> doc.getName().endsWith(documentName);
	}

	public void invokeSubmitMethod(String methodExpression) {
		if (StringUtils.isBlank(methodExpression)) {
			methodExpression = LOGIC_CLOSE_METHOD;
		}
		FacesContexts.invokeMethodByExpression(methodExpression, new Object[] {}, null);
	}
}
