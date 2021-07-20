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

package com.esatus.ssi.bkamt.controller.verification.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.esatus.ssi.bkamt.controller.verification.domain.CheckInCredential;
import com.esatus.ssi.bkamt.controller.verification.repository.CheckInCredentialRepository;
import com.esatus.ssi.bkamt.controller.verification.service.CheckInCredentialService;
import com.esatus.ssi.bkamt.controller.verification.service.HotelService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.CheckInCredentialDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.CorporateIdDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.MasterIdDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.CheckinCredentialNotFoundException;
import com.esatus.ssi.bkamt.controller.verification.service.mapper.CheckInCredentialMapper;
import com.esatus.ssi.bkamt.controller.verification.service.mapper.CorporateIdMapper;
import com.esatus.ssi.bkamt.controller.verification.service.mapper.MasterIdMapper;

@Service
public class CheckInCredentialServiceImpl implements CheckInCredentialService {

    private final Logger log = LoggerFactory.getLogger(CheckInCredentialServiceImpl.class);

    @Autowired
    HotelService hotelService;

    @Autowired
    CheckInCredentialMapper checkInCredentialMapper;

    @Autowired
    MasterIdMapper masterIdMapper;

    @Autowired
    CorporateIdMapper corporateIdMapper;

    @Autowired
    CheckInCredentialRepository checkInCredentialRepository;

    @Override
    public List<CheckInCredentialDTO> getDeskCredentials(String deskId) {
        log.debug("Getting the DeskId");
        String hotelDesks = hotelService.getMyHotel().get().getId();
        List<CheckInCredentialDTO> deskCredentials = this.checkInCredentialRepository
                .findByHotelIdAndDeskIdAndSendDateIsNotNullOrderByScanDateAsc(hotelDesks, deskId).stream()
                .map(checkInCredentialMapper::checkInCredentialToCheckInCredentialDTO)
                .collect(Collectors.toCollection(ArrayList::new));

        return deskCredentials;
    }

    @Override
    public void createCheckInCredential(String hotelId, String deskId, String presentationExchangeId) {

        CheckInCredential checkInCredential = new CheckInCredential(hotelId, deskId, presentationExchangeId);
        checkInCredential.setScanDate(new Date());

        this.checkInCredentialRepository.insert(checkInCredential);
    }

    @Override
    public CheckInCredential updateCheckinCredential(String presentationExchangeId, MasterIdDTO masterIdDTO, CorporateIdDTO corporateIdDTO)
            throws CheckinCredentialNotFoundException {

        Optional<CheckInCredential> checkInCredentialOptional = this.checkInCredentialRepository.findOneByPresentationExchangeId(presentationExchangeId);
        if (checkInCredentialOptional.isPresent()) {
            CheckInCredential checkInCredential = checkInCredentialOptional.get();
            checkInCredential.setCorporateId(this.corporateIdMapper.corporateIdDTOToCorporateId(corporateIdDTO));
            checkInCredential.setMasterId(this.masterIdMapper.masterIdDTOToMasterId(masterIdDTO));
            checkInCredential.setSendDate(new Date());

            return this.checkInCredentialRepository.save(checkInCredential);
        } else {
            throw new CheckinCredentialNotFoundException();
        }
    }

    @Override
    public CheckInCredential updateValidity(String presentationExchangeId, boolean proofVerified)
            throws CheckinCredentialNotFoundException {
        Optional<CheckInCredential> checkInCredentialOptional = this.checkInCredentialRepository.findOneByPresentationExchangeId(presentationExchangeId);
        if (checkInCredentialOptional.isPresent()) {

            // get the existing checkInCredential
            CheckInCredential checkInCredential = checkInCredentialOptional.get();

            // checks whether the credential is expired
            LocalDate expirationDate = checkInCredential.getMasterId().getDateOfExpiry();
            Boolean notExpired = expirationDate.isAfter(LocalDate.now());

            // sets the checkinCredential valid if the proof is verified and the credential is not expired
            checkInCredential.setValid(proofVerified && notExpired);

            return this.checkInCredentialRepository.save(checkInCredential);
        } else {
            throw new CheckinCredentialNotFoundException();
        }
    }

    public Optional<CheckInCredentialDTO> getCheckInCredentialById(String id) {
        return this.checkInCredentialRepository.findById(id).map(checkInCredentialMapper::checkInCredentialToCheckInCredentialDTO);
    }
}

