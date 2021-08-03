package com.esatus.ssi.bkamt.controller.verification.service.impl;

import com.esatus.ssi.bkamt.agent.client.model.V10PresentationExchange;
import com.esatus.ssi.bkamt.controller.verification.client.model.Presentation;
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

        Instant expirationDate = buildExpirationDate();

        boolean isValid = issueDateValid(issuedDate, expiryCheckFormat, expirationDate);
        if (isValid) {
            return new RequestPresentationValidationResult(true, "Verification succeeded");
        } else {
            return new RequestPresentationValidationResult(false, "Verification failed");
        }
    }

    private Instant buildExpirationDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date d = cal.getTime();

        return d.toInstant().plus(Long.parseLong(expiryCheckValidity), ChronoUnit.DAYS);
    }

    @Override
    public boolean issueDateValid(String validUntil, String format, Instant expirationDate) {
        DateFormat simpleDateFormat = new SimpleDateFormat(format);

        try {
            Instant validUntilDate = simpleDateFormat.parse(validUntil).toInstant();
            return validUntilDate.isBefore(expirationDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
