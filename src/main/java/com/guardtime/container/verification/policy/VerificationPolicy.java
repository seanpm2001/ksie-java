/*
 * Copyright 2013-2017 Guardtime, Inc.
 *
 * This file is part of the Guardtime client SDK.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES, CONDITIONS, OR OTHER LICENSES OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * "Guardtime" and "KSI" are trademarks or registered trademarks of
 * Guardtime, Inc., and no license to trademarks is granted; Guardtime
 * reserves and retains all trademark rights.
 */

package com.guardtime.container.verification.policy;

import com.guardtime.container.packaging.Container;
import com.guardtime.container.packaging.SignatureContent;
import com.guardtime.container.verification.rule.Rule;

import java.util.List;

/**
 * Access interface for providing {@link Rule}s to be used for verifying a {@link com.guardtime.container.packaging.Container}
 * Contains rules to be performed on container to prove validity.
 * As an example should contain rules for:
 * <ol>
 *   <li>verifying MIME-type</li>
 *   <li>verifying signature</li>
 *   <li>verifying data manifest</li>
 *   <li>verifying data files</li>
 *   <li>verifying annotations manifest</li>
 *   <li>verifying annotations (including annotation manifests)</li>
 * </ol>
 * May contain extra rules to add specialized verification requirements to the policy.
 */
public interface VerificationPolicy {

    List<Rule<SignatureContent>> getSignatureContentRules();

    List<Rule<Container>> getContainerRules();
}