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
import org.springframework.stereotype.Service;
import com.esatus.ssi.bkamt.controller.verification.domain.Hotel;
import com.esatus.ssi.bkamt.controller.verification.service.dto.HotelDTO;

/**
 * Mapper for the entity {@link Hotel} and its DTO called {@link HotelDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as
 * MapStruct support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class HotelMapper {

    @Autowired
    DeskMapper deskMapper;

    @Autowired
    AddressMapper addressMapper;

    public Hotel hotelDTOToHotel(HotelDTO hotelDTO) {
        if (hotelDTO == null) {
            return null;
        } else {

            Hotel hotel = new Hotel();
            hotel.setId(hotelDTO.getId());
            hotel.setName(hotelDTO.getName());
            hotel.setAddress(this.addressMapper.addressDTOToAddress(hotelDTO.getAddress()));
            hotel.setDesks(this.deskMapper.deskDTOsToDesks(hotelDTO.getDesks()));

            return hotel;
        }
    }

    public HotelDTO hotelToHotelDTO(Hotel hotel) {
        if (hotel == null) {
            return null;
        } else {

            HotelDTO hotelDTO = new HotelDTO();
            hotelDTO.setId(hotel.getId());
            hotelDTO.setName(hotel.getName());
            hotelDTO.setAddress(this.addressMapper.addressToAddressDTO(hotel.getAddress()));
            hotelDTO.setDesks(this.deskMapper.desksToDeskDTOs(hotel.getDesks()));

            return hotelDTO;
        }
    }

    public List<HotelDTO> hotelsToHotelDTOs(List<Hotel> hotels) {
        return hotels.stream().filter(Objects::nonNull).map(this::hotelToHotelDTO).collect(Collectors.toList());
    }
}
