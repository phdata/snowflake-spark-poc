#!/bin/bash

set -e

# Generate unencrypted private key
openssl genrsa -out ../src/main/resources/private-rsa-key.pem 2048

# Generate public key
openssl rsa -in ../src/main/resources/private-rsa-key.pem -pubout -out ../src/main/resources/private-rsa-key.pub