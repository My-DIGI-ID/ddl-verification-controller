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

#!/usr/bin/env groovy

node {
    def customImage
    def registryCredential
    def registry
    def image_tag
    def image_Name = '/verification-controller'

   stage('Checkout repository') {
      echo "#------------------- Checkout Develop Branch -------------------#"
      checkout scm

      script {
       echo "#------------------- load environment variable -------------------#"

       load "${env.WORKSPACE}" + "/.env"
       echo "${env.CURRENT_BRANCH_NAME}"
       echo "${env.VERSION_TAG}"
       echo "${env.REGISTRY_PREFIX}"

       image_tag ="${env.VERSION_TAG}"
       registryCredential = "${env.REGISTRY_CREDENTIAL}"
       registry = "${env.REGISTRY_PREFIX}" + image_Name

       echo "#------------------- environment variable loaded-------------------#"
      }
    }

    stage('Build image') {
      echo "#------------------- Build Docker Image-------------------#"

      customImage = docker.build(registry)

      echo "#------------------- Image Build Complete -------------------#"
    }

    stage('Push image to custom registry') {
      echo "#------------------- Push Image to custom registry-------------------#"

          docker.withRegistry('https://de.icr.io', registryCredential) {
              customImage.push("${env.BUILD_NUMBER}")
              customImage.push(image_tag)
          }

      echo "#------------------- Image Pushed to registry -------------------#"

    }
}
