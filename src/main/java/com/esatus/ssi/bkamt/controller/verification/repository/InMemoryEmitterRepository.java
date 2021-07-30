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

package com.esatus.ssi.bkamt.controller.verification.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryEmitterRepository implements EmitterRepository {

    private final Logger log = LoggerFactory.getLogger(InMemoryEmitterRepository.class);

    private final Map<String, Pair<String, SseEmitter>> emitterMap = new ConcurrentHashMap<>();

    @Override
    public void addEmitter(String uuid, String hotelId, String deskId, SseEmitter emitter) {
        log.debug("Adding emitter for key {} and topic {}", uuid, getTopic(hotelId, deskId));
        emitterMap.put(uuid, Pair.of(getTopic(hotelId, deskId), emitter));
    }

    @Override
    public void remove(String uuid) {
        if (emitterMap != null && emitterMap.containsKey(uuid)) {
            log.debug("Removing emitter for key: {}", uuid);
            emitterMap.remove(uuid);
        } else {
            log.debug("No emitter to remove for key: {}", uuid);
        }
    }

    @Override
    public Map<String, SseEmitter> findByHotelIdAndDeskId(String hotelId, String deskId) {
        log.debug("Finding emitters for topic", getTopic(hotelId, deskId));
        String topic = getTopic(hotelId, deskId);

        return this.emitterMap.entrySet()
            .stream()
            .filter(entry -> entry.getValue().getFirst().equals(topic))
            .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue().getSecond()));
    }

    private String getTopic(String hotelId, String deskId) {
        return hotelId + ":" + deskId;
    }
}
