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

public class BookingDataDTO {

    private String bookingId;
    private String companyEmail;
    private String firstName;
    private String lastName;
    private String companyAddressStreet;
    private String companyAddressZipCode;
    private String companyAddressCity;
    private Date arrivalDate;
    private Date departureDate;
    private String guestId;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyAddressStreet() {
        return companyAddressStreet;
    }

    public void setCompanyAddressStreet(String companyAddressStreet) {
        this.companyAddressStreet = companyAddressStreet;
    }

    public String getCompanyAddressZipCode() {
        return companyAddressZipCode;
    }

    public void setCompanyAddressZipCode(String companyAddressZipCode) {
        this.companyAddressZipCode = companyAddressZipCode;
    }

    public String getCompanyAddressCity() {
        return companyAddressCity;
    }

    public void setCompanyAddressCity(String companyAddressCity) {
        this.companyAddressCity = companyAddressCity;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    @Override
    public String toString() {
        return "BookingDataDTO [arrivalDate=" + arrivalDate + ", bookingId=" + bookingId + ", companyAddressCity="
                + companyAddressCity + ", companyAddressStreet=" + companyAddressStreet + ", companyAddressZipCode="
                + companyAddressZipCode + ", companyEmail=" + companyEmail + ", departureDate=" + departureDate
                + ", firstName=" + firstName + ", guestId=" + guestId + ", lastName=" + lastName + "]";
    }
}
