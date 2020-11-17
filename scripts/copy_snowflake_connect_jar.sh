#!/bin/bash

set -e

docker cp ../lib/snowflake-kafka-connector-1.5.0.jar connect:/usr/share/confluent-hub-components/