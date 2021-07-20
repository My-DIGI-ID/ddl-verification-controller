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

import java.net.URI;
import com.esatus.ssi.bkamt.controller.verification.service.dto.WebhookPresentProofDTO;

public interface ProofService {

    URI getProofURI(String hotelId, String deskId);

	void handleProofWebhook(WebhookPresentProofDTO webhookPresentProofDTO);

}
