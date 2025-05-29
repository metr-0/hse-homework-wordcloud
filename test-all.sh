#!/bin/bash

cd api-gateway
./mvnw test

cd ../file-analises-service
./mvnw test

cd ../file-storing-service
./mvnw test
