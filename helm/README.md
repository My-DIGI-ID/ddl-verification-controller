# Verification Controller Helm Chart

This chart is for installing the verification controller, some key facts:

1. Install a mongo database with a PVC
   1. This will require a storage class to be specify in the values file
1. Variables will be defaulted, which will require modification and overrides
## Minimal required values:

The values assume that aca-py agent is installed as verifier in the same namespace with the [helm chart](https://github.com/My-DIGI-ID/ssi-helm-charts/tree/main/charts/ssi-aca-py) and release name ddl-agent

```yaml
APIKey: APIKEY

agent:
  verkey: VERKEY
  webHookAPIKey: WEBHOOK_API_KEY
  APIKey: AGENT_APIKEY
  endpoint: PUBLIC_AGENT_ENDPOINT

mongodb:
  auth:
    rootPassword: MONGO_ROOT_PASSWORD
    password: MONGO_PASSWORD

ingress:
  hosts:
    - host: HOSTNAME
      paths:
        - path: /
          pathType: ImplementationSpecific
  tls:
  - hosts:
    - HOSTNAME
    secretName: tls-secret

```
## Values Notes

The following sections will break down into the specific value groups that relate to the environment variables used to drive service behaviour.  All other settings are standard kubernetes helm settings and the relevant documentation should be referenced.  Values are saved in either a secrets object, config map or as a standard deployment variable.  If not explicitly mentioned it should be assumed to be in the deployment environment specification.



### `agent` Group

| Parameter       | Description                                             | Environment Variable Mapping | Default                          |
| --------------- | ------------------------------------------------------- | ---------------------------- | -------------------------------- |
| `verkey`        | Agent verification key, stored i secrets object         | `VERI_AGENT_VERKEY`          |                                  |
| `webHookAPIKey` | Agent web-hook API Key, stored in secrets object        | `VERI_AGENT_WEBHOOK_API_KEY` |                                  |
| `APIKey`        | API Key for the aca-py issuer, stored in secrets object | `VERI_AGENT_API_KEY`         |                                  |
| `ariesAttachID` | Aries ID, stored in secrets object                      | `AGENT_ARIES_ATTACH_ID`      | `libindy-request-presentation-0` |
| `url`           | Issue aca-py agent url                                  | `VERI_AGENT_API_URL`         |                                  |
| `endpoint`      | Agent endpoint URL.                                     | `VERI_AGENT_ENDPOINT`        | `http://0.0.0.0:11000`           |
| `port`          | Port number for acapy agent, stored in config map       | `VERI_AGENT_PORT_ADMIN`      | `11080`                          |

### `guest` Group

| Parameter            | Description                                        | Environment Variable Mapping           | Default                                                       |
| -------------------- | -------------------------------------------------- | -------------------------------------- | ------------------------------------------------------------- |
| `invitationRedirect` | Guest invitation redirect url, must include `{id}` | `VERI_EMAIL_GUEST_INVITATION_REDIRECT` | `http://<releasename>:8080/guest/invitation/redirect?id={id}` |
| `definition`         | Guest credential for acapy agent and indy nodes    | `GUEST_CREDENTIAL_DEFINITION`          |                                                               |
| `name`               | Guest credential for acapy agent and indy nodes    | `GUEST_CREDENTIAL_NAME`                |                                                               |
| `schema`             | Guest db schema                                    | `GUEST_CREDENTIAL_SCHEMA`              |                                                               |
| `checkoutDelay`      | Checkout delay in seconds                          | `VERI_CHECKOUT_DELAY_IN_SECONDS`       | `120`                                                         |


### `swagger` Group

| Parameter | Description                                                                               | Environment Variable Mapping       | Default |
| --------- | ----------------------------------------------------------------------------------------- | ---------------------------------- | ------- |
| `host`    | Authorisation host for swagger ui page, must be format `"http(s)://host({port})/{path}/"` | `VERI_SWAGGER_UI_ID_PROVIDER_HOST` |         |


### `mongodb` Group

The full list of possible mongoDB helm values can be found at [Bitnami MongoDB Chart](https://github.com/bitnami/charts/tree/master/bitnami/mongodb/#installing-the-chart).  The key values, and there mappings, are detailed below.  By default the mongoDB chart is executed, unless the `enabled` variable is set to `false`, in which case all related values must point to a valid mongoDB database hosted elsewhere (e.g. cloud managed service).

| Parameter                  | Description                                                                              | Environment Variable Mapping  | Default                 |
| -------------------------- | ---------------------------------------------------------------------------------------- | ----------------------------- | ----------------------- |
| `enabled`                  | If `false` mongoDB chart is not executed                                                 | `VERI_CONTROLLER_SSL_ENABLED` | `false`                 |
| `host`                     | The host for mongoDB, if using chart leave blank                                         | `VERI_MONGODB_HOST`           | `<releasename>-mongodb` |
| `auth.rootUser`            | MongoDB Root username                                                                    | `VERO_MONGODB_USERNAME`       | `root`                  |
| `auth.rootPassword`        | MongoDB Root password, stored in secret                                                  | `VERI_MONGODB_PASSWORD`       |                         |
| `auth.username`            | MongoDB User, needed for chart creation. Not used by controller                          |                               | `mongouser`             |
| `auth.password`            | MongoDB password, needed for chart creation. Not used by controller, but must have value |                               |                         |
| `auth.database`            | MongoDB database, stored in configmap                                                    | `VERI_MONGODB_DB_NAME`        | `admin`                 |
| `auth.authDatabase`        | MongoDB authorisation database, stored in configmap                                      | `VERI_MONGODB_AUTH_DB_NAME`   | `admin`                 |
| `persistence.storageClass` | Kubernetes storage class for PVC                                                         |                               |                         |
| `persistence.size`         | PVC storage size to request                                                              |                               | `10Gi`                  |

### Defaulted Variables

These are typically expected to be left as defaults.  However, if they require being overridden then add the variables explicitly to the `overrides` value group, for example:

```yaml
overrides:
  VERI_MONGODB_PORT: 27018
```
Because of the dependancy on servivces such as keycloak, image build there are several variables which are not explicitly overwritten, or have values mappings.  The following is a list of those variables.

| Variable                             | Description                                                                               | Default                                                        |
| ------------------------------------ | ----------------------------------------------------------------------------------------- | -------------------------------------------------------------- |
| `VERI_AGENT_NAME`                    | Use `overrides.VERI_AGENT_NAME` to override                                               | `Verification-Agent`                                           |
| `VERI_ID_PROVIDER_PERMISSIONS_PATH`  | This is a standard keycloak path, will only change for specific changes to authentication | `auth/realms/{realm}/protocol/openid-connect/token`            |
| `VERI_ID_PROVIDER_TOKEN_PATH`        | This is a keycloak path and will only be changed if the id provider is changed            | `auth/realms/{realm}/protocol/openid-connect/token/introspect` |
| `VERI_MONGODB_PORT`                  | Defaults to `27017`, override using `overrides.VERI_MONGODB_PORT`                         | `27017`                                                        |
| `SPRING_PROFILES_ACTIVE`             | This should nearly always be 'localdocker'                                                | `localdocker`                                                  |
| `VERI_JWT_USER_IDENTIFIER_ENTRY_NAME`| This will only change if keycloak is not used                                             | `preferred_username`                                           |
| `AGENT_API_KEY_HEADER_NAME`          | Use `overrides.AGENT_API_KEY_HEADER_NAME` to override                                     | `X-API-Key`                                                    |
| `ACCR_API_KEY_HEADER_NAME`           | Use `overrides.ACCR_API_KEY_HEADER_NAME` to override                                      | `X-API-Key`                                                    |
| `VERI_AGENT_PORT_ADMIN`              | Port number for acapy agent, exposed through the docker image                             | `11080`                                                        |

### `env` Group

This is a section in which custom variables can be inserted and added to the deployment resource.  It is intended to be used if the image has been rebuilt with additional variables, for example the source code has been downloaded and modified for a custom usage.  This allows the main helm chart to be used with the ability to support simple custom changes.  An example usage would be:

```yaml
env:
  MYCUSTOMVARIABLE: "hello"
```
