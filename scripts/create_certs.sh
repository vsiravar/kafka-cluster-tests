#!/bin/bash

set -euxo pipefail

# Define the parent directory
PARENT_DIR="$(dirname "$(dirname "$(realpath "$0")")")"

# Define the SSL directory
SSL_DIR="$PARENT_DIR/ssl"

# Create directory if it doesn't exist
mkdir -p $SSL_DIR

# Generate CA key and certificate
openssl req -new -x509 -keyout $SSL_DIR/ca-key -out $SSL_DIR/ca-cert -days 365 -subj "/CN=Kafka-CA"

# Generate Kafka broker key and certificate signing request
openssl req -new -keyout $SSL_DIR/broker-key -out $SSL_DIR/broker-req -days 365 -subj "/CN=kafka"
openssl x509 -req -in $SSL_DIR/broker-req -CA $SSL_DIR/ca-cert -CAkey $SSL_DIR/ca-key -CAcreateserial -out $SSL_DIR/broker-cert -days 365

# Convert certificates to PKCS12 format
openssl pkcs12 -export -in $SSL_DIR/ca-cert -inkey $SSL_DIR/ca-key -out $SSL_DIR/ca.p12 -name kafka-ca -passout pass:kafka1234
openssl pkcs12 -export -in $SSL_DIR/broker-cert -inkey $SSL_DIR/broker-key -out $SSL_DIR/broker.p12 -name kafka-broker -passout pass:kafka1234

# Create keystore and truststore

keytool -importkeystore -destkeystore $SSL_DIR/keystore.jks -srckeystore $SSL_DIR/broker.p12 -srcstoretype PKCS12 -alias kafka-broker -storepass kafka1234 -keypass kafka1234

keytool -importcert -keystore $SSL_DIR/truststore.jks -file $SSL_DIR/ca-cert -alias kafka-ca -storepass kafka1234 -noprompt

