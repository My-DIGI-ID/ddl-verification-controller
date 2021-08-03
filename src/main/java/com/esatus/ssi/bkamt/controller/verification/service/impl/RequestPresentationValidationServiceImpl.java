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
        String dateOfIssue = presentation.getRequestedProof().getRevealedAttrGroups().getDdl().getValues().getAusstellungsdatum().getRaw();

        Instant expirationDate = createExpirationDate();

        boolean isValid = dateOfIssueDateValid(dateOfIssue, expiryCheckFormat, expirationDate);
        return new RequestPresentationValidationResult(isValid, "Validation of data succeeded");
    }

    private Instant createExpirationDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        Date d = cal.getTime();

        return d.toInstant().plus(Long.parseLong(expiryCheckValidity), ChronoUnit.DAYS);
    }

    @Override
    public boolean dateOfIssueDateValid(String validUntil, String format, Instant expirationDate) {
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
