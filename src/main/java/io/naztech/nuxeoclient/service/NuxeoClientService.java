package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.IOException;

import javax.inject.Named;

import org.nuxeo.client.NuxeoClient;
import org.nuxeo.client.objects.Document;
import org.nuxeo.client.objects.upload.BatchUpload;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.FileBlob;

import io.naztech.nuxeoclient.model.DocumentWrapper;
import lombok.Setter;

/**
 * TODO ISSUE can not get custom attributes (properties) using
 * getDocumentById/Path call.
 * 
 * @author armaan.choudhury
 * @since 2019-04-15
 */
@Named
@Setter
public class NuxeoClientService {
	private NuxeoClient nuxeoClient;
	private Session clientSession;

	public Document createDocument(DocumentWrapper req) {
		if (req.getFiles().isEmpty())
			try {
				throw new NuxeoDocumentServiceException("Document request must contain at least one file attachment");
			} catch (NuxeoDocumentServiceException e1) {
				return null;
			}
		Document doc = req.buildDocument();
		/* Create a document in Nuxeo platform repository */
		doc = nuxeoClient.repository().createDocumentByPath(req.getRepoPath(), req.buildDocument());
		/* Attach file(s) to the document created in repository */
		try {
			return req.getFiles().size() == 1 ? uploadOneFile(doc, req.getFiles().get(0))
					: uploadMultipleFiles(doc, req);
		} catch (NuxeoDocumentServiceException e) {
			return null;
		}
	}

	// TODO what response need to send to service caller
	private Document uploadOneFile(Document doc, File file) {
		/* Create batch */
		BatchUpload batch = nuxeoClient.batchUploadManager().createBatch();

		/* Upload file attachment to batch */
		batch = batch.upload("1", new org.nuxeo.client.objects.blob.FileBlob(file));

		/* Attach file(s) to the document uploaded into batch */
		doc.setPropertyValue("file:content", batch.getBatchBlob());
		return doc.updateDocument();
	}

	private Document uploadMultipleFiles(Document doc, DocumentWrapper req) throws NuxeoDocumentServiceException {
		DocRef docRef = new DocRef(doc.getId());
		DocumentService autoService = clientSession.getAdapter(DocumentService.class);
		// TODO maybe save first file in "file:content" and rest in <schemaTag>:files
		for (File file : req.getFiles()) {
			if (!file.isFile())
				continue;
			try {
				autoService.setBlob(docRef, new FileBlob(file), req.getType() + ":files");
			} catch (IOException e) {
				throw new NuxeoDocumentServiceException(e);
			}
		}
		return doc;
	}

	public Document getDocument(String pathOrId) {
		Document ob = nuxeoClient.repository().fetchDocumentById(pathOrId);
		if (ob == null)
			ob = nuxeoClient.repository().fetchDocumentByPath(pathOrId);
		return ob;
	}
}
