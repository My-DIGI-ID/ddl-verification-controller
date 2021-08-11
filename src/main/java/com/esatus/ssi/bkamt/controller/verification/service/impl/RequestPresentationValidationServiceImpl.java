/*
 * Copyright 2021 Bundesrepublik Deutschland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.esatus.ssi.bkamt.controller.verification.service.impl;

import com.esatus.ssi.bkamt.agent.client.model.V10PresentationExchange;
import com.esatus.ssi.bkamt.agent.model.Presentation;
import com.esatus.ssi.bkamt.controller.verification.domain.RequestPresentationValidationResult;
import com.esatus.ssi.bkamt.controller.verification.service.RequestPresentationValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Service class for presentation request validation.
 */
@Service
public class RequestPresentationValidationServiceImpl implements RequestPresentationValidationService {

    @Value("${ssibk.verification.controller.expiryCheck.attribute}")
    private String expiryCheckAttribute;

    @Value("${ssibk.verification.controller.expiryCheck.format}")
    private String expiryCheckFormat;

    @Value("${ssibk.verification.controller.expiryCheck.validity}")
    private String expiryCheckValidity;

    @Override
    public RequestPresentationValidationResult validatePresentationExchange(V10PresentationExchange presentationExchange) {
        Presentation presentation = new ObjectMapper().convertValue(presentationExchange.getPresentation(), Presentation.class);

        Map<String, Map<String, String>> values = (Map) presentation.getRequestedProof().getRevealedAttrGroups().getDdl().getValues();
        String issuedDate = values.get(expiryCheckAttribute).get("raw");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date now = cal.getTime();

        boolean isValid = issueDateValid(issuedDate, expiryCheckFormat, now, "0");

        if (isValid) {
            return new RequestPresentationValidationResult(true, "Verification succeeded");
        } else {
            return new RequestPresentationValidationResult(false, "Verification failed");
        }
    }

    @Override
    public boolean issueDateValid(String issueDate, String format, Date now, String validityValue) {
        DateFormat simpleDateFormat = new SimpleDateFormat(format);

        try {
            Instant dateOfIssuingInstant = simpleDateFormat.parse(issueDate).toInstant();

            Date dateOfIssuing = Date.from(dateOfIssuingInstant);

            Instant start = now.toInstant().minus(Long.parseLong(validityValue), ChronoUnit.DAYS);
            Instant end = now.toInstant().plus(Long.parseLong("1"), ChronoUnit.DAYS);

            return dateOfIssuing.before(Date.from(end)) && (dateOfIssuing.after(Date.from(start)) || dateOfIssuing.equals(Date.from(start)));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
