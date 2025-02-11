# BalHom Currency Profiles API

BalHom Currency Profiles API which acts as a microservice for BalHom infrastructure

## Error Codes

| Code | Description                               |
|------|-------------------------------------------|
| 100  | "Currency Profile not found"              |
| 101  | "The init date must not be in the future" |

> **1 to 99** Generic errors \
> **100 to 199** Currency Profile related errors

## Environment Variables

| Name            | Description                                         |
|-----------------|-----------------------------------------------------|
| KEYCLOAK_URL    | Keycloak instance url                               |
| KEYCLOAK_REALM  | Keycloak instance realm name. Default: balhom-realm |
| KEYCLOAK_CLIENT | Keycloak instance client id. Default: balhom-client |
| MONGODB_URL     | Mongo instance url                                  |
| MONGODB_DB      | Mongo instance database name                        |
| S3_URL          | S3 url                                              |
| S3_REGION       | S3 region. Default: us-west-2                       |
| S3_ACCESS_KEY   | S3 access key                                       |
| S3_SECRET_KEY   | S3 secret key                                       |
| S3_BUCKET_NAME  | S3 bucket name. Default: balhom-currency-api-bucket |
| KAFKA_SERVERS   | Kafka server urls                                   |
