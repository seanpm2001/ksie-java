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

package com.guardtime.envelope.extending.ksi;

import com.guardtime.envelope.extending.ExtendingPolicy;
import com.guardtime.envelope.packaging.Envelope;
import com.guardtime.envelope.signature.SignatureException;
import com.guardtime.envelope.util.Util;
import com.guardtime.ksi.KSI;
import com.guardtime.ksi.exceptions.KSIException;
import com.guardtime.ksi.publication.PublicationRecord;
import com.guardtime.ksi.unisignature.KSISignature;

/**
 * Extends all {@link KSISignature}s in {@link Envelope} to specified publication
 * record. The publication time of the publication record must be after signature aggregation time.
 */
public class PublicationKsiEnvelopeSignatureExtendingPolicy implements ExtendingPolicy<KSISignature> {
    private final PublicationRecord publicationRecord;
    protected final KSI ksi;

    public PublicationKsiEnvelopeSignatureExtendingPolicy(KSI ksi, PublicationRecord publicationRecord) {
        Util.notNull(ksi, "KSI");
        Util.notNull(publicationRecord, "Publication record");
        this.ksi = ksi;
        this.publicationRecord = publicationRecord;
    }

    public KSISignature getExtendedSignature(KSISignature ksiSignature) throws SignatureException {
        try {
            return ksi.extend(ksiSignature, publicationRecord);
        } catch (KSIException e) {
            throw new SignatureException(e);
        }
    }

}