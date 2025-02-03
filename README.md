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

| Name         | Description  |
|--------------|--------------|
| MONGO_DB_URL | Mongo db url |
