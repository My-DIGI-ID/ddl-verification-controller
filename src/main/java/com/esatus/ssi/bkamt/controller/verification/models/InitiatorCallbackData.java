package com.esatus.ssi.bkamt.controller.verification.models;

import java.util.List;

public class InitiatorCallbackData {
    private Integer code;
    private boolean verified;
    private String message;
    private List<InitiatorCallbackDataValue> data;

    public InitiatorCallbackData(Integer code, boolean verified, String message, List<InitiatorCallbackDataValue> data) {
        this.code =  code;
        this.verified = verified;
        this.message = message;
        this.data = data;
    }
}

