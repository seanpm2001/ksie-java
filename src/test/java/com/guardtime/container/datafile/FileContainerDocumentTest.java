package com.guardtime.container.datafile;

import com.guardtime.container.AbstractBlockChainContainerTest;
import com.guardtime.ksi.hashing.HashAlgorithm;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FileContainerDocumentTest extends AbstractBlockChainContainerTest {

    protected static final String MIME_TYPE_APPLICATION_TXT = "application/txt";
    protected static final String TEST_FILES_TEST_TXT = "test-data-files/test.txt";

    @Test
    public void testCreateFileDocumentWithoutInputFile_ThrowNullPointerException() throws Exception {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("File must be present");
        new FileContainerDocument(null, MIME_TYPE_APPLICATION_TXT);
    }

    @Test
    public void testCreateFileDocumentWithoutMimeType_ThrowNullPointerException() throws Exception {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("MIME type must be present");
        new FileContainerDocument(new File(TEST_FILES_TEST_TXT), null);
    }

    @Test
    public void testCreateNewFileBasedDocument() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource(TEST_FILES_TEST_TXT);
        FileContainerDocument fileDocument = new FileContainerDocument(new File(url.toURI()), MIME_TYPE_APPLICATION_TXT);
        assertEquals("test.txt", fileDocument.getFileName());
        assertEquals(MIME_TYPE_APPLICATION_TXT, fileDocument.getMimeType());
        assertNotNull(fileDocument.getDataHash(HashAlgorithm.SHA2_256));
    }

    @Test
    public void testOverrideDocumentName() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource(TEST_FILES_TEST_TXT);
        FileContainerDocument fileDocument = new FileContainerDocument(new File(url.toURI()), MIME_TYPE_APPLICATION_TXT, "test2.doc");
        assertEquals("test2.doc", fileDocument.getFileName());
    }
}