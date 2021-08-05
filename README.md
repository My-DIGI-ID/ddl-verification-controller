# Verification-Controller

## Prerequisites

### Environment variables

Navigate to `src/main/docker/` and rename the `.env-default` file to `.env`. This file is used by docker to set all
required environment variables passed into the docker containers.

There are different variables to set:

1. **VERIFY AGENT (ACA-PY)**
    * `VERIFY_AGENT_GENESIS_URL`: URL of the genesis file the agent uses
    * `VERIFY_AGENT_WALLET_KEY`:
    * `VERIFY_AGENT_API_KEY`:
    * `VERIFY_AGENT_WEBHOOK_APIKEY`: The key the agent uses to interact with our controller. The agent will send this
      value in X-AUTH-HEADER of the request

2. **Network**
    * `IP_ADDRESS`: Your current IP-Address

3. **MONGO DB**
    * `MONGODB_USERNAME`:
    * `MONGODB_PASSWORD`:
    * `CONTROLLER_DB_USERNAME`:
    * `CONTROLLER_DB_PASSWORD`:

4. **CONTROLLER CREDENTIALS**
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

To start your application in the dev profile, open the terminal, navigate to the `verification-controller` folder and
run the following commands:

```
docker-compose -f src/main/docker/agent-mongodb.yml up -d
./mvnw
```

The first step will deploy a MongoDB instance. The second step will deploy the application.

If the container won`t start, or you want to recreate all containers just run
```
docker-compose -f src/main/docker/agent-mongodb.yml down -V --remove-orphants
```
and rebuild the containers with
```
docker-compose -f src/main/docker/agent-mongodb.yml up
```

## Swagger-Ui

Swagger UI will be available at the following URL

```
http://localhost:8090/swagger-ui/index.html
```

Note: The API key can be configured in ``src/main/resources/config/application-dev.yml`` (application properties) file which
can be used to interact with the API.

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

There is an database init script `mongo-init.js` located in `src/main/docker/mongodb/` which connects to the mongodb on
port 27017. The scripts creates an admin user with username: admin123 and password: pass123.

After the connection was established successfull it creates a new user:

```
username: user123
password: 123pass
```

You can use this user to connect to the database with your favourite MongoDB access tool. Here we use AdminMongo. You
will find more information about how to use it below.

## AdminMongo

AdminMongo is running in Port 8092

```
- localhost:8092
- Connection-Name: Verification Controller
- Connection-String: mongodb://user123:123pass@docker_verification-controller-mongodb_1:27017/VerificationController?authSource=VerificationController
```

### Connect to the database

* Call `http://localhost:8092` in your browser
* Add the details shown above and click `Add connection`
  ![AdminMongo create connection](./images/admin_mongo_setup.png)
* After a page reload the connection is shown. Click connect to open the details view
  ![AdminMongo connection](./images/admin_mongo_connection.png)
* You can see all tables and the data
  ![AdminMongo overview](./images/admin_mongo_overview.png)
* When you select the verifier (jhi_verifier) table you can see the initial verifier stored by our init script
  ![AdminMongo overview](./images/admin_mongo_verifier.png)

## Verifier

The database is initialized with a demo verifier with the following values:

```
name: demo
password: $2y$10$AW0Zit2JNBcTI0UDpPmc4OM72nm86AyvoOfV7GJOP4iropj9IuyVS
```

The password in plain text is `secure`. When you try to interact with the api endpoints use the plain value in
the `X-AUTH-HEADER`.

To create a new verifier you have to create a password hash with bcrypt with a strength factor of 12 Password length may
not be greater than 72 bytes because that the maxim password length bcrypt supports only

You can use any online bcrypt password generator, e.g. https://www.appdevtools.com/bcrypt-generator

## Endpoints

As mentioned above all endpoints are documented via swagger on `http://localhost:8090/swagger-ui/index.html`

Currently, there are three different security mechanisms for these endpoints:

* `/api/proof`: No Authentication
* `/topic/present_proof`: The X-AUTH-HEADER is checked against the value configured in the `application-dev.yml`
  under `ssibk:verification:controller:apikey`
* `/*`: All requests to any other routes have to include a value in the `X-AUTH-HEADER` which matches the api-key of a
  verifier stored in the database
  (remember they are hashed in the database so don`t use the hash in the header, use the plan value instead)

## Testing with your mobile device and the ID Wallet App
To test the whole application with your mobile phone you need to make sure the following prerequisites are met:
* You have the ID Wallet app installed on your mobile device
  * iOS: https://apps.apple.com/at/app/id-wallet/id1564933989 
  * Android: https://play.google.com/store/apps/details?id=com.digitalenabling.idw&hl=de&gl=US
* All containers are running without any errors
* You have a tool like ngrok installed on your system (https://ngrok.com/). You can use any other tool which provides the same functionality but this how-to uses ngrok. See the docs of you favourite tools on how to use it

### Configure your IP Address
Make sure the correct IP Address of your PC in `/src/main/docker/.env`
```yaml
# NETWORK
IP_ADDRESS=192.168.152.43
```

### Start the application and execute ngrok
Open three terminals and navigate to the folder ngrok is located in each instance of your shell (e.g. Ngrok is in C:\dev\ngrok navigate with `cd C:\dev\ngrok`)
* `.\ngrok.exe http 8090`
* `.\ngrok.exe http 10000`
* `.\ngrok.exe http 10080`

### Open demo page
When the application runs with dev profile there is demo website where you can generate a QR-Code to scan with your Wallet-ID App.
To see this page call 

Ngrok now creates three public endpoints which are tunneled to your local endpoints

## Building Docker Image

To build a docker dev image, run:

```
./mvnw package jib:dockerBuild
```

This one creates a new docker image with the name verificationcontroller. To run the controller + MongoDB and the
mongoadmin, run:

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

You can run a Sonar analysis by using
the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the Maven
plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties
are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

## Troubleshooting

### Error: javax.management.beanserver: Exception calling isInstanceOf java.lang.ClassNotFoundException...

- Remove the .m2 folder in your user directory and rebuild with ``mvnw``
