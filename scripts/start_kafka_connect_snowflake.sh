#!/bin/bash

set -e

curl -X POST -H "Content-Type: application/json" --data @../src/main/resources/private-kafka-connect.json http://localhost:8083/connectors