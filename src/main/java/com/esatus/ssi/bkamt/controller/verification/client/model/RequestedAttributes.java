/*
 * Copyright 2021 Bundesrepublik Deutschland
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.esatus.ssi.bkamt.controller.verification.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestedAttributes {

  @JsonProperty("masterId")
  private ProofRequestProperty masterId;

  public RequestedAttributes() {}

  public void setMasterId(ProofRequestProperty masterId) {
    this.masterId = masterId;
  }

  /**
   * @SuppressWarnings("unchecked") @JsonProperty("requested_attributes") private void unpackNested(Map<String,Object>
   * requested_attributes) { this.additionalProp1 = (String)requested_attributes.get("additionalProp1"); this.name =
   * additionalProp1.get("name"); Map<String,String> name = (Map<String,String>)additionalProp1.get("name");
   * Map<String,String> name = (Map<String,String>)additionalProp1.get("name"); }
   **/
}
