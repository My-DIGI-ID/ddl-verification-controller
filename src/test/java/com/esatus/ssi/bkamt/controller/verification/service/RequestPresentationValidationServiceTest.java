package com.esatus.ssi.bkamt.controller.verification.service;

import com.esatus.ssi.bkamt.controller.verification.service.impl.RequestPresentationValidationServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class RequestPresentationValidationServiceTest {

    private final String dateFormat = "yyyyMMdd";

    @InjectMocks
    @Autowired
    private RequestPresentationValidationServiceImpl requestPresentationValidationService;

    @Test
    public void ValidateDateOfIssueDate_IsValid_ShouldReturnFalseBecauseNoDateWasPassed() {
        Instant expirationDate = new Date().toInstant().plus(Long.parseLong("1"), ChronoUnit.DAYS);
        var isValid = requestPresentationValidationService.dateOfIssueDateValid("", dateFormat, expirationDate);
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    public void ValidateDateOfIssueDate_IsValid_ShouldReturnFalseBecauseAnInvalidDateWasPassed() {
        Instant expirationDate = new Date().toInstant().plus(Long.parseLong("1"), ChronoUnit.DAYS);
        var isValid = requestPresentationValidationService.dateOfIssueDateValid("12.20.2587", dateFormat, expirationDate);
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    public void ValidateDateOfIssueDate_IsValid_ShouldReturnTrueBecauseTheValidityWasSetTo1Day() {
        Instant expirationDate = new Date().toInstant().plus(Long.parseLong("1"), ChronoUnit.DAYS);
        var isValid = requestPresentationValidationService.dateOfIssueDateValid("20210803", dateFormat, expirationDate);
        Assertions.assertThat(isValid).isTrue();
    }

    @Test
    public void ValidateDateOfIssueDate_IsValid_ShouldReturnTrue() {
        Date date = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat(dateFormat);
        String strDate = format.format(date);

        Instant expirationDate = date.toInstant().plus(Long.parseLong("1"), ChronoUnit.DAYS);
        var isValid = requestPresentationValidationService.dateOfIssueDateValid(strDate, dateFormat, expirationDate);
        Assertions.assertThat(isValid).isTrue();
    }

    @Test
    public void ValidateDateOfIssueDate_IsValid_ShouldReturnFalseBecauseTheExpirationDateIsOneDayBefore() {
        Date date = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat(dateFormat);
        String strDate = format.format(date);

        Instant expirationDate = date.toInstant().minus(Long.parseLong("1"), ChronoUnit.DAYS);
        var isValid = requestPresentationValidationService.dateOfIssueDateValid(strDate, dateFormat, expirationDate);
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    public void ValidateDateOfIssueDate_IsValid_ShouldReturnFalseBecauseTheExpirationDateIsOneWeekBefore() {
        Date date = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat(dateFormat);
        String strDate = format.format(date);

        Instant expirationDate = date.toInstant().minus(Long.parseLong("7"), ChronoUnit.DAYS);
        var isValid = requestPresentationValidationService.dateOfIssueDateValid(strDate, dateFormat, expirationDate);
        Assertions.assertThat(isValid).isFalse();
    }
}
