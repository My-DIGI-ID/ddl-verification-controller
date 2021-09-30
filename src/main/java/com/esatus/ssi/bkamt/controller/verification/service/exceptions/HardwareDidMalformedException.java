package com.esatus.ssi.bkamt.controller.verification.service.exceptions;

public class HardwareDidMalformedException extends Exception {
    public HardwareDidMalformedException() {
        super("HardwareDid is malformed");
    }
}
