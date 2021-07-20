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

package com.esatus.ssi.bkamt.controller.verification.domain;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;

public class CheckInCredential {

    @Id
    private String id;

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String hotelId;

    @Size(max = 50)
    @NotNull
    @NotBlank
    private String deskId;

    private String presentationExchangeId;

    private Date scanDate;

    private Date sendDate;

    private MasterId masterId;

    private CorporateId corporateId;

    private boolean valid;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public CheckInCredential() {}

    public CheckInCredential(@Size(max = 50) @NotNull @NotBlank String hotelId,
            @Size(max = 50) @NotNull @NotBlank String deskId, String presentationExchangeId) {
        this.hotelId = hotelId;
        this.deskId = deskId;
        this.presentationExchangeId = presentationExchangeId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getDeskId() {
        return deskId;
    }

    public void setDeskId(String deskId) {
        this.deskId = deskId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPresentationExchangeId() {
        return presentationExchangeId;
    }

    public void setPresentationExchangeId(String presentationExchangeId) {
        this.presentationExchangeId = presentationExchangeId;
    }

    public Date getScanDate() {
        return scanDate;
    }

    public void setScanDate(Date scanDate) {
        this.scanDate = scanDate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public MasterId getMasterId() {
        return masterId;
    }

    public void setMasterId(MasterId masterId) {
        this.masterId = masterId;
    }

    public CorporateId getCorporateId() {
        return corporateId;
    }

    public void setCorporateId(CorporateId corporateId) {
        this.corporateId = corporateId;
    }

    @Override
    public String toString() {
        return "CheckInCredential [corporateId=" + corporateId + ", deskId=" + deskId + ", presentationExchangeId="
                + presentationExchangeId + ", hotelId=" + hotelId + ", id=" + id + ", masterId=" + masterId + ", scanDate="
                + scanDate + ", sendDate=" + sendDate + "]";
    }
}
