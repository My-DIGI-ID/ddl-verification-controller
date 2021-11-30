# Verification-Controller

## Prerequisites
To load the application properties set the environment variable for spring to ``dev``
```yaml
SPRING_PROFILES_ACTIVE = dev
```

### Application Environment variables
To successfully start the application, several environment variables have to be defined.
Find those placeholders in `application-dev.yml` and replace them:
* ``${{VERIFICATIONCONTROLLER_APIKEY}}`` API key of the verification controller (Example: `0pen$3s4meC0ntroller`)
* ``${{VERIFICATIONCONTROLLER_ENDPOINT}}`` Endpoint of the verification controller (Example: `https://2192847001ad.ngrok.io`)
* ``${{VERIFICATIONCONTROLLER_HARDWAREBINDING}}`` Enable or disable hardware binding validation (``true|false``) 
* ``${{VERIFICATIONCONTROLLER_AGENT_APIKEY}}`` Api-Key for the agent (Example: `0pen$3s4meAgent`)
* ``${{VERIFICATIONCONTROLLER_AGENT_APIURL}}`` Api-Url of the admin url (Example: `localhost:10080`)
* ``${{VERIFICATIONCONTROLLER_AGENT_RECIPIENTKEY}}`` DDL agent *verkey*, in this case for the seed `000...DDL`; to be retrieved via
  ```
  curl -X GET "http://localhost:10080/wallet/did" -H  "accept: application/json" -H "X-Api-Key: <VERIFICATIONCONTROLLER_AGENT_APIKEY>" | jq ".results[0]" | jq ".verkey" | sed 's/"//g'
  ```
  after the DDL agent has been started 
* ``${{VERIFICATIONCONTROLLER_AGENT_ENDPOINT`` DDL agent endpoint *URL / IP* (Example: `localhost:10000`)
* ``${{VERIFICATIONCONTROLLER_AGENT_ENDPOINTNAME`` DDL agent endpoint *name* (shown to credential holders in the Proof Request presentation dialog in the ID Wallet app). (Example: `Big Corporate`)
* ``${{VERIFICATIONCONTROLLER_CRED_DEFINITION_IDS`` Comma separated credential definitions (Example: `XnGEZ7gJxDNfxwnZpkkVcs:3:CL:988:Digitaler Führerschein`)

### Docker Environment variables

Navigate to `src/main/docker/` and rename `.env-default` file to `.env`.
This file is used by Docker to set all required environment variables for the Docker containers.

1. **Verify Agent (ACA-Py)**
    * `VERIFY_AGENT_GENESIS_URL`: URL of the genesis file the agent uses (Example: `https://raw.githubusercontent.com/My-DIGI-ID/Ledger-Genesis-Files/main/Test/pool_transactions_genesis`)
    * `VERIFY_AGENT_WALLET_KEY`: Key to generate the wallet with and unlocks the wallet (Example: Random number)
    * `VERIFY_AGENT_API_KEY`: Secures all requests to ACA-Py (used in `x-api-key` header) (Example: Random number)
    * `VERIFY_AGENT_WEBHOOK_API_KEY`: Send from ACA-Py in `x-api-key` header to the client application endpoints (Example: `0pen$3s4meWebhook`)

2. **Network**
    * `IP_ADDRESS`: The IP-Address of the system, the controller runs on 

3. **MongoDB**
    * `MONGODB_USERNAME`: Mongodb username (defaults to `admin123`)
    * `MONGODB_PASSWORD`: Mongodb password (defaults to `pass123`)
    
   **Important**
   Replace the placeholders in the MongoDB init file in ``/src/main/docker/mongodb/mogno-init.js`` with the correct values,
otherwise the mongodb will not be initialized and the connection will fail:
   * `var db = connect("[...]`
   * ```
     db.createUser(
     {
     user: <user>,
     pwd: <password>,
     [...]
     ```
   * `db.grantRolesToUser(<user>,[...]`

   Replace the placeholder for the mongodb connection in `src/main/resources/config/application-dev.yml`: 
   ```
   data:
     mongodb:
       uri: mongodb://<user>:<password>@localhost:27018/?authSource=VerificationController
   ```

## Development

To start your application in the dev profile, open the terminal, navigate to the `verification-controller` folder and
run the following commands:

```
docker-compose -f src/main/docker/agent-mongodb.yml up -d

./mvnw
```

The first step will deploy a MongoDB instance. The second step will deploy the application.

If the container doesn't start, or you want to recreate all containers, try
```
docker-compose -f src/main/docker/agent-mongodb.yml down -v --remove-orphans
```
and rebuild the containers with
```
docker-compose -f src/main/docker/agent-mongodb.yml up
```

## Sonar
Sonar is used to analyze code quality.

1. Start a local Sonar server
   ```
   docker-compose -f src/main/docker/sonar.yml up -d
   ```
1. Log on to http://localhost:9001 with default credentials `admin/admin`
1. Create a new empty project called `VerificationController`
1. Generate an access token and copy it to `sonar-project.properties`, property `sonar.login`

You can run a Sonar analysis by using
the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the Maven
plugin.

1. Run a Sonar analysis
   ```
   ./mvnw -Pprod clean verify sonar:sonar
   ```
   **Note:** To re-run the Sonar phase, make sure to specify the `initialize` phase to load the essential Sonar properties
   from the `sonar-project.properties` file.
   ```
   ./mvnw initialize sonar:sonar
   ```
1. Access the results on http://localhost:9001/dashboard?id=VerificationController


## Swagger-Ui

Swagger UI can be accessed using this URL: http://localhost:8090/swagger-ui/index.html

Note: The API key can be configured in ``src/main/resources/config/application-dev.yml`` (application properties) file which
can be used to interact with the API.

## MongoDB

There is a database init script `mongo-init.js` located in `src/main/docker/mongodb/` which connects to the MongoDB on
port 27017. The script creates an admin user with username `admin123` and password `pass123` (It is highly recommended to replace the default values)..

After the connection was established successful, a new user is created:
```
username: user123
password: 123pass
```

You can use this user to connect to the database with your favorite MongoDB access tool. We recommend using AdminMongo (see next chapter).

## AdminMongo

### Connect to the database

```
- localhost:8092
- Connection-Name: Verification Controller
- Connection-String: mongodb://user123:123pass@ddl-mongodb:27017/VerificationController?authSource=VerificationController
```

* Open `http://localhost:8092` in a browser
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
* You have a tool like ngrok installed on your system (https://ngrok.com/). You can use any other tool which provides the same functionality but this how-to uses ngrok. See the docs of you favorite tools on how to use it

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
To open this page, navigate to http://localhost:8090/demo/

Ngrok now creates three public endpoints which are tunneled to your local endpoints

If you currently do not have a service that can accept the request made to the callback url, you could use a service such as `https://webhook.site`.

Here you can create a temporary endpoint which you can use as a callback url. Just paste the url in the demo page here:
![Paste callback_url](./images/demo_page_callback_url.png)

When everything is working you should see a request made by the application after you scanned the QR Code

## Troubleshooting

### Error: javax.management.beanserver: Exception calling isInstanceOf java.lang.ClassNotFoundException...
- Remove the .m2 folder in your user directory and rebuild with ``mvnw``
