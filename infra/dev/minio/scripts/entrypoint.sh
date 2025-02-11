#!/bin/bash

set -e

MINIO_ALIAS=balhom-currency-profile-minio

check_bucket() {
  if ! mc ls $MINIO_ALIAS/$MINIO_DEFAULT_BUCKET &>/dev/null; then
    mc mb $MINIO_ALIAS/$MINIO_DEFAULT_BUCKET
  else
    >&2 echo "Bucket already exists - skipping bucket creation"
  fi
}

# Check Minio availability
check_minio() {
  mc alias set $MINIO_ALIAS http://0.0.0.0:9090 $MINIO_ROOT_USER $MINIO_ROOT_PASSWORD --api "s3v4"
  until mc admin info $MINIO_ALIAS; do
    >&2 echo "MinIO is unavailable"
    sleep 5
  done
  >&2 echo "MinIO is UP"

  check_bucket
}

check_minio &

# Execute Minio server
exec minio server --address 0.0.0.0:9090 --console-address 0.0.0.0:9091 /bitnami/minio/data "$@"
