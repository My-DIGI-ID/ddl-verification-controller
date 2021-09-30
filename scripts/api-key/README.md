# API Keys
The API key is used to protect the init API and to track usage.

## Generate on you local machine
Prerequisites:
- Node v12

1. Open a terminal and run ``npm install``
1. Execute the api-key-generator.js file ``node api-key-generator.js``


## Generate using a docker container
1. Open a terminal
1. Execute the ``generate-api-key.sh`` script which will
    1. Created a new docker container
    1. Execute the api-key-generator.js file


The script will print out something like
```
c14080cb-b8e8-476c-867d-4c147de845a5
$2a$10$MMA47Zz.hNgCk4noyfA05eVJ0RQtUAeQjumSBkGFNmzSoATGOABFW
```

* The first line is the clear text password. (Do not use as ID for the verifier.)
* The second line is the hashed api key.

## Create a new verifier in the database

1. To get the root password run:
    ```bash
    export MONGODB_ROOT_PASSWORD=$(kubectl get secret --namespace default mongodb -o jsonpath="{.data.mongodb-root-password}" | base64 --decode)
    ```

1. To connect to your database, create a MongoDB(R) client container:
    ```bash
    kubectl run --namespace default mongodb-client --rm --tty -i --restart='Never' --env="MONGODB_ROOT_PASSWORD=$MONGODB_ROOT_PASSWORD" --image docker.io/bitnami/mongodb:4.4.6-debian-10-r29 --command -- bash
    ```

1. Then, run the following command:
    ```bash
    mongo admin --host "mongodb" --authenticationDatabase admin -u root -p $MONGODB_ROOT_PASSWORD 
    ```

1. Add generated API Key
    ```
    use VerificationController
    col = db.jhi_verifier
    col.insert({ "id": "<enter a randomely generated id here e.g. uuid>", "api_key": "<enter api key here>", "name": "Test" }))
    ```

## Remove a verifier from the database
1. Follow step 1-3 from "Create a new verifier in the database"
1. Remove API Key for a verifier
    ```
    use VerificationController
    col = db.jhi_verifier
    col.deleteOne({ "name": "Test" }))
    ```