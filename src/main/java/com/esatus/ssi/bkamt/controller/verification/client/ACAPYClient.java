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

package com.esatus.ssi.bkamt.controller.verification.client;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import com.esatus.ssi.bkamt.controller.verification.client.model.InvitationDTO;
import com.esatus.ssi.bkamt.controller.verification.client.model.ProofRecordDTO;
import com.esatus.ssi.bkamt.controller.verification.client.model.ProofRequestDTO;
import com.esatus.ssi.bkamt.controller.verification.client.model.ProofResponseDTO;

@Repository
@FeignClient(name = "ACAPYClient", url = "${ssibk.verification.controller.agent.apiurl}")
public interface ACAPYClient {

    @RequestMapping(method = RequestMethod.POST, value = "/connections/create-invitation")
    InvitationDTO createInvitation(@RequestHeader("X-API-KEY") String apiKey, @RequestParam(value="alias") String alias);

    @RequestMapping(method = RequestMethod.POST, value = "/present-proof/create-request")
    ProofResponseDTO createProofRequest(@RequestHeader("X-API-Key") String apiKey, @RequestBody ProofRequestDTO proofRequest);

    @RequestMapping(method = RequestMethod.GET, value= "/present-proof/records/{presentation_exchange_id}")
    ProofRecordDTO getProofRecord(@RequestHeader("X-API-KEY") String apiKey, @PathVariable("presentation_exchange_id") String presentationExchangeId);

    @RequestMapping(method = RequestMethod.DELETE, value= "/present-proof/records/{presentation_exchange_id}")
    void deleteProofRecord(@RequestHeader("X-API-KEY") String apiKey, @PathVariable("presentation_exchange_id") String presentationExchangeId);

}
