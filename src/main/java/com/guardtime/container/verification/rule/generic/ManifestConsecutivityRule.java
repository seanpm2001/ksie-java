package com.guardtime.container.verification.rule.generic;

import com.guardtime.container.manifest.SignatureManifest;
import com.guardtime.container.packaging.BlockChainContainer;
import com.guardtime.container.packaging.SignatureContent;
import com.guardtime.container.util.Pair;
import com.guardtime.container.util.Util;
import com.guardtime.container.verification.context.VerificationContext;
import com.guardtime.container.verification.result.GenericVerificationResult;
import com.guardtime.container.verification.result.RuleResult;
import com.guardtime.container.verification.result.RuleVerificationResult;
import com.guardtime.container.verification.rule.RuleState;

import java.util.LinkedList;
import java.util.List;

public class ManifestConsecutivityRule extends GenericRule {

    private static final String KSIE_VERIFY_MANIFEST_INDEX = "KSIE_VERIFY_MANIFEST_INDEX";

    public ManifestConsecutivityRule() {
        super(KSIE_VERIFY_MANIFEST_INDEX);
    }

    public ManifestConsecutivityRule(RuleState state) {
        super(state, KSIE_VERIFY_MANIFEST_INDEX);
    }

    @Override
    public List<Pair<? extends Object, ? extends RuleVerificationResult>> verify(VerificationContext context) {
        BlockChainContainer container = context.getContainer();
        List<Pair<? extends Object, ? extends RuleVerificationResult>> results = new LinkedList<>();
        int expectedIndex = 1;
        for (SignatureContent content : container.getSignatureContents()) {
            RuleResult result = getFailureResult();
            Pair<String, SignatureManifest> manifest = content.getSignatureManifest();
            int index = Util.extractIntegerFrom(manifest.getLeft());
            if (index == expectedIndex) {
                result = RuleResult.OK;
            }
            expectedIndex = index + 1;
            results.add(Pair.of(manifest.getRight(), new GenericVerificationResult(result, this)));
        }
        return results;
    }
}
