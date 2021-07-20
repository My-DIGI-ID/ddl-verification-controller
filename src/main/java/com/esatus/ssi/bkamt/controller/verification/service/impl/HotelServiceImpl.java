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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.esatus.ssi.bkamt.controller.verification.domain.Hotel;
import com.esatus.ssi.bkamt.controller.verification.domain.User;
import com.esatus.ssi.bkamt.controller.verification.repository.HotelRepository;
import com.esatus.ssi.bkamt.controller.verification.repository.UserRepository;
import com.esatus.ssi.bkamt.controller.verification.security.SecurityUtils;
import com.esatus.ssi.bkamt.controller.verification.service.HotelService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.HotelDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.HotelAlreadyExistsException;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.HotelNotFoundException;
import com.esatus.ssi.bkamt.controller.verification.service.mapper.DeskMapper;
import com.esatus.ssi.bkamt.controller.verification.service.mapper.HotelMapper;

@Service
public class HotelServiceImpl implements HotelService {

    private final Logger log = LoggerFactory.getLogger(HotelServiceImpl.class);

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HotelMapper hotelMapper;

    @Autowired
    DeskMapper deskMapper;

    @Override
    public HotelDTO createHotel(HotelDTO hotelDTO) throws HotelAlreadyExistsException {

        log.debug("create hotel");
        if (this.hotelRepository.existsById(hotelDTO.getId())) {
            throw new HotelAlreadyExistsException();
        }

        Hotel hotel = hotelMapper.hotelDTOToHotel(hotelDTO);
        Hotel createdHotel = this.hotelRepository.insert(hotel);

        HotelDTO createdHotelDTO = hotelMapper.hotelToHotelDTO(createdHotel);
        return createdHotelDTO;
    }

    @Override
    public HotelDTO updateHotel(HotelDTO hotelDTO) throws HotelNotFoundException {

        log.debug("update hotel");
        if (!this.hotelRepository.existsById(hotelDTO.getId())) {
            throw new HotelNotFoundException();
        }

        Hotel hotel = hotelMapper.hotelDTOToHotel(hotelDTO);
        Hotel updatedHotel = this.hotelRepository.save(hotel);

        HotelDTO updatedHotelDTO = hotelMapper.hotelToHotelDTO(updatedHotel);
        return updatedHotelDTO;
    }

    @Override
    public List<HotelDTO> getAllHotels() {
        log.debug("get all hotels");
        return this.hotelRepository.findAll().stream().map(hotelMapper::hotelToHotelDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Optional<HotelDTO> getHotel(String id) {
        log.debug("get hotel by id");
        return this.hotelRepository.findById(id).map(hotelMapper::hotelToHotelDTO);
    }

    @Override
    public Optional<HotelDTO> getMyHotel() {

        Optional<HotelDTO> hotel = null;

        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (login.isPresent()) {
            Optional<User> user = this.userRepository.findOneByLogin(login.get());
            if (user.isPresent()) {
                String hotelId = user.get().getHotelId();
                hotel = this.hotelRepository.findById(hotelId).map(hotelMapper::hotelToHotelDTO);
            }
        }

        return hotel;
    }

    @Override
    public void deleteHotel(String id) {
        log.debug("delete hotel");
        this.hotelRepository.deleteById(id);
    }
}
