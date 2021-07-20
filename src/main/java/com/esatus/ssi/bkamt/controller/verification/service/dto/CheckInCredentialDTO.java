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

package com.esatus.ssi.bkamt.controller.verification.service.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.esatus.ssi.bkamt.controller.verification.domain.CheckInCredential;

public class CheckInCredentialDTO {

    @NotNull
    @Size(max = 100)
    private String id;

    @NotNull
    @Size(max = 100)
    private String hotelId;

    @NotNull
    @Size(max = 100)
    private String deskId;

    private Date scanDate;

    @NotNull
    private MasterIdDTO masterId;

    private CorporateIdDTO corporateId;

    private boolean valid;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public CheckInCredentialDTO() {

    }

    public CheckInCredentialDTO(CheckInCredential checkInCredential) {
        this.id = checkInCredential.getId();
        this.hotelId = checkInCredential.getHotelId();
        this.deskId = checkInCredential.getDeskId();
    }

    public CheckInCredentialDTO(@NotNull @Size(max = 100) String id, @NotNull @Size(max = 100) String hotelId,
    @NotNull @Size(max = 100) String deskId) {
        this.id = id;
        this.hotelId = hotelId;
        this.deskId = deskId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getScanDate() {
        return scanDate;
    }

    public void setScanDate(Date scanDate) {
        this.scanDate = scanDate;
    }

    public MasterIdDTO getMasterId() {
        return masterId;
    }

    public void setMasterId(MasterIdDTO masterId) {
        this.masterId = masterId;
    }

    public CorporateIdDTO getCorporateId() {
        return corporateId;
    }

    public void setCorporateId(CorporateIdDTO corporateId) {
        this.corporateId = corporateId;
    }

    @Override
    public String toString() {
        return "CheckInCredentialDTO [corporateId=" + corporateId + ", deskId=" + deskId + ", hotelId=" + hotelId
                + ", id=" + id + ", masterId=" + masterId + ", scanDate=" + scanDate + ", valid=" + valid + "]";
    }
}
