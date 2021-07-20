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
import org.springframework.beans.factory.annotation.Autowired;
import com.esatus.ssi.bkamt.controller.verification.domain.PMSData;
import com.esatus.ssi.bkamt.controller.verification.service.dto.PMSDataDTO;

public class PMSDataMapper {

    @Autowired
    BookingDataMapper bookingDataMapper;

    @Autowired
    CheckInCredentialMapper checkInCredentialMapper;

    @Autowired
    HotelMapper hotelMapper;

    public PMSData pmsDataDTOToPMSData(PMSDataDTO pmsDataDTO) {
        if (pmsDataDTO == null) {
            return null;
        } else {

            PMSData pmsData = new PMSData();
            pmsData.setBookingData(pmsDataDTO.getBookingData());
            pmsData.setCheckInCredential(pmsDataDTO.getCheckInCredential());

            return pmsData;
        }
    }

    public PMSDataDTO pmsDataToPMSDataDTO(PMSData pmsData) {
        if (pmsData == null) {
            return null;
        } else {

            PMSDataDTO pmsDataDTO = new PMSDataDTO();
            pmsDataDTO.setBookingData(pmsData.getBookingData());
            pmsDataDTO.setCheckInCredential(pmsData.getCheckInCredential());
            pmsDataDTO.setHotel(pmsData.getHotel());

            return pmsDataDTO;
        }
    }

    public List<PMSDataDTO> pmsDatasToPMSDataDTOs(List<PMSData> pmsDatas) {
        return pmsDatas.stream().filter(Objects::nonNull).map(this::pmsDataToPMSDataDTO).collect(Collectors.toList());
    }
}
