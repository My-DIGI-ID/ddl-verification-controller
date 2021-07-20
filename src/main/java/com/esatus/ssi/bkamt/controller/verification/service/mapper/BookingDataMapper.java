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

package com.esatus.ssi.bkamt.controller.verification.service.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.esatus.ssi.bkamt.controller.verification.domain.BookingData;
import com.esatus.ssi.bkamt.controller.verification.service.dto.BookingDataDTO;

@Service
public class BookingDataMapper {

    public BookingData bookingDataDTOToBookingData(BookingDataDTO bookingDataDTO) {
        if (bookingDataDTO == null) {
            return null;
        } else {

            BookingData bookingData = new BookingData();
            bookingData.setBookingId(bookingDataDTO.getBookingId());
            bookingData.setCompanyEmail(bookingDataDTO.getCompanyEmail());
            bookingData.setFirstName(bookingDataDTO.getFirstName());
            bookingData.setLastName(bookingDataDTO.getLastName());
            bookingData.setCompanyAddressStreet(bookingDataDTO.getCompanyAddressStreet());
            bookingData.setCompanyAddressZipCode(bookingDataDTO.getCompanyAddressZipCode());
            bookingData.setCompanyAddressCity(bookingDataDTO.getCompanyAddressCity());
            bookingData.setArrivalDate(bookingDataDTO.getArrivalDate());
            bookingData.setDepartureDate(bookingDataDTO.getDepartureDate());

            return bookingData;
        }
    }

    public BookingDataDTO bookingDataToBookingDataDTO(BookingData bookingData) {
        if (bookingData == null) {
            return null;
        } else {

            BookingDataDTO bookingDataDTO = new BookingDataDTO();
            bookingDataDTO.setBookingId(bookingData.getBookingId());
            bookingDataDTO.setCompanyEmail(bookingData.getCompanyEmail());
            bookingDataDTO.setFirstName(bookingData.getFirstName());
            bookingDataDTO.setLastName(bookingData.getLastName());
            bookingDataDTO.setCompanyAddressStreet(bookingData.getCompanyAddressStreet());
            bookingDataDTO.setCompanyAddressZipCode(bookingData.getCompanyAddressZipCode());
            bookingDataDTO.setCompanyAddressCity(bookingData.getCompanyAddressCity());
            bookingDataDTO.setArrivalDate(bookingData.getArrivalDate());
            bookingDataDTO.setDepartureDate(bookingData.getDepartureDate());

            return bookingDataDTO;
        }
    }

    public List<BookingDataDTO> bookingDatasToBookingDataDTOs(List<BookingData> bookingDatas) {
        return bookingDatas.stream().filter(Objects::nonNull).map(this::bookingDataToBookingDataDTO).collect(Collectors.toList());
    }
}
