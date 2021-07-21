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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.esatus.ssi.bkamt.controller.verification.domain.Authority;
import com.esatus.ssi.bkamt.controller.verification.domain.Hotel;
import com.esatus.ssi.bkamt.controller.verification.domain.User;
import com.esatus.ssi.bkamt.controller.verification.repository.UserRepository;
import com.esatus.ssi.bkamt.controller.verification.security.AuthoritiesConstants;
import com.esatus.ssi.bkamt.controller.verification.service.UserService;
import com.esatus.ssi.bkamt.controller.verification.service.dto.UserCreationDTO;
import com.esatus.ssi.bkamt.controller.verification.service.dto.UserDTO;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.HotelNotFoundException;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.UserAlreadyExistsException;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.UserNotFoundException;
import com.esatus.ssi.bkamt.controller.verification.service.exceptions.UserWithLoginAlreadyExists;
import com.esatus.ssi.bkamt.controller.verification.service.mapper.UserMapper;

/**
 * Service class for managing users.
 */
@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    public UserDTO createUser(UserCreationDTO userDTO) throws UserAlreadyExistsException, HotelNotFoundException {
        Optional<User> existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException();
        }

        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstname());
        user.setLastName(userDTO.getLastname());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encryptedPassword);

        Set<Authority> userAuthorities = this.giveAuthority();
        user.setAuthorities(userAuthorities);

        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return this.userMapper.userToUserDTO(user);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) throws
            UserNotFoundException,
            UserWithLoginAlreadyExists,
            HotelNotFoundException {
        Optional<User> findById = this.userRepository.findById(userDTO.getId());

        if (!findById.isPresent()) {
            throw new UserNotFoundException();
        }

        Optional<User> loginCheck = this.userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());

        if ((loginCheck.isPresent()) && (!loginCheck.get().getId().equals(userDTO.getId()))) {
            throw new UserWithLoginAlreadyExists();
        }

        String userPassword = findById.get().getPassword();

        User editedUser = new User();
        editedUser.setId(userDTO.getId());
        editedUser.setLogin(userDTO.getLogin());
        editedUser.setFirstName(userDTO.getFirstname());
        editedUser.setLastName(userDTO.getLastname());
        editedUser.setEmail(userDTO.getEmail());
        editedUser.setPassword(userPassword);

        Set<Authority> userAuthorities = this.giveAuthority();
        editedUser.setAuthorities(userAuthorities);

        User updatedUser = this.userRepository.save(editedUser);

        return this.userMapper.userToUserDTO(updatedUser);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        Predicate<User> hasUserRole = user -> user.getAuthorities().stream().map(Authority:: getName).anyMatch(AuthoritiesConstants.USER::equals);
        return userRepository.findAll().stream().filter(hasUserRole).map(userMapper::userToUserDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Optional<UserDTO> getUser(String id) {
        return this.userRepository.findById(id).map(userMapper::userToUserDTO);
    }

    @Override
    public void deleteUser(String id) {
        userRepository.findById(id).ifPresent(user -> {

            userRepository.delete(user);
            log.debug("Deleted User: {}", user);
        });
    }

    private Set<Authority> giveAuthority() {
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);
        Set<Authority> userAuthorities = new HashSet<Authority>();
        userAuthorities.add(userAuthority);

        return userAuthorities;
    }
}
