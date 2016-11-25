package com.guardtime.container.document;

import com.guardtime.container.util.Util;
import com.guardtime.ksi.hashing.DataHash;
import com.guardtime.ksi.hashing.HashAlgorithm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import static com.guardtime.container.util.Util.createTempFile;
import static com.guardtime.container.util.Util.notNull;

/**
 * Document that is based on a {@link InputStream}.
 */
public class StreamContainerDocument implements UnknownDocument {

    private final File tempFile;
    private FileContainerDocument containerDocument;
    private boolean closed;

    public StreamContainerDocument(InputStream input, String mimeType, String fileName) {
        notNull(input, "Input stream");
        notNull(mimeType, "MIME type");
        notNull(fileName, "File name");
        this.tempFile = copy(input);
        this.containerDocument = new FileContainerDocument(tempFile, mimeType, fileName);
    }

    @Override
    public String getFileName() {
        return containerDocument.getFileName();
    }

    @Override
    public String getMimeType() {
        return containerDocument.getMimeType();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        checkClosed();
        return containerDocument.getInputStream();
    }

    @Override
    public DataHash getDataHash(HashAlgorithm algorithm) throws IOException {
        checkClosed();
        return containerDocument.getDataHash(algorithm);
    }

    @Override
    public List<DataHash> getDataHashList(List<HashAlgorithm> algorithmList) throws IOException {
        checkClosed();
        return containerDocument.getDataHashList(algorithmList);
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public String toString() {
        return "{type=Stream" +
                ", fileName=" + containerDocument.getFileName() +
                ", mimeType=" + containerDocument.getMimeType() + "}";
    }

    protected File copy(InputStream input) {
        try {
            File tempFile = createTempFile();
            Util.copyToTempFile(input, tempFile);
            return tempFile;
        } catch (IOException e) {
            throw new IllegalArgumentException("Can not copy input stream", e);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StreamContainerDocument that = (StreamContainerDocument) o;

        return containerDocument != null ? containerDocument.equals(that.containerDocument) : that.containerDocument == null;

    }

    @Override
    public int hashCode() {
        return containerDocument != null ? containerDocument.hashCode() : 0;
    }

    @Override
    public void close() throws IOException {
        containerDocument.close();
        Files.deleteIfExists(tempFile.toPath());
        this.closed = true;
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }

    private void checkClosed() throws IOException {
        if(closed) {
            throw new IOException("Can't access closed document!");
        }
    }

    @Override
    public UnknownDocument clone() {
        try (InputStream inputStream = getInputStream()) {
            return new StreamContainerDocument(inputStream, getMimeType(), getFileName());
        } catch (IOException e) {
            return null;
        }
    }
}
