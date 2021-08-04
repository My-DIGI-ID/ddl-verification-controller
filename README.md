# Verification-Controller

## Prerequisites

### Environment variables
Navigate to `src/main/docker/` and rename the `.env-default` file to `.env`. This file is used by docker to set all required
environment variables passed into the docker containers.

There are different variables to set:

1. **VERIFY AGENT (ACA-PY)**
    * `VERIFY_AGENT_GENESIS_URL`: URL of the genesis file the agent uses
    * `VERIFY_AGENT_WALLET_KEY`:
    * `VERIFY_AGENT_API_KEY`:
    * `VERIFY_AGENT_WEBHOOK_APIKEY`: The key the agent uses to interact with our controller. The agent will send this value in X-AUTH-HEADER of the request

2. **Network**
    * `IP_ADDRESS`: Your current IP-Address

3. **MONGO DB**
    * `MONGODB_USERNAME`:
    * `MONGODB_PASSWORD`:
    * `CONTROLLER_DB_USERNAME`:
    * `CONTROLLER_DB_PASSWORD`:

4. CONTROLLER CREDENTIALS
    * `HOTEL_CONTROLLER_ADMIN_USERNAME`
    * `HOTEL_CONTROLLER_ADMIN_PASSWORD`
    * `HOTEL_CONTROLLER_AGENT_APIKEY`
    * `HOTEL_CONTROLLER_AGENT_RECIPIENTKEY`
    * `HOTEL_AGENT_MASTERID_CREDENTIAL_DEFINITION_IDS`
    * `HOTEL_AGENT_CORPORATEID_SCHEMA_IDS`
    * `HOTEL_CONTROLLER_AGENT_CORPORATEID_ISSUER_DIDS`
    * `HOTEL_CONTROLLER_INTEGRATIONSERVICE_APIKEY`

Add verification-agent-url, verification-agent-key,credential-definition-id in application-dev.yml file.

## Development
To start your application in the dev profile, open the terminal, navigate to the `verification-controller` folder and run the following commands:

```
docker-compose -f src/main/docker/agent-mongodb.yml up -d
./mvnw
```

The first step will deploy a MongoDB instance. The second step will deploy the application.

## Swagger-Ui

Swagger UI will be available at the following URL

```
http://localhost:8090/swagger-ui/index.html
```

Note: The API key can be configured in src/main/resources/config/application-dev.yml (application properties) file which can be used to interact with the API.

### Default authentication against the api
If you want to try out the api use the user you configured in ssibk->verification->controller->admin

E.g

```
	ssibk:
	      admin:
	        username: verification-username
	        password: verification-password

```

## MongoDB
There is an database init script `mongo-init.js` located in `src/main/docker/mongodb/` which connects to the mongodb on port 27017. The scripts creates an admin user with username: admin123 and password: pass123.

After the connection was established successfull it creates a new user:

```
username: user123
password: 123pass
```

You can use this user to connect to the database with your favourite MongoDB access tool. Here we use AdminMongo. You will find more information about how to use it below.

## AdminMongo

AdminMongo is running in Port 8092

```
- localhost:8092
- Connection-Name: Verification Controller
- Connection-String: mongodb://user123:123pass@docker_verification-controller-mongodb_1:27017/VerificationController?authSource=VerificationController
```

## Verifier
The database is initialized with a demo verifier with the following values:
```
name: demo
password: $2y$10$AW0Zit2JNBcTI0UDpPmc4OM72nm86AyvoOfV7GJOP4iropj9IuyVS
```

The password in plain text is `secure`. When you try to interact with the api endpoints use the plain value in the `X-AUTH-HEADER`.


To create a new verifier you have to create a password hash with bcrypt with a strength factor of 12
Password length may not be greater than 72 bytes because that the maxim password length bcrypt supports only

You can use any online bcrypt password generator, e.g. https://www.appdevtools.com/bcrypt-generator

## Endpoints
As mentioned above all endpoints are documented via swagger on `http://localhost:8090/swagger-ui/index.html`

Currently there are three different security mechanisms for these endpoints:
* `/api/proof`: No Authentication
* `/topic/present_proof`: The X-AUTH-HEADER is checked against the value configured in the `application-dev.yml` under `ssibk:verification:controller:apikey`
* `/*`: All requests to any other routes have to include a value in the `X-AUTH-HEADER` which matches the api-key of a verifier stored in the database 
(remember they are hashed in the database so don`t use the hash in the header, use the plan value instead)

## Building Docker Image

To build a docker dev image, run:

```
./mvnw package jib:dockerBuild
```

This one creates a new docker image with the name verificationcontroller.
To run the controller + MongoDB and the mongoadmin, run:

```
docker-compose -f src/main/docker/controller-mongo-mongoadmin.yml up -d
```

## Building for production

### Packaging as jar

To build the final jar and optimize the `verification-controller` application for production, run:

```
./mvnw -Pprod clean verify
```

To ensure everything worked, run:

```
java -jar target/*.jar
```

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./mvnw -Pprod,war clean verify
```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

You can run a Sonar analysis by using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the Maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

## Troubleshooting
### Error: javax.management.beanserver: Exception calling isInstanceOf java.lang.ClassNotFoundException...
- Remove the .m2 folder in your user directory and rebuild with ``mvnw``
