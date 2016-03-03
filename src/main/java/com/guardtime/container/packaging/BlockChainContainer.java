package com.guardtime.container.packaging;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface BlockChainContainer {

    List<SignatureContent> getSignatureContents();

    void writeTo(OutputStream output) throws IOException;

}
