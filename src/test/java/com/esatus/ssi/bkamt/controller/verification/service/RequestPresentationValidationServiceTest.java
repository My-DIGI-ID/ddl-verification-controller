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
import java.util.Calendar;
import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class RequestPresentationValidationServiceTest {

    public final static String DATE_FORMAT = "yyyyMMdd";
    public final static String DATE_FORMAT_DE = "dd.MM.yyyy";

    @InjectMocks
    @Autowired
    private RequestPresentationValidationServiceImpl requestPresentationValidationService;

    @Test
    public void validateDateOfIssueDate_IsValid_ShouldReturnFalseBecauseNoDateWasPassed() {
        var isValid = requestPresentationValidationService.issueDateValid("", DATE_FORMAT, new Date(), "0");
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    public void validateDateOfIssueDate_IsValid_ShouldReturnFalseBecauseAnInvalidDateWasPassed() {
        var isValid = requestPresentationValidationService.issueDateValid("12.20.2587", DATE_FORMAT, new Date(), "0");
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    public void validateDateOfIssueDate_IsValid_ShouldReturnTrueBecauseTheValidityWasSetTo1Day() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 4);
        cal.set(Calendar.MONTH, 7); // January = 0
        cal.set(Calendar.YEAR, 2021);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date now = cal.getTime();

        var isValid = requestPresentationValidationService.issueDateValid("20210803", DATE_FORMAT, now, "1");
        Assertions.assertThat(isValid).isTrue();
    }

    @Test
    public void validateDateOfIssueDate_IsValid_ShouldReturnTrue() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 4);
        cal.set(Calendar.MONTH, 7); // January = 0
        cal.set(Calendar.YEAR, 2021);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date now = cal.getTime();

        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        String strDate = format.format(now);

        var isValid = requestPresentationValidationService.issueDateValid(strDate, DATE_FORMAT, now, "0");
        Assertions.assertThat(isValid).isTrue();
    }

    @Test
    public void validateDateOfIssueDate_IsValid_ShouldReturnFalseBecauseTheExpirationDateIsOneDayBefore() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 5);
        cal.set(Calendar.MONTH, 7); // January = 0
        cal.set(Calendar.YEAR, 2021);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date now = cal.getTime();

        var isValid = requestPresentationValidationService.issueDateValid("20210803", DATE_FORMAT, now, "1");
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    public void validateDateOfIssueDate_IsValid_ShouldReturnFalseBecauseTheExpirationDateIsOneWeekBefore() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 5);
        cal.set(Calendar.MONTH, 7); // January = 0
        cal.set(Calendar.YEAR, 2021);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date now = cal.getTime();

        var isValid = requestPresentationValidationService.issueDateValid("20210729", DATE_FORMAT, now, "0");
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    public void validateDateOfIssueDateWithGermanDateFormat_IsValid_ShouldReturnTrue() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 4);
        cal.set(Calendar.MONTH, 7); // January = 0
        cal.set(Calendar.YEAR, 2021);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date now = cal.getTime();

        var isValid = requestPresentationValidationService.issueDateValid("04.08.2021", DATE_FORMAT_DE, now, "0");
        Assertions.assertThat(isValid).isTrue();
    }

    @Test
    public void validateDateOfIssueDateWithGermanDateFormat_IsValid_ShouldReturnFalseBecauseTheExpirationDateIsOneWeekBefore() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 4);
        cal.set(Calendar.MONTH, 7); // January = 0
        cal.set(Calendar.YEAR, 2021);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date now = cal.getTime();

        var isValid = requestPresentationValidationService.issueDateValid("29.07.2021", DATE_FORMAT_DE, now, "0");
        Assertions.assertThat(isValid).isFalse();
    }
}
