package com.guardtime.container.verification.rule.generic;

import com.guardtime.container.manifest.AnnotationsManifest;
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
 * This rule verifies that the annotations manifest is actually present in the {@link
 * com.guardtime.container.packaging.Container}
 */
public class AnnotationsManifestExistenceRule extends AbstractRule<SignatureContent> {

    public AnnotationsManifestExistenceRule(RuleState state) {
        super(state);
    }

    @Override
    protected void verifyRule(ResultHolder holder, SignatureContent verifiable) throws RuleTerminatingException {
        VerificationResult verificationResult = getFailureVerificationResult();
        Manifest manifest = verifiable.getManifest().getRight();
        FileReference annotationsManifestReference = manifest.getAnnotationsManifestReference();
        Pair<String, AnnotationsManifest> annotationsManifest = verifiable.getAnnotationsManifest();
        if (annotationsManifest != null) {
            verificationResult = VerificationResult.OK;
        }
        String annotationsManifestUri = annotationsManifestReference.getUri();
        holder.addResult(new GenericVerificationResult(verificationResult, this, annotationsManifestUri));


        if (!verificationResult.equals(VerificationResult.OK)) {
            throw new RuleTerminatingException("AnnotationsManifest integrity could not be verified for '" + annotationsManifestUri + "'");
        }
    }

    @Override
    public String getName() {
        return "KSIE_VERIFY_ANNOTATION_MANIFEST_EXISTS";
    }

    @Override
    public String getErrorMessage() {
        return "Annotations manifest is not present in the container.";
    }
}
