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

package com.esatus.ssi.bkamt.controller.verification.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProofRequestThread {

    @JsonProperty("thid")
    private String threadId;

    @JsonProperty("sender_order")
    private Integer senderOrder = 0;

    @JsonProperty("received_orders")
    private EmptyDTO emptyDTO;

    public ProofRequestThread() {}

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public void setSenderOrder(Integer senderOrder) {
        this.senderOrder = senderOrder;
    }

    public void setReceivedOrders(EmptyDTO empty) {
        this.emptyDTO = empty;
    }
}
