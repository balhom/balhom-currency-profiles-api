#!/bin/bash

set -e

# Wait until Keycloak is initialized
until curl -s -o /dev/null http://localhost:8080; do
    echo "Waiting for Keycloak to be ready..."
    sleep 5
done

export PATH=$PATH:/opt/bitnami/keycloak/bin

# More info on how to use kcadm.sh on:
# https://access.redhat.com/documentation/en-us/red_hat_single_sign-on/7.5/html/server_administration_guide/admin_cli


echo "Starting initialization of Keycloak server"

## Log in as master realm admin
echo "Logging into Keycloak realm master as ${KC_BOOTSTRAP_ADMIN_USERNAME}"
kcadm.sh config credentials --server http://localhost:8080 --realm master \
--user "${KC_BOOTSTRAP_ADMIN_USERNAME}" --password "${KC_BOOTSTRAP_ADMIN_PASSWORD}"

## Realm creation step
if ! kcadm.sh get realms/${KEYCLOAK_INIT_REALM} &>/dev/null; then
  echo "Creating '${KEYCLOAK_INIT_REALM}' realm"
  kcadm.sh create realms -s realm="${KEYCLOAK_INIT_REALM}" -s enabled=true -s registrationEmailAsUsername=true \
  -s loginWithEmailAllowed=true -s smtpServer.host=${KEYCLOAK_INIT_MAILHOG_HOST} -s smtpServer.port=1025 \
  -s smtpServer.auth=false -s smtpServer.ssl=none -s smtpServer.replyTo=${KEYCLOAK_INIT_SMTP_RECEIVER} \
  -s smtpServer.from=${KEYCLOAK_INIT_SMTP_SENDER}

  # Init realm user
  echo "Creating '${KEYCLOAK_INIT_USER}' user in '${KEYCLOAK_INIT_REALM}' realm"
  kcadm.sh create users -r "${KEYCLOAK_INIT_REALM}" -s username="${KEYCLOAK_INIT_USER}" -s enabled=true \
  -s email="${KEYCLOAK_INIT_USER}" -s emailVerified=true -s firstName="Test" -s lastName="Test"
  kcadm.sh set-password -r "${KEYCLOAK_INIT_REALM}" --username "${KEYCLOAK_INIT_USER}" \
  --new-password "${KEYCLOAK_INIT_USER_PASSWORD}"
  echo "Created '${KEYCLOAK_INIT_USER}' user in '${KEYCLOAK_INIT_REALM}' realm"

  # Create client
  echo "Creating '${KEYCLOAK_INIT_CLIENT}' client"
  kcadm.sh create clients -r "${KEYCLOAK_INIT_REALM}" -s clientId="${KEYCLOAK_INIT_CLIENT}" -s enabled=true -s \
  publicClient=true -s redirectUris='["*"]' -s webOrigins='["*"]' -s directAccessGrantsEnabled=true \
  -s standardFlowEnabled=true
  echo "Created new client with id '${KEYCLOAK_INIT_CLIENT}'"
else
  >&2 echo "Realm already exists, skipping keycloak init"
fi

echo "Keycloak Server initialization completed!"
