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

package com.esatus.ssi.bkamt.controller.verification.service;

import java.util.List;
import java.util.Optional;
import com.esatus.ssi.bkamt.controller.verification.service.dto.HotelDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.HotelAlreadyExistsException;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.HotelNotFoundException;


public interface HotelService {

    HotelDTO createHotel(HotelDTO hotelDTO) throws HotelAlreadyExistsException;

    HotelDTO updateHotel(HotelDTO hotelDTO) throws HotelNotFoundException;

    List<HotelDTO> getAllHotels();

    Optional<HotelDTO> getHotel(String id);

    Optional<HotelDTO> getMyHotel();

    void deleteHotel(String id);
}
