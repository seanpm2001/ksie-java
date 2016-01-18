package com.guardtime.container.packaging.zip;

import com.guardtime.container.BlockChainContainerException;
import com.guardtime.container.annotation.ContainerAnnotation;
import com.guardtime.container.datafile.ContainerDataFile;
import com.guardtime.container.manifest.*;
import com.guardtime.container.packaging.BlockchainContainerPackagingFactory;
import com.guardtime.container.signature.ContainerSignature;
import com.guardtime.container.signature.SignatureFactory;
import com.guardtime.container.util.Util;
import com.guardtime.ksi.hashing.DataHash;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class ZipContainerPackagingFactory implements BlockchainContainerPackagingFactory<ZipBlockchainContainer> {

    private final SignatureFactory signatureFactory;
    private final ContainerManifestFactory manifestFactory;

    public ZipContainerPackagingFactory(SignatureFactory signatureFactory, ContainerManifestFactory manifestFactory) {
        Util.notNull(signatureFactory, "Signature factory");
        Util.notNull(manifestFactory, "Manifest factory");
        this.signatureFactory = signatureFactory;
        this.manifestFactory = manifestFactory;
    }

    @Override
    public ZipBlockchainContainer read(InputStream input) {

        return null;
    }

    @Override
    public ZipBlockchainContainer create(List<ContainerDataFile> files, List<ContainerAnnotation> annotations) throws BlockChainContainerException {
        Util.notEmpty(files, "Data files");
        DataFilesManifest dataFilesManifest = manifestFactory.createDataFilesManifest(files);
        List<AnnotationInfoManifest> annotationInfoManifests = createAnnotationInfoManifests(annotations, dataFilesManifest);
        AnnotationsManifest annotationsManifest = manifestFactory.createAnnotationsManifest(annotationInfoManifests);
        SignatureManifest signatureManifest = manifestFactory.createSignatureManifest(dataFilesManifest, annotationsManifest);

        ZipBlockchainContainer container = new Builder(files, annotations).
                withDataFilesManifest(dataFilesManifest).
                withAnnotationInfoManifests(annotationInfoManifests).
                withAnnotationsManifest(annotationsManifest).
                withSignatureManifest(signatureManifest).
                build();

        DataHash hash = container.getSignatureInputHash();
        ContainerSignature signature = signatureFactory.create(hash);
        container.addSignature(signature);
        return container;
    }

    private List<AnnotationInfoManifest> createAnnotationInfoManifests(List<ContainerAnnotation> annotations, DataFilesManifest dataFilesManifest) {
        List<AnnotationInfoManifest> annotationInfoManifests = new LinkedList<>();
        for (ContainerAnnotation annotation : annotations) {
            AnnotationInfoManifest singleAnnotationManifest = manifestFactory.createAnnotationManifest(dataFilesManifest, annotation);
            annotationInfoManifests.add(singleAnnotationManifest);
        }
        return annotationInfoManifests;
    }

}
