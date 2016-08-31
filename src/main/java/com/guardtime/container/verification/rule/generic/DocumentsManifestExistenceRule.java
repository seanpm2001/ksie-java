package com.guardtime.container.verification.rule.generic;

import com.guardtime.container.manifest.DocumentsManifest;
import com.guardtime.container.manifest.FileReference;
import com.guardtime.container.manifest.Manifest;
import com.guardtime.container.packaging.SignatureContent;
import com.guardtime.container.util.Pair;
import com.guardtime.container.verification.result.GenericVerificationResult;
import com.guardtime.container.verification.result.ResultHolder;
import com.guardtime.container.verification.result.VerificationResult;
import com.guardtime.container.verification.rule.AbstractRule;
import com.guardtime.container.verification.rule.RuleState;
import com.guardtime.container.verification.rule.RuleTerminatingException;

/**
 * This rule verifies that the documents manifest is actually present in the {@link
 * com.guardtime.container.packaging.Container}
 */
public class DocumentsManifestExistenceRule extends AbstractRule<SignatureContent> {

    public DocumentsManifestExistenceRule(RuleState state) {
        super(state);
    }

    @Override
    protected void verifyRule(ResultHolder holder, SignatureContent verifiable) throws RuleTerminatingException {

        VerificationResult verificationResult = getFailureVerificationResult();
        Manifest manifest = verifiable.getManifest().getRight();
        FileReference documentsManifestReference = manifest.getDocumentsManifestReference();
        Pair<String, DocumentsManifest> documentsManifest = verifiable.getDocumentsManifest();
        if (documentsManifest != null) {
            verificationResult = VerificationResult.OK;
        }
        String manifestUri = documentsManifestReference.getUri();
        holder.addResult(new GenericVerificationResult(verificationResult, this, manifestUri));

        if (!verificationResult.equals(VerificationResult.OK)) {
            throw new RuleTerminatingException("DocumentsManifest existence could not be verified for '" + manifestUri + "'");
        }
    }

    @Override
    public String getName() {
        return "KSIE_VERIFY_DATA_MANIFEST_EXISTS";
    }

    @Override
    public String getErrorMessage() {
        return "Datamanifest is not present in the container.";
    }
}
