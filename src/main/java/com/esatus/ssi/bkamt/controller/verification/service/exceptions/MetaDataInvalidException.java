package com.esatus.ssi.bkamt.controller.verification.service.exceptions;

public class MetaDataInvalidException extends Exception {
    public MetaDataInvalidException() {
        super("Meta data validation failed");
    }
}
