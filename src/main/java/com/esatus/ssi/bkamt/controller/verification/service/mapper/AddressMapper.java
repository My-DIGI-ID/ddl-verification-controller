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
import com.esatus.ssi.bkamt.controller.verification.domain.Address;
import com.esatus.ssi.bkamt.controller.verification.service.dto.AddressDTO;

/**
 * Mapper for the entity {@link Address} and its DTO called {@link AddressDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as
 * MapStruct support is still in beta, and requires a manual step with an IDE.
 */

@Service
public class AddressMapper {

    public Address addressDTOToAddress(AddressDTO addressDTO) {
        if (addressDTO == null) {
            return null;
        } else {
        Address address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setHouseNumber(addressDTO.getHouseNumber());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setCity(addressDTO.getCity());

        return address;

        }
    }

    public AddressDTO addressToAddressDTO(Address address) {
        if (address == null) {
            return null;
        } else {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet(address.getStreet());
        addressDTO.setHouseNumber(address.getHouseNumber());
        addressDTO.setPostalCode(address.getPostalCode());
        addressDTO.setCity(address.getCity());

        return addressDTO;
        }
    }

    public List<AddressDTO> addressToAddressDTOs(List<Address> addresses) {
        return addresses.stream().filter(Objects::nonNull).map(this::addressToAddressDTO).collect(Collectors.toList());
    }

    public List<Address> addressDTOsToAddresses(List<AddressDTO> addressDTOs) {
        return addressDTOs.stream().filter(Objects::nonNull).map(this::addressDTOToAddress).collect(Collectors.toList());
    }
}
